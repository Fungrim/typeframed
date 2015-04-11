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

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import org.typeframed.api.Envelope;
import org.typeframed.api.EnvelopeWriter;
import org.typeframed.api.HeaderProvider;
import org.typeframed.api.MessageTypeDictionary;
import org.typeframed.api.digest.ChecksumProvider;

public class StreamWriter<H> implements EnvelopeWriter<H> {

	private OutputStream target;
	private ByteBufferWriter<H> buffer;
	
	public StreamWriter(MessageTypeDictionary dictionary, OutputStream target) {
		this(dictionary, target, 512);
	}
	
	public StreamWriter(MessageTypeDictionary dictionary, OutputStream target, int bufferSize) {
		buffer = new ByteBufferWriter<H>(dictionary, bufferSize);
		checkNotNull(target);
		this.target = target;
	}
	
	public void setChecksumProvider(ChecksumProvider checksum) {
		this.buffer.setChecksumProvider(checksum);
	}
	
	public void setHeaderProvider(HeaderProvider<H> header) {
		this.buffer.setHeaderProvider(header);
	}
	
	@Override
	public void write(Envelope<H> env) throws IOException {
		ByteBuffer bytes = this.buffer.asByteBuffer(env);
		while(bytes.remaining() > 0) {
			target.write(bytes.get());
		}
	}
}
