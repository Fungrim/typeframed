package net.larsan.protobuf.typeframe.parser;

import net.larsan.protobuf.typeframe.codegen.DuplicateIdException;
import net.larsan.protobuf.typeframe.codegen.MissingIdException;

public interface ErrorHandler {

	public void typeMissingId(String name) throws MissingIdException;

	public void duplicateId(int id, String oldName, String newName) throws DuplicateIdException;

}
