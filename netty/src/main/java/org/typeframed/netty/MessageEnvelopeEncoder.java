package org.typeframed.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;

import org.typeframed.api.BuilderConfig;
import org.typeframed.api.ChecksumProvider;
import org.typeframed.api.HeaderProvider;
import org.typeframed.api.MessageEnvelope;
import org.typeframed.api.TypeDictionary;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Message;

public class MessageEnvelopeEncoder<H> extends MessageToByteEncoder<MessageEnvelope<H>> {

	private ChecksumProvider checksum;
	private HeaderProvider<H> header;
	private final TypeDictionary dictionary;
	
	public MessageEnvelopeEncoder(TypeDictionary dictionary) {
		this.dictionary = dictionary;
	}
	
	@SuppressWarnings("unchecked")
	public MessageEnvelopeEncoder(BuilderConfig config) {
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
	protected void encode(ChannelHandlerContext ctx, MessageEnvelope<H> env, ByteBuf out) throws Exception {
		Message msg = env.getMessage();
		MessageDigest digest = checkDigest();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DigestOutputStream dout = new DigestOutputStream(bout, digest);
		dout.on(false); // calculating only on message itself
		CodedOutputStream cout = CodedOutputStream.newInstance(dout);
		int len = msg.getSerializedSize();
		int type = dictionary.getId(msg);
		cout.writeRawVarint32(len);
		cout.writeRawVarint32(type);
		cout.flush();
		if(header != null) {
			header.write(env.getHeader(), dout);
		}
		dout.on(true); // start recording
		digest.reset();
		msg.writeTo(cout);
		cout.flush();
		dout.on(false); // end recording
		byte[] checksum = digest.digest();
		if(checksum != null) {
			dout.write(checksum);
		}
		dout.flush();
		byte[] bytes = bout.toByteArray();
		out.writeBytes(bytes);
	}

	private MessageDigest checkDigest() {
		if(this.checksum != null) {
			return this.checksum.toDigest();
		} else {
			return new MessageDigest("NULL") {
				
				@Override
				protected byte[] engineDigest() {
					return null;
				}
				
				@Override
				protected void engineReset() { }
				
				@Override
				protected void engineUpdate(byte input) { }
				
				@Override
				protected void engineUpdate(byte[] input, int offset, int len) { }
				
				@Override
				public byte[] digest() {
					return null;
				}
			};
		}
	}
}
