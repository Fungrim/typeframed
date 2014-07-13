package org.typeframed.api.codegen;

public class DuplicateIdException extends IllegalStateException {

	private static final long serialVersionUID = -4932682122836574350L;
	
	private final int id;
	private final String firstMessage;
	private final String secondMessage;
	
	public DuplicateIdException(int id, String firstMessage, String secondMessage) {
		super("Type ID " + id + " already defined; Messages: " + firstMessage + ", " + secondMessage);
		this.firstMessage = firstMessage;
		this.secondMessage = secondMessage;
		this.id = id;
	}
	
	public String getFirstMessage() {
		return firstMessage;
	}
	
	public String getSecondMessage() {
		return secondMessage;
	}
	
	public int getId() {
		return id;
	}
}
