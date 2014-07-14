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
package org.typeframed.api.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.typeframed.api.util.Varints;

import com.google.protobuf.CodedOutputStream;

public class VarintsTest {

	private final Random rand = new Random();
	
	@Test
	public void testRandomNumber() throws Exception {
		int[] source = new int[513];
		for (int i = 0; i < source.length; i++) {
			source[i] = rand.nextInt(50000);
		}
		ByteArrayOutputStream baout = new ByteArrayOutputStream();
		CodedOutputStream out = CodedOutputStream.newInstance(baout);
		for (int i = 0; i < source.length; i++) {
			out.writeRawVarint32(source[i]);
		}
		out.flush();
		byte[] arr = baout.toByteArray();
		ByteArrayInputStream in = new ByteArrayInputStream(arr);
		for (int i = 0; i < source.length; i++) {
			Assert.assertEquals(source[i], Varints.readRawVarint32(in));
		}
	}
}
