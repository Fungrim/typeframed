package net.larsan.protobuf.typeframe.digest;

import java.security.MessageDigest;

import net.larsan.protobuf.typeframe.ChecksumProvider;

public class CRC32ChecksumProvider implements ChecksumProvider {

	@Override
	public int getByteLength() {
		return 4;
	}

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
