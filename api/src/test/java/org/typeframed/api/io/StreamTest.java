/**
 * Copyright 2014 Lars J. Nilsson <contact@larsan.net>
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
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.typeframed.api.Envelope;
import org.typeframed.api.HeaderProvider;
import org.typeframed.api.Int32HeaderProvider;
import org.typeframed.api.MessageTypeDictionary;
import org.typeframed.api.Msg.Tell;
import org.typeframed.api.NoSuchTypeException;
import org.typeframed.api.UnknownMessageException;
import org.typeframed.api.digest.CRC32ChecksumProvider;
import org.typeframed.api.digest.ChecksumProvider;

import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;

public class StreamTest {

	private MessageTypeDictionary types;
	private ChecksumProvider checksum;
	private HeaderProvider<Integer> header;
	
	@Before
	public void setup() throws Exception {
		header = new Int32HeaderProvider();
		checksum = new CRC32ChecksumProvider();
		types = new MessageTypeDictionary() {
			
			@Override
			public int getId(Message msg) throws UnknownMessageException {
				return 666;
			}
			
			@Override
			public Builder getBuilderForId(int id) throws NoSuchTypeException {
				if(id != 666) {
					throw new NoSuchTypeException(id);
				} else {
					return Tell.newBuilder();
				}
			}
		};
	}
	
	@Test
	public void testSimpleMultiple() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		StreamWriter<Void> writer = new StreamWriter<Void>(types, out);
		for (int i = 0; i < 5; i++) {
			Tell msg = Tell.newBuilder().setMsg(String.valueOf(i)).build();
			writer.write(new Envelope<Void>(null, msg));
		}
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		StreamReader<Void> reader = new StreamReader<Void>(types, in);
		for (int i = 0; i < 5; i++) {
			Envelope<Void> env = reader.read();
			Tell tell = (Tell) env.getMessage();
			Assert.assertEquals(i, Integer.parseInt(tell.getMsg()));
		}
	}
	
	@Test
	public void testMessageWithHeaderAndChecksum() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Tell msg = Tell.newBuilder().setMsg("hello worlds").build();
		StreamWriter<Integer> writer = new StreamWriter<Integer>(types, out);
		writer.setChecksumProvider(checksum);
		writer.setHeaderProvider(header);
		writer.write(new Envelope<Integer>(1, msg));
		byte[] bytes = out.toByteArray();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		StreamReader<Integer> reader = new StreamReader<Integer>(types, in);
		reader.setChecksumProvider(checksum);
		reader.setHeaderProvider(header);
		Envelope<Integer> env = reader.read();
		assertEquals(1, (int) env.getHeader()); 
		assertEquals(msg.getMsg(), ((Tell)env.getMessage()).getMsg());
	}
	
	@Test
	public void testMessageWithHeader() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Tell msg = Tell.newBuilder().setMsg("hello worlds").build();
		StreamWriter<Integer> writer = new StreamWriter<Integer>(types, out);
		writer.setHeaderProvider(header);
		writer.write(new Envelope<Integer>(1, msg));
		byte[] bytes = out.toByteArray();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		StreamReader<Integer> reader = new StreamReader<Integer>(types, in);
		reader.setChecksumProvider(checksum);
		reader.setHeaderProvider(header);
		Envelope<Integer> env = reader.read();
		assertEquals(1, (int) env.getHeader()); 
		assertEquals(msg.getMsg(), ((Tell)env.getMessage()).getMsg());
	}
	
	@Test
	public void testMessage() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Tell msg = Tell.newBuilder().setMsg("hello worlds").build();
		StreamWriter<Integer> writer = new StreamWriter<Integer>(types, out);
		writer.write(new Envelope<Integer>(1, msg));
		byte[] bytes = out.toByteArray();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		StreamReader<Integer> reader = new StreamReader<Integer>(types, in);
		reader.setChecksumProvider(checksum);
		reader.setHeaderProvider(header);
		Envelope<Integer> env = reader.read();
		assertNull(env.getHeader());
		assertEquals(msg.getMsg(), ((Tell)env.getMessage()).getMsg());
	}
	
	@Test(expected=IllegalStateException.class)
	public void testMessageMissingChecksumProvider() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Tell msg = Tell.newBuilder().setMsg("hello worlds").build();
		StreamWriter<Integer> writer = new StreamWriter<Integer>(types, out);
		writer.setChecksumProvider(checksum);
		writer.setHeaderProvider(header);
		writer.write(new Envelope<Integer>(1, msg));
		byte[] bytes = out.toByteArray();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		StreamReader<Integer> reader = new StreamReader<Integer>(types, in);
		// reader.setChecksumProvider(checksum);
		reader.setHeaderProvider(header);
		reader.read();
	}
	
	@Test(expected=IllegalStateException.class)
	public void testMessageMissingHeaderProvider() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Tell msg = Tell.newBuilder().setMsg("hello worlds").build();
		StreamWriter<Integer> writer = new StreamWriter<Integer>(types, out);
		writer.setChecksumProvider(checksum);
		writer.setHeaderProvider(header);
		writer.write(new Envelope<Integer>(1, msg));
		byte[] bytes = out.toByteArray();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		StreamReader<Integer> reader = new StreamReader<Integer>(types, in);
		reader.setChecksumProvider(checksum);
		// reader.setHeaderProvider(header);
		reader.read();
	}
}
