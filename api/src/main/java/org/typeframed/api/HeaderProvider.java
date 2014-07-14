package org.typeframed.api;

import java.io.IOException;

/**
 * The header provider is used to parse and read header
 * bytes. Currently it works on byte arrays only.
 *
 * @author Lars J. Nilsson
 * @param <T> The header type
 */
public interface HeaderProvider<T> {

	public byte[] toBytes(T head) throws IOException;

	public T fromBytes(byte[] bytes);
	
}
