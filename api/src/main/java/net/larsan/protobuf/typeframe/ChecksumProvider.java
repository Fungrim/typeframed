package net.larsan.protobuf.typeframe;

import java.security.MessageDigest;

public interface ChecksumProvider {

	public int getByteLength();

	public byte[] calculate(byte[] rawMessage);
	
	public MessageDigest toDigest();
	
}
