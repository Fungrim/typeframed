package org.typeframed.codegen;

public class GoConfig extends Config {

	private String[] extraImports = new String[0];
	
	public String[] getExtraImports() {
		return extraImports;
	}
	
	public void setExtraImports(String[] extraImports) {
		this.extraImports = extraImports;
	}
}
