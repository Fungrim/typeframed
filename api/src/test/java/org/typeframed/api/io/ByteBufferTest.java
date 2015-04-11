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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.ByteBuffer;

import org.junit.Test;
import org.typeframed.api.Envelope;
import org.typeframed.api.Msg.Tell;

public class ByteBufferTest extends BaseParseTest {
	
	@Test
	public void testMessageWithHeaderAndChecksumInSingleBuffer() throws Exception {
		Tell msg = Tell.newBuilder().setMsg("hello worlds").build();
		ByteBufferWriter<Integer> writer = new ByteBufferWriter<Integer>(types);
		writer.setChecksumProvider(checksum);
		writer.setHeaderProvider(header);
		writer.write(new Envelope<Integer>(1, msg));
		ByteBuffer bytes = writer.getResult();
		int len = bytes.remaining();
		ByteBufferReader<Integer> reader = new ByteBufferReader<Integer>(types);
		reader.setChecksumProvider(checksum);
		reader.setHeaderProvider(header);
		int read = reader.consume(bytes);
		assertEquals(len, read);
		assertTrue(reader.isDone());
		Envelope<Integer> env = reader.read();
		assertEquals(1, (int) env.getHeader()); 
		assertEquals(msg.getMsg(), ((Tell)env.getMessage()).getMsg());
	}

	@Test
	public void testMessageWithHeaderAndChecksumInMultipleBuffers() throws Exception {
		Tell msg = Tell.newBuilder().setMsg("hello worlds").build();
		ByteBufferWriter<Integer> writer = new ByteBufferWriter<Integer>(types);
		writer.setChecksumProvider(checksum);
		writer.setHeaderProvider(header);
		writer.write(new Envelope<Integer>(1, msg));
		ByteBuffer bytes = writer.getResult();
		ByteBufferReader<Integer> reader = new ByteBufferReader<Integer>(types);
		while(bytes.hasRemaining()) {
			ByteBuffer tmp = ByteBuffer.wrap(new byte[] { bytes.get() });
			reader.setChecksumProvider(checksum);
			reader.setHeaderProvider(header);
			reader.consume(tmp);
		}
		assertTrue(reader.isDone());
		Envelope<Integer> env = reader.read();
		assertEquals(1, (int) env.getHeader()); 
		assertEquals(msg.getMsg(), ((Tell)env.getMessage()).getMsg());
	}
	
	@Test
	public void testMessageWithHeaderAndChecksumAtEverySlicePoint() throws Exception {
		Tell msg = Tell.newBuilder().setMsg("hello worlds").build();
		ByteBufferWriter<Integer> writer = new ByteBufferWriter<Integer>(types);
		writer.setChecksumProvider(checksum);
		writer.setHeaderProvider(header);
		writer.write(new Envelope<Integer>(1, msg));
		ByteBuffer bytes = writer.getResult();
		ByteBufferReader<Integer> reader = new ByteBufferReader<Integer>(types);
		int len = bytes.remaining();
		for (int i = 0; i < len; i++) {
			bytes.position(i);
			ByteBuffer b2 = bytes.slice();
			bytes.rewind();
			reader.setChecksumProvider(checksum);
			reader.setHeaderProvider(header);
			reader.consume(bytes);
			reader.consume(b2);
			assertTrue(reader.isDone());
			Envelope<Integer> env = reader.read();
			assertEquals(1, (int) env.getHeader()); 
			assertEquals(msg.getMsg(), ((Tell)env.getMessage()).getMsg());
			bytes.limit(len);
			bytes.position(0);
		}	
	}
	
	@Test
	public void testMessageWithHeaderInSingleBuffer() throws Exception {
		Tell msg = Tell.newBuilder().setMsg("hello worlds").build();
		ByteBufferWriter<Integer> writer = new ByteBufferWriter<Integer>(types);
		writer.setHeaderProvider(header);
		writer.write(new Envelope<Integer>(1, msg));
		ByteBuffer bytes = writer.getResult();
		int len = bytes.remaining();
		ByteBufferReader<Integer> reader = new ByteBufferReader<Integer>(types);
		reader.setHeaderProvider(header);
		int read = reader.consume(bytes);
		assertEquals(len, read);
		assertTrue(reader.isDone());
		Envelope<Integer> env = reader.read();
		assertEquals(1, (int) env.getHeader()); 
		assertEquals(msg.getMsg(), ((Tell)env.getMessage()).getMsg());
	}
	
	@Test
	public void testMessageWithHeaderMultipleBuffers() throws Exception {
		Tell msg = Tell.newBuilder().setMsg("hello worlds").build();
		ByteBufferWriter<Integer> writer = new ByteBufferWriter<Integer>(types);
		writer.setHeaderProvider(header);
		writer.write(new Envelope<Integer>(1, msg));
		ByteBuffer bytes = writer.getResult();
		ByteBufferReader<Integer> reader = new ByteBufferReader<Integer>(types);
		while(bytes.hasRemaining()) {
			ByteBuffer tmp = ByteBuffer.wrap(new byte[] { bytes.get() });
			reader.setHeaderProvider(header);
			reader.consume(tmp);
		}
		assertTrue(reader.isDone());
		Envelope<Integer> env = reader.read();
		assertEquals(1, (int) env.getHeader()); 
		assertEquals(msg.getMsg(), ((Tell)env.getMessage()).getMsg());
	}
	
	@Test
	public void testMessageWithChecksumInSingleBuffer() throws Exception {
		Tell msg = Tell.newBuilder().setMsg("hello worlds").build();
		ByteBufferWriter<Void> writer = new ByteBufferWriter<Void>(types);
		writer.setChecksumProvider(checksum);
		writer.write(new Envelope<Void>(msg));
		ByteBuffer bytes = writer.getResult();
		int len = bytes.remaining();
		ByteBufferReader<Void> reader = new ByteBufferReader<Void>(types);
		reader.setChecksumProvider(checksum);
		int read = reader.consume(bytes);
		assertEquals(len, read);
		assertTrue(reader.isDone());
		Envelope<Void> env = reader.read();
		assertEquals(msg.getMsg(), ((Tell)env.getMessage()).getMsg());
	}
	
	@Test
	public void testMessageWithChecksumInMultipleBuffers() throws Exception {
		Tell msg = Tell.newBuilder().setMsg("hello worlds").build();
		ByteBufferWriter<Void> writer = new ByteBufferWriter<Void>(types);
		writer.setChecksumProvider(checksum);
		writer.write(new Envelope<Void>(msg));
		ByteBuffer bytes = writer.getResult();
		ByteBufferReader<Void> reader = new ByteBufferReader<Void>(types);
		while(bytes.hasRemaining()) {
			ByteBuffer tmp = ByteBuffer.wrap(new byte[] { bytes.get() });
			reader.setChecksumProvider(checksum);
			reader.consume(tmp);
		}
		assertTrue(reader.isDone());
		Envelope<Void> env = reader.read();
		assertEquals(msg.getMsg(), ((Tell)env.getMessage()).getMsg());
	}
}
