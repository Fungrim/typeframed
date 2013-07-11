package net.larsan.protobuf.typeframe;

import com.google.protobuf.Message;

public class MessageEnvelope<H> {
	
	public static final <H> MessageEnvelope<H> wrap(Message msg) {
		return new MessageEnvelope<H>((H) null, msg);
	}
	
	public static final <H> MessageEnvelope<H> wrap(H header, Message msg) {
		return new MessageEnvelope<H>(header, msg);
	}

	private final H header;
	private final Message message;
	
	private MessageEnvelope(H header, Message msg) {
		this.header = header;
		message = msg;
	}
	
	public H getHeader() {
		return header;
	}
	
	public Message getMessage() {
		return message;
	}
}
