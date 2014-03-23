package org.typeframed.api.digest;

import java.security.MessageDigest;

import org.typeframed.api.ChecksumProvider;

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
