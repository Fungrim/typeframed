package org.typeframed.protobuf.parser;

public class ParserError extends RuntimeException {
	
	public static class Location {
		public final int index;
		public Location(int index) {
			this.index = index;
		}
	}

	private static final long serialVersionUID = 7024966705801752310L;
	
	private Location[] indexes;

	public ParserError(Location[] indexes) {
		this.indexes = indexes;	
	}
	
	public Location[] getIndexes() {
		return indexes;
	}
}

