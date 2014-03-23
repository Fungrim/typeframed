package org.typeframed.api;

public class DuplicateIdException extends IllegalStateException {

	private static final long serialVersionUID = 2323188712616190444L;
	
	private final int id;
	private final String oldName;
	private final String newName;
	
	public DuplicateIdException(int id, String oldName, String newName) {
		super("ID " + id + " already mapped to type: " + oldName);
		this.id = id;
		this.oldName = oldName;
		this.newName = newName;
	}
	
	public int getId() {
		return id;
	}
	
	public String getNewName() {
		return newName;
	}
	
	public String getOldName() {
		return oldName;
	}
}
