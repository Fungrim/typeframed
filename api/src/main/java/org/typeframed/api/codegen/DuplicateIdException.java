/**
 * Copyright 2015 Lars J. Nilsson <contact@larsan.net>
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
package org.typeframed.api.codegen;

/**
 * An illegal state exception thrown when an ID is found on
 * several places during compilation.
 *
 * @author Lars J. Nilsson
 */
public class DuplicateIdException extends IllegalStateException {

	private static final long serialVersionUID = -4932682122836574350L;
	
	private final int id;
	private final String firstMessage;
	private final String secondMessage;
	
	/**
	 * @param id The ID that is duplicated
	 * @param firstMessage The first message type with the ID
	 * @param secondMessage The second message type with the ID
	 */
	public DuplicateIdException(int id, String firstMessage, String secondMessage) {
		super("Type ID " + id + " already defined; Messages: " + firstMessage + ", " + secondMessage);
		this.firstMessage = firstMessage;
		this.secondMessage = secondMessage;
		this.id = id;
	}
	
	/**
	 * @return The second message type with the ID
	 */
	public String getFirstMessage() {
		return firstMessage;
	}
	
	/**
	 * @return The first message type with the ID
	 */
	public String getSecondMessage() {
		return secondMessage;
	}
	
	/**
	 * @return The ID that is duplicated
	 */
	public int getId() {
		return id;
	}
}
