package org.typeframed.protobuf.codegen;

import java.io.IOException;

public interface CodeGenerator {

	public void generate() throws ClassNotFoundException, IOException;
	
}
