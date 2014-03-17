package org.typeframed.api;

import java.io.IOException;
import java.io.OutputStream;

public interface HeaderProvider<T> {
	
	public int getByteLength();

	public void write(T head, OutputStream out) throws IOException;

	public T parse(byte[] bytes);
	
}
