package org.typeframed.api;

import java.io.IOException;

import com.google.protobuf.Message;

public interface MessageReader {

	public Message read() throws IOException;
	
	// public Message read(long timeout, TimeUnit unit) throws IOException;
	
	// public Message poll() throws IOException;
	
}
