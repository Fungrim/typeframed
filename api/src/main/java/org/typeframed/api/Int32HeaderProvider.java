package org.typeframed.api;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class Int32HeaderProvider implements HeaderProvider<Integer> {

	@Override
	public int getByteLength() {
		return 4;
	}

	@Override
	public Integer parse(byte[] bytes) {
		try {
			return new DataInputStream(new ByteArrayInputStream(bytes)).readInt();
		} catch (IOException e) { 
			throw new IllegalStateException(e);
		}
	}
	
	@Override
	public void write(Integer head, OutputStream out) throws IOException {
		DataOutputStream dout = new DataOutputStream(out);
		dout.writeInt(head);
		dout.flush();
	}
}
