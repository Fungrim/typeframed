package org.typeframed.api.codegen;

/**
 * An illegal state exception thrown when a type in a
 * proto file is missing an ID. This is usually optional exception;
 *
 * @author Lars J. Nilsson
 */
public class MissingIdException extends IllegalStateException {

	private static final long serialVersionUID = 5159664232350329216L;

	public MissingIdException(String name) {
		super("Message " + name + " is missing an ID annotation");
	}
}
