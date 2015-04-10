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
package org.typeframed.api;

import com.google.protobuf.Message;

/**
 * This is an illegal argument exception for when the system
 * cannot find an ID for a given message type.
 *
 * @author Lars J. Nilsson
 */
public class UnknownMessageException extends IllegalArgumentException {

	private static final long serialVersionUID = -378764320880222223L;
	
	private Class<? extends Message> messageClass;

	/**
	 * @param messageClass Type of the message which there was no ID for
	 */
	public UnknownMessageException(Class<? extends Message> messageClass) {
		this.messageClass = messageClass;
	}
	
	/**
	 * @return Type of the message which there was no ID for
	 */
	public Class<? extends Message> getMessageClass() {
		return messageClass;
	}
}
