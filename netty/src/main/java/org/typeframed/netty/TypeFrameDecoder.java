package org.typeframed.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.MessageList;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

import java.io.IOException;
import java.util.Arrays;

import org.typeframed.api.BuilderConfig;
import org.typeframed.api.ChecksumProvider;
import org.typeframed.api.CurruptedChecksumException;
import org.typeframed.api.HeaderProvider;
import org.typeframed.api.TypeDictionary;
import org.typeframed.api.TypeFrame;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message.Builder;

public class TypeFrameDecoder<H> extends ByteToMessageDecoder {
	
	private final TypeDictionary dictionary;
	
	private ChecksumProvider checksum;
	private HeaderProvider<H> header;
	
	public TypeFrameDecoder(TypeDictionary dictionary) {
		this.dictionary = dictionary;
	}
	
	@SuppressWarnings("unchecked")
	public TypeFrameDecoder(BuilderConfig config) {
		this.dictionary = config.getDictionary();
		this.checksum = config.getChecksum();
		this.header = (HeaderProvider<H>) config.getHeader();
	}

	public void setChecksum(ChecksumProvider checksum) {
		this.checksum = checksum;
	}
	
	public void setHeader(HeaderProvider<H> header) {
		this.header = header;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, MessageList<Object> out) throws Exception {
		in.markReaderIndex();
		int len = tryParseVarint(in);	
		if(len == -1) {
			in.resetReaderIndex();
			return; // could not read length
		}
		int type = tryParseVarint(in);
		if(type == -1) {
			in.resetReaderIndex();
			return; // could not parse type
		}
		int fullLen = calculateFullLength(len);
		if(in.isReadable(fullLen)) {
			out.add(createTypeFrame(in, len, type));
		} else {
			in.resetReaderIndex();
		}
	}

	private TypeFrame<H> createTypeFrame(ByteBuf in, int len, int type) throws IOException, InvalidProtocolBufferException {
		TypeFrame<H> frame = new TypeFrame<H>();
		frame.setLength(len);
		frame.setType(type);
		
		checkSetHeader(in, frame);
		
		byte[] rawMessage = new byte[len];
		in.readBytes(rawMessage);
		
		checkSetChecksum(in, frame, rawMessage);
		
		Builder builder = dictionary.getBuilderForId(type);
		CodedInputStream cin = CodedInputStream.newInstance(rawMessage);
		do {
			builder.mergeFrom(cin);
		} while(cin.getBytesUntilLimit() > 0);
		cin.checkLastTagWas(0);
		
		frame.setMessage(builder.build());
		return frame;
	}

	private void checkSetChecksum(ByteBuf in, TypeFrame<H> frame, byte[] rawMessage) {
		if(checksum != null) {
			byte[] check = new byte[checksum.getByteLength()];
			in.readBytes(check);
			byte[] current = checksum.calculate(rawMessage);
			if(!Arrays.equals(check, current)) {
				throw new CurruptedChecksumException(); // TODO message?
			}
			frame.setChecksum(check);
		}
	}

	private void checkSetHeader(ByteBuf in, TypeFrame<H> frame) {
		if(header != null) {
			byte[] buff = new byte[header.getByteLength()];
			in.readBytes(buff);
			frame.setHeader(header.parse(buff));
		}
	}

	private int calculateFullLength(int len) {
		int full = (checksum == null ? len : len + checksum.getByteLength());
		full += (header == null ? 0 : header.getByteLength());
		return full;
	}

	/*
	 * The below method comes from the Netty project: Copyright 2012 The Netty Project
	 */
	private int tryParseVarint(ByteBuf in) throws IOException {
        byte[] buf = new byte[5];
        for (int i = 0; i < buf.length; i ++) {
            if (!in.isReadable()) {
                in.resetReaderIndex();
                return -1;
            }
            buf[i] = in.readByte();
            if (buf[i] >= 0) {
                int length = CodedInputStream.newInstance(buf, 0, i + 1).readRawVarint32();
                if (length < 0) {
                    throw new CorruptedFrameException("negative length: " + length);
                }
                return length;
            }
        }
        // Couldn't find the byte whose MSB is off.
        throw new CorruptedFrameException("Varint length wider than 32-bit");
	}
}
