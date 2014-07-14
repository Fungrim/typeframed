package org.typeframed.api;

/**
 * This is an illegal argument exception to indicate that
 * the system cannot find a type for a given ID.
 * 
 * @author Lars J. Nilsson
 */
public class NoSuchTypeException extends IllegalArgumentException {

	private static final long serialVersionUID = 8663537933223095321L;

	private int typeId;

	/**
	 * @param typeId The type ID that had no match
	 */
	public NoSuchTypeException(int typeId) {
		this.typeId = typeId;
	}
	
	/**
	 * @return The type ID that had no match
	 */
	public int getTypeId() {
		return typeId;
	}
}
