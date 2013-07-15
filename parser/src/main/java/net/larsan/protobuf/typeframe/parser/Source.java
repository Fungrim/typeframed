package net.larsan.protobuf.typeframe.parser;

import java.io.IOException;
import java.io.Reader;

public interface Source {

	public String getName();
	
	public Reader getReader() throws IOException;
	
}
