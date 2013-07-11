package net.larsan.protobuf.typeframe;

import com.google.protobuf.Message;

public interface TypeDictionary {

	public int getId(Message msg) throws UnknownMessageException;

	public Message.Builder getBuilderForId(int id) throws NoSuchTypeException;
	
}
