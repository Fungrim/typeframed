package org.typeframed.api.digest;

import java.security.MessageDigest;

public interface ChecksumProvider {

	public byte[] calculate(byte[] rawMessage);
	
	public MessageDigest toDigest();
	
}
