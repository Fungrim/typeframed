package org.typeframed.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Int32HeaderProvider implements HeaderProvider<Integer> {

	@Override
	public Integer fromBytes(byte[] bytes) {
		try {
			return new DataInputStream(new ByteArrayInputStream(bytes)).readInt();
		} catch (IOException e) { 
			throw new IllegalStateException(e);
		}
	}
	
	@Override
	public byte[] toBytes(Integer head) throws IOException {
		ByteArrayOutputStream ba = new ByteArrayOutputStream(4);
		DataOutputStream dout = new DataOutputStream(ba);
		dout.writeInt(head);
		dout.flush();
		return ba.toByteArray();
	}
}
