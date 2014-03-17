package org.typeframed.api;

import java.util.concurrent.Future;

import com.google.protobuf.Message;

public interface MessageSender<H> {

	public Future<Boolean> send(H header, Message msg);
	
	public Future<Boolean> send(Message msg);
	
}
