package net.larsan.protobuf.typeframe.codegen;

public interface CodeGeneratorFactory {

	public CodeGenerator create(Config config);
	
}
