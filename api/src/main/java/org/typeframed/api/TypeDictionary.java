package org.typeframed.api;

import com.google.protobuf.Message;

public interface TypeDictionary {
 
	public int getId(Message msg) throws UnknownMessageException;

	public Message.Builder getBuilderForId(int id) throws NoSuchTypeException;
	
}
