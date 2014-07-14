package org.typeframed.api;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * This is a header provider for fixed size, 32 bit integers.
 *
 * @author Lars J. Nilsson
 */
public class Int32HeaderProvider implements HeaderProvider<Integer> {

	@Override
	public Integer fromBytes(byte[] bytes) {
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		return buffer.getInt();
	}
	
	@Override
	public byte[] toBytes(Integer head) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(head);
		return buffer.array();
	}
}
