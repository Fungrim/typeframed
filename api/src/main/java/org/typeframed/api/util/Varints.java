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

import java.io.IOException;
import java.io.InputStream;

import com.google.protobuf.InvalidProtocolBufferException;

public class Varints {

	private Varints() {
	}

	/*
	 * Copied from protobuf: Copyright 2008 Google Inc. All rights reserved.
	 * 
	 * Used here in order to read varints without filling up a buffer. Otherwise
	 * we won't be able to calculate checksum properly.
	 */
	public static int readRawVarint32(InputStream in) throws IOException {
		byte tmp = (byte) in.read();
		if (tmp >= 0) {
			return tmp;
		}
		int result = tmp & 0x7f;
		if ((tmp = (byte) in.read()) >= 0) {
			result |= tmp << 7;
		} else {
			result |= (tmp & 0x7f) << 7;
			if ((tmp = (byte) in.read()) >= 0) {
				result |= tmp << 14;
			} else {
				result |= (tmp & 0x7f) << 14;
				if ((tmp = (byte) in.read()) >= 0) {
					result |= tmp << 21;
				} else {
					result |= (tmp & 0x7f) << 21;
					result |= (tmp = (byte) in.read()) << 28;
					if (tmp < 0) {
						// Discard upper 32 bits.
						for (int i = 0; i < 5; i++) {
							if ((byte) in.read() >= 0) {
								return result;
							}
						}
						throw new InvalidProtocolBufferException("CodedInputStream encountered a malformed varint.");
					}
				}
			}
		}
		return result;
	}
}
