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
 * The envelope is a simple bean encapsulating
 * the optional header field and the protobuf message.
 *
 * @author Lars J. Nilsson
 * @param <H> Envelope header type
 */
public class Envelope<H> {

	private H header;
	private Message message;
	
	public Envelope() { }
	
	/**
	 * @param header Header to use, may be null
	 * @param message Message to use, should not be null
	 */
	public Envelope(H header, Message message) {
		this.header = header;
		this.message = message;
	}

	public H getHeader() {
		return header;
	}
	
	public void setHeader(H header) {
		this.header = header;
	}
	
	public Message getMessage() {
		return message;
	}
	
	public void setMessage(Message message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Envelope [header=" + header + ", message=" + message + "]";
	}
}
