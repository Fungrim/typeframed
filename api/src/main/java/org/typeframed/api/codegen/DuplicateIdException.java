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
