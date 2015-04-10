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
package org.typeframed.api.util;

import java.security.MessageDigest;

/**
 * This is the digest version of /dev/null, it does nothing.
 *
 * @author Lars J. Nilsson
 */
public class NullDigest extends MessageDigest {

	public static final MessageDigest INSTANCE = new NullDigest();
	
	private NullDigest() {
		super("NULL");
	}

	@Override
	protected byte[] engineDigest() {
		return null;
	}
	
	@Override
	protected void engineReset() { }
	
	@Override
	protected void engineUpdate(byte input) { }
	
	@Override
	protected void engineUpdate(byte[] input, int offset, int len) { }
	
	@Override
	public byte[] digest() {
		return null;
	}
}
