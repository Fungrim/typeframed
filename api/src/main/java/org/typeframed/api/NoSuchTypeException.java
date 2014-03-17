package org.typeframed.api;

public class NoSuchTypeException extends IllegalArgumentException {

	private static final long serialVersionUID = 8663537933223095321L;

	private int typeId;

	public NoSuchTypeException(int typeId) {
		this.typeId = typeId;
	}
	
	public int getTypeId() {
		return typeId;
	}
}
