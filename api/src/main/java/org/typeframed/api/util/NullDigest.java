package org.typeframed.api.util;

import java.security.MessageDigest;

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
