package org.typeframed.codegen;

public interface CodeGeneratorFactory {

	public CodeGenerator create(Config config);
	
}
