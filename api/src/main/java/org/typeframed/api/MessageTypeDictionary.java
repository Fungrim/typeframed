/**
 * Copyright 2014 Lars J. Nilsson <contact@larsan.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
