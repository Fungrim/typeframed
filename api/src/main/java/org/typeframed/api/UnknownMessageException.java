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
