package org.typeframed.api;

import com.google.protobuf.Message;

public interface MessageTypeDictionary {
 
	public int getId(Message msg) throws UnknownMessageException;

	public Message.Builder getBuilderForId(int id) throws NoSuchTypeException;
	 
}
