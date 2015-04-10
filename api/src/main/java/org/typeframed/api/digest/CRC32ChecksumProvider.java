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

/**
 * This is the default CRC32 checksum provider.
 *
 * @author Lars J. Nilsson
 */
public class CRC32ChecksumProvider implements ChecksumProvider {

	@Override
	public byte[] calculate(byte[] rawMessage) {
		MessageDigest dig = new CRC32MessageDigest();
		return dig.digest(rawMessage);
	}
	
	@Override
	public MessageDigest toDigest() {
		return new CRC32MessageDigest();
	}
}
