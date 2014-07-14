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
