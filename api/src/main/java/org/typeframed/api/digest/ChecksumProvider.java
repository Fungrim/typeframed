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
