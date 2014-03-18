package org.typeframed.codegen;

import org.typeframed.api.CodeGenerator;

public interface CodeGeneratorFactory {

	public CodeGenerator create(Config config);
	
}
