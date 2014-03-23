package org.typeframed.api;

import com.google.protobuf.Message;

public class MessageFrame<H> {

	private int length;
	private int type;
	private H header;
	private Message message;
	private byte[] checksum;
	
	public int getLength() {
		return length;
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
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
	
	public byte[] getChecksum() {
		return checksum;
	}
	
	public void setChecksum(byte[] checksum) {
		this.checksum = checksum;
	}
}
