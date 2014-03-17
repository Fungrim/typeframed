package net.larsan.protobuf.typeframe.codegen;

import org.typeframed.api.codegen.CodeGenerator;

public interface CodeGeneratorFactory {

	public CodeGenerator create(Config config);
	
}
