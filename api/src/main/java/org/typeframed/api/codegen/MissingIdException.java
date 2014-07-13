package org.typeframed.api.codegen;

public class MissingIdException extends IllegalStateException {

	private static final long serialVersionUID = 5159664232350329216L;

	public MissingIdException(String name) {
		super("Message " + name + " is missing an ID annotation");
	}
}
