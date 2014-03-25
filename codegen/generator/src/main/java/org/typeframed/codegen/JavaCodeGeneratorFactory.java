package org.typeframed.codegen;

public class JavaCodeGeneratorFactory implements CodeGeneratorFactory {

	@Override
	public CodeGenerator create(Config config) {
		return new JavaCodeGenerator(config);
	}
}
