package org.typeframed.protobuf.parser;

import org.apache.log4j.Logger;
import org.typeframed.api.codegen.DuplicateIdException;

public class StandardErrorHandler implements ErrorHandler {

	protected final Logger log;

	public StandardErrorHandler(Logger log) {
		this.log = log;
	}

	@Override
	public void typeMissingId(String name) {
		log.warn("Did not find any type ID in message '" + name + "'");	
	}
	
	@Override
	public void duplicateId(int id, String oldName, String newName) throws DuplicateIdException {
		throw new DuplicateIdException(id, oldName, newName);	
	}
}
