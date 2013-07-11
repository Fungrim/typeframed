package net.larsan.protobuf.typeframe;

import com.google.protobuf.Message;

public class UnknownMessageException extends IllegalArgumentException {

	private static final long serialVersionUID = -378764320880222223L;
	
	private Class<? extends Message> messageClass;

	public UnknownMessageException(Class<? extends Message> messageClass) {
		this.messageClass = messageClass;
	}
	
	public Class<? extends Message> getMessageClass() {
		return messageClass;
	}
}
