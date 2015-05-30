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
package org.typeframed.api.digest;

import java.security.MessageDigest;
import java.util.zip.CRC32;

/**
 * A digest version of the CRC32 checksum provider.
 *
 * @author Lars J. Nilsson
 */
public class CRC32MessageDigest extends MessageDigest {

	private CRC32 crc32;

	public CRC32MessageDigest() {
		super("CRC32");
		crc32 = new CRC32();
	}

	@Override
	protected void engineUpdate(byte input) {
		crc32.update(input);
	}

	@Override
	protected void engineUpdate(byte[] input, int offset, int len) {
		crc32.update(input, offset, len);
	}

	@Override
	protected byte[] engineDigest() {
		long val = crc32.getValue();
		byte[] arr = new byte[8];
		arr[0] = (byte)(val >>> 56);
		arr[1] = (byte)(val >>> 48);
		arr[2] = (byte)(val >>> 40);
		arr[3] = (byte)(val >>> 32);
		arr[4] = (byte)(val >>> 24);
		arr[5] = (byte)(val >>> 16);
		arr[6] = (byte)(val >>>  8);
		arr[7] = (byte)(val >>>  0);
		return arr;
	}

	@Override
	protected void engineReset() {
		crc32.reset();
	}
}
