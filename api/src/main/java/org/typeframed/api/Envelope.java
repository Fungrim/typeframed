package org.typeframed.api;

import com.google.protobuf.Message;

public class Envelope<H> {

	private H header;
	private Message message;
	
	public Envelope() { }
	
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
