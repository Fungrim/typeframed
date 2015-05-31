package org.typeframed.protobuf.parser;

public enum Option {

	JAVA_PACKAGE("java_package"),
	JAVA_OUTER_CLASSNAME("java_outer_classname"),
	GO_PACKAGE("go_package");
	
	private String optionName;

	private Option(String optionName) {
		this.optionName = optionName;
	}
	
	public String getOptionName() {
		return optionName;
	}
}
