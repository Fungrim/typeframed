package org.typeframed.codegen;

import org.typeframed.api.codegen.CodeGenerator;

public interface CodeGeneratorFactory {

	public CodeGenerator create(Config config);
	
}
