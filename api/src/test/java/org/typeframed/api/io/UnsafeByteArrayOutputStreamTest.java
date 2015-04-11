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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.util.Random;

import org.junit.Test;

@SuppressWarnings("resource")
public class UnsafeByteArrayOutputStreamTest {

	@Test
	public void testSimpleCopy() {
		UnsafeByteArrayOutputStream out = new UnsafeByteArrayOutputStream();
		out.write(1);
		out.write(2);
		out.write(3);
		byte[] arr = out.toByteArray();
		assertArrayEquals(new byte[] { 1, 2, 3 }, arr);
	}
	
	@Test
	public void testSimpleOverFlow() {
		UnsafeByteArrayOutputStream out = new UnsafeByteArrayOutputStream(12);
		byte[] bytes = new byte[32];
		new Random().nextBytes(bytes);
		out.write(bytes);
		byte[] arr = out.toByteArray();
		assertArrayEquals(bytes, arr);
	}
	
	@Test
	public void testSimpleAsByteBuffer() {
		UnsafeByteArrayOutputStream out = new UnsafeByteArrayOutputStream(12);
		out.write(1);
		out.write(2);
		out.write(3);
		ByteBuffer buff = out.asByteBuffer();
		assertEquals(3, buff.remaining());
		assertEquals(1, buff.get());
		assertEquals(2, buff.get());
		assertEquals(3, buff.get());
	}
}
