package org.typeframed.api.codegen;

public class MissingIdException extends IllegalStateException {

	private static final long serialVersionUID = -6599536467513488354L;
	
	private final String type;

	public MissingIdException(String type) {
		super("Message " + type + " is missing a type ID");
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
}
