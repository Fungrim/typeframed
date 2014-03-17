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
