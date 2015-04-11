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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.DigestOutputStream;
import java.security.MessageDigest;

import org.typeframed.api.Envelope;
import org.typeframed.api.EnvelopeWriter;
import org.typeframed.api.HeaderProvider;
import org.typeframed.api.MessageTypeDictionary;
import org.typeframed.api.digest.ChecksumProvider;
import org.typeframed.api.util.NullDigest;

import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Message;

public class ByteBufferWriter<H> implements EnvelopeWriter<H> {

	private int bufferSize;
	private ByteBuffer result;
	private MessageTypeDictionary dictionary;
	private HeaderProvider<H> header;
	private ChecksumProvider checksum;
	
	public ByteBufferWriter(MessageTypeDictionary dictionary) {
		this(dictionary, 512);
	}
	
	public ByteBufferWriter(MessageTypeDictionary dictionary, int bufferSize) {
		checkArgument(bufferSize > 0, "Buffer size must be more than zero");
		checkNotNull(dictionary);
		this.dictionary = dictionary;
		this.bufferSize = bufferSize;
	}
	
	public void setChecksumProvider(ChecksumProvider checksum) {
		this.checksum = checksum;
	}
	
	public void setHeaderProvider(HeaderProvider<H> header) {
		this.header = header;
	}
	
	public ByteBuffer getResult() {
		return result;
	}

	@Override
	public void write(Envelope<H> env) throws IOException {
		asByteBuffer(env);
	}
	
	public ByteBuffer asByteBuffer(Envelope<H> env) throws IOException {
		Message msg = env.getMessage();
		MessageDigest digest = checkDigest();
		UnsafeByteArrayOutputStream bout = new UnsafeByteArrayOutputStream(bufferSize);
		DigestOutputStream dout = new DigestOutputStream(bout, digest);
		CodedOutputStream cout = CodedOutputStream.newInstance(dout);
		// dout.on(false); // calculating only on message itself
		/*
		 * 1: Mandatory type
		 */
		int type = dictionary.getId(msg);
		cout.writeRawVarint32(type);
		/*
		 * 2: Optional header, length + data
		 */
		if(header != null) {
			byte[] tmp = header.toBytes(env.getHeader());
			cout.writeRawVarint32(tmp.length);
			cout.writeRawBytes(tmp);
		} else {
			cout.writeRawVarint32(0);
		}
		/*
		 * 3: Message, length + data
		 */
		int len = msg.getSerializedSize();
		cout.writeRawVarint32(len);
		cout.flush(); // !! (the coded output stream is caching... )
		dout.on(true); // start recording digest
		digest.reset();
		msg.writeTo(cout);
		cout.flush(); // !! (the coded output stream is caching... )
		dout.on(false); // end recording digest
		/*
		 * 4: Optional checksum, length + data
		 */
		byte[] checksum = digest.digest();
		if(checksum != null) {
			cout.writeRawVarint32(checksum.length);
			cout.writeRawBytes(checksum);
		} else {
			cout.writeRawVarint32(0);
		}
		cout.flush(); 
		this.result = bout.asByteBuffer();
		return result;
	}
	
	private MessageDigest checkDigest() {
		if(this.checksum != null) {
			return this.checksum.toDigest();
		} else {
			return NullDigest.INSTANCE;
		}
	}
}
