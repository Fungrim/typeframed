package org.typeframed.protobuf.parser;

import org.typeframed.api.DuplicateIdException;
import org.typeframed.api.MissingIdException;

public interface ErrorHandler {

	public void typeMissingId(String name) throws MissingIdException;

	public void duplicateId(int id, String oldName, String newName) throws DuplicateIdException;

}
