package org.typeframed.api;

import java.io.IOException;

public interface HeaderProvider<T> {

	public byte[] toBytes(T head) throws IOException;

	public T fromBytes(byte[] bytes);
	
}
