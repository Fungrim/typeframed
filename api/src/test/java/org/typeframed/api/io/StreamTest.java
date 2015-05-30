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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import junit.framework.Assert;

import org.junit.Test;
import org.typeframed.api.Envelope;
import org.typeframed.api.Msg.Tell;

public class StreamTest extends BaseParseTest {
	
	@Test
	public void testSimple() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Tell msg = Tell.newBuilder().setMsg("Hello World!").build();
		StreamWriter<Integer> writer = new StreamWriter<Integer>(types, out);
		//writer.setChecksumProvider(checksum);
		//writer.setHeaderProvider(header);
		writer.write(new Envelope<Integer>(1, msg));
		byte[] bytes = out.toByteArray();
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		StreamReader<Integer> reader = new StreamReader<Integer>(types, in);
		//reader.setChecksumProvider(checksum);
		//reader.setHeaderProvider(header);
		Envelope<Integer> env = reader.read();
		assertEquals(msg.getMsg(), ((Tell)env.getMessage()).getMsg());
	}
	
	@Test
	public void testSimpleWithChecksum() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Tell msg = Tell.newBuilder().setMsg("Hello World!").build();
		StreamWriter<Integer> writer = new StreamWriter<Integer>(types, out);
		writer.setChecksumProvider(checksum);
		//writer.setHeaderProvider(header);
		writer.write(new Envelope<Integer>(1, msg));
		byte[] bytes = out.toByteArray();
		System.out.print("K: ");
//		for (int i = 0; i < bytes.length; i++) {
//			System.out.print("" + (bytes[i] & 0xFF));
//			if (i + 1 < bytes.length) {
//				System.out.print(", ");
//			}
//		}
//		System.out.println("");
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		StreamReader<Integer> reader = new StreamReader<Integer>(types, in);
		reader.setChecksumProvider(checksum);
		//reader.setHeaderProvider(header);
		Envelope<Integer> env = reader.read();
		assertEquals(msg.getMsg(), ((Tell)env.getMessage()).getMsg());
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
		Tell msg = Tell.newBuilder().setMsg("Hello World!").build();
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
