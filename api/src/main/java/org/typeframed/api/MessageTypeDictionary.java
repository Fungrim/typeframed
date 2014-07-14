package org.typeframed.api;

import com.google.protobuf.Message;

/**
 * The message type dictionary is a lookup facility
 * that associates type ID with message builders. The type
 * dictionary will usually be auto-created by code generation
 * at compile time, but can also be created manually.
 *
 * @author Lars J. Nilsson
 */
public interface MessageTypeDictionary {
 
	/**
	 * Given a message, returns its ID. 
	 * 
	 * @param msg Message to get ID for, never null
	 * @return The ID of the message
	 * @throws UnknownMessageException If no message ID is found
	 */
	public int getId(Message msg) throws UnknownMessageException;

	/**
	 * Given an ID, return a build for the message type.
	 * 
	 * @param id ID of the message type
	 * @return A builder for the message, never null
	 * @throws NoSuchTypeException If no type for the ID can be found
	 */
	public Message.Builder getBuilderForId(int id) throws NoSuchTypeException;
	 
}
