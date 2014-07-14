package org.typeframed.protobuf.parser;

import org.typeframed.api.codegen.DuplicateIdException;

public class StandardErrorHandler implements ErrorHandler {

	private ParserLogger log;

	public StandardErrorHandler(ParserLogger log) {
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
