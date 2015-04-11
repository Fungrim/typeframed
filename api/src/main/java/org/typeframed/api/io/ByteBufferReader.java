/**
 * Copyright 2015 Lars J. Nilsson <contact@larsan.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.typeframed.api.io;

import static org.typeframed.api.util.Varints.readRawVarint32;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.typeframed.api.Envelope;
import org.typeframed.api.EnvelopeReader;
import org.typeframed.api.HeaderProvider;
import org.typeframed.api.MessageTypeDictionary;
import org.typeframed.api.digest.ChecksumProvider;
import org.typeframed.api.digest.CurruptedChecksumException;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.Message.Builder;

/**
 * This class is able to {@link #consume(ByteBuffer)} a number of byte
 * buffer and parse the result into an envelope. The calling code must
 * check the {@link #isDone()} method to figure out when the envelope is 
 * fully read. 
 * 
 * <p>This class is a simple state machine which is not safe for concurrent 
 * use. It can be {@link #reset()} for multiple uses, in which case the current
 * buffer size will be reused. 
 */
public class ByteBufferReader<H> implements EnvelopeReader<H> {

	enum State {
		TYPE,
		HEAD_LEN,
		HEAD,
		MSG_LEN,
		MSG,
		CHKSUM_LEN,
		CHKSUM,
		DONE
	}
	
	private ChecksumProvider checksum;
	private HeaderProvider<H> header;
	private MessageTypeDictionary dictionary;
	
	private Envelope<H> result;
	private UnsafeByteArrayOutputStream buffer;
	private State state;
	
	// ** begin msg members
	private int type;
	private int headLen;
	private int msgLen;
	private byte[] rawMessage;
	private int chksumLen;
	private byte[] rawChksum;
	// ** end msg members
	
	public ByteBufferReader(MessageTypeDictionary dictionary) {
		this(dictionary, 512);
	}
	
	public ByteBufferReader(MessageTypeDictionary dictionary, int buffersize) {
		this.buffer = new UnsafeByteArrayOutputStream(buffersize);
		this.dictionary = dictionary;
		reset();
	}

	public void setChecksumProvider(ChecksumProvider checksum) {
		this.checksum = checksum;
	}
	
	public void setHeaderProvider(HeaderProvider<H> header) {
		this.header = header;
	}
	
	@Override
	public Envelope<H> read() throws IOException {
		if(this.rawChksum != null) {
			byte[] current = checksum.calculate(rawMessage);
			if(!Arrays.equals(this.rawChksum, current)) {
				throw new CurruptedChecksumException(); // TODO message?
			}
		}
		Builder builder = dictionary.getBuilderForId(type);
		CodedInputStream cin = CodedInputStream.newInstance(rawMessage);
		do {
			builder.mergeFrom(cin);
		} while(cin.getBytesUntilLimit() > 0);
		cin.checkLastTagWas(0);
		result.setMessage(builder.build());
		return result;
	}
	
	/**
	 * Read bytes from a byte buffer into the result, and return the number of bytes 
	 * read. Callers should check if the parsing is {@link #isDone() done} when this method
	 * returns.
	 */
	public int consume(ByteBuffer bytes) throws IOException {
		switch(state) {
			case TYPE : return fillType(bytes);
			case HEAD_LEN : return fillHeadLen(bytes);
			case HEAD : return fillHead(bytes);
			case MSG_LEN : return fillMsgLen(bytes);
			case MSG : return fillMsg(bytes);
			case CHKSUM_LEN : return fillChksumLen(bytes);
			case CHKSUM : return fillChksum(bytes);
			case DONE : 
			default : return 0; 
		}
	}
	
	public void reset() {
		this.result = new Envelope<H>();
		this.buffer.reset(false);
		this.state = State.TYPE;
		this.type = 0;
		this.headLen = 0;
		this.msgLen = 0;
		this.chksumLen = 0;
		this.rawChksum = null;
		this.rawMessage = null;
	}
	
	public boolean isDone() {
		return state == State.DONE;
	}

	private int fillChksum(ByteBuffer bytes) throws IOException {
		if(this.chksumLen == 0) {
			this.state = State.DONE;
			return consume(bytes);
		} else {
			int mark = buffer.size();
			tryBuffer(bytes, this.chksumLen - mark);
			int len = buffer.size() - mark;
			if(buffer.size() == this.chksumLen) {
				ensureChecksumProvider();
				this.rawChksum = buffer.toByteArray();
				return len + advanceAndRecurse(bytes, State.DONE);
			} else {
				return len;
			}
		}
	}

