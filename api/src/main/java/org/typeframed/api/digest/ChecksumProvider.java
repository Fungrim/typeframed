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
package org.typeframed.api.digest;

import java.security.MessageDigest;

/**
 * A provider for a checksum. The provider works much
 * like the header providers, and is sing straight byte
 * arrays only.
 *
 * @author Lars J. Nilsson
 */
public interface ChecksumProvider {

	/**
	 * Calculate checksum from the ram message bytes.
	 * 
	 * @param rawMessage The message, never null
	 * @return The checksum, never null
	 */
	public byte[] calculate(byte[] rawMessage);
	
	/**
	 * @return A digest implementation of the checksum, never null
	 */
	public MessageDigest toDigest();
	
}
