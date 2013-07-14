package net.larsan.protobuf.typeframe.codegen;

public enum Language {

	JAVA(new JavaCodeGeneratorFactory());

	private final CodeGeneratorFactory factory;
	
	private Language(CodeGeneratorFactory factory) {
		this.factory = factory;
	}
	
	public CodeGeneratorFactory getCodeGeneratorFactory() {
		return factory;
	}
}
