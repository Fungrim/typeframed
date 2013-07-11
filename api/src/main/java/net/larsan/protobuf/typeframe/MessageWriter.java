package net.larsan.protobuf.typeframe;

import java.io.IOException;

import com.google.protobuf.Message;

public interface MessageWriter {

	public void write(Message msg) throws IOException;
	
}
