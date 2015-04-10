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
package org.typeframed.api;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * This is a header provider for fixed size, 32 bit integers.
 *
 * @author Lars J. Nilsson
 */
public class Int32HeaderProvider implements HeaderProvider<Integer> {

	@Override
	public Integer fromBytes(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		return buffer.getInt();
	}
	
	@Override
	public byte[] toBytes(Integer head) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(head);
		return buffer.array();
	}
}