	private int advanceAndRecurse(ByteBuffer bytes, State next) throws IOException {
		this.buffer.reset(false);
		this.state = next;
		return consume(bytes);
	}

	private int fillChksumLen(ByteBuffer bytes) throws IOException {
		int mark = buffer.size();
		int i = tryReadVarint(bytes);
		int len = buffer.size() - mark;
		if(i == -1) {
			return len;
		} else {
			this.chksumLen = i;
			return len + advanceAndRecurse(bytes, State.CHKSUM); 
		}
	}

	private int fillMsg(ByteBuffer bytes) throws IOException {
		int mark = buffer.size();
		tryBuffer(bytes, this.msgLen - mark);
		int len = buffer.size() - mark;
		if(buffer.size() == this.msgLen) {
			this.rawMessage = buffer.toByteArray();
			return len + advanceAndRecurse(bytes, State.CHKSUM_LEN); 
		} else {
			return len;
		}
	}

	private int fillMsgLen(ByteBuffer bytes) throws IOException {
		int mark = buffer.size();
		int i = tryReadVarint(bytes);
		int len = buffer.size() - mark;
		if(i == -1) {
			return len;
		} else {
			this.msgLen = i;
			return len + advanceAndRecurse(bytes, State.MSG); 
		}
	}

	private int fillHead(ByteBuffer bytes) throws IOException {
		if(this.headLen == 0) {
			this.state = State.MSG_LEN;
			return consume(bytes);
		} else {
			int mark = buffer.size();
			tryBuffer(bytes, this.headLen - mark);
			int len = buffer.size() - mark;
			if(buffer.size() == this.headLen) {
				ensureHeaderProvider();
				byte[] tmp = buffer.toByteArray();
				result.setHeader(header.fromBytes(tmp));
				return len + advanceAndRecurse(bytes, State.MSG_LEN); 
			} else {
				return len;
			}
		}
	}
	
	private void ensureHeaderProvider() {
		if(header == null) {
			throw new IllegalStateException("Mismatch, incoming message contains header, but header provider is null");
		}
	}
	
	private void ensureChecksumProvider() {
		if(checksum == null) {
			throw new IllegalStateException("Mismatch, incoming message contains checksum, but checksum provider is null");
		}
	}

	private void tryBuffer(ByteBuffer input, int len) {
		int count = 0;
		while(count < len && input.hasRemaining()) {
			buffer.write(input.get());
			count++;
		}
	}

	private int fillHeadLen(ByteBuffer bytes) throws IOException {
		int mark = buffer.size();
		int i = tryReadVarint(bytes);
		int len = buffer.size() - mark;
		if(i == -1) {
			return len;
		} else {
			this.headLen = i;
			return len + advanceAndRecurse(bytes, State.HEAD); 
		}
	}

	private int fillType(ByteBuffer bytes) throws IOException {
		int mark = buffer.size();
		int i = tryReadVarint(bytes);
		int len = buffer.size() - mark;
		if(i == -1) {
			return len;
		} else {
			this.type = i;
			this.buffer.reset(false);
			this.state = State.HEAD_LEN;
			return len + consume(bytes);
		}
	}

	private int tryReadVarint(ByteBuffer bytes) throws IOException {
		try {
			return readRawVarint32(new BufferInputStream(bytes));
		} catch(BufferOverrun f) {
			return -1;
		}
	}
	
	private static class BufferOverrun extends RuntimeException  {
		
		private static final long serialVersionUID = -5254264203245344853L;

		private BufferOverrun() { }
		
	}
	
	private class BufferInputStream extends InputStream {
		
		private final ByteBuffer input;
		private final ByteBuffer prefilled;
		
		private BufferInputStream(ByteBuffer input) {
			this.prefilled = buffer.asByteBuffer();
			this.input = input;
		}

		@Override
		public int read() throws IOException {
			if(prefilled.hasRemaining()) {
				return prefilled.get();
			}
			if(input.hasRemaining()) {
				int i = input.get();
				buffer.write(i);
				return i;
			} else {
				throw new BufferOverrun();
			}
		}
	}
}
