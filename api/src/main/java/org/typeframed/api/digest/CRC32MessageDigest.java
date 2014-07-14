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
		byte[] arr = new byte[4];
		arr[0] = (byte)(val >>> 24);
		arr[1] = (byte)(val >>> 16);
		arr[2] = (byte)(val >>>  8);
		arr[3] = (byte)(val >>>  0);
		return arr;
	}

	@Override
	protected void engineReset() {
		crc32.reset();
	}
}
