package org.typeframed.codegen;

import java.io.IOException;

public interface CodeGenerator {

	public void generate() throws ClassNotFoundException, IOException;

	void setCodegenLogger(CodegenLogger logger);
	
}
