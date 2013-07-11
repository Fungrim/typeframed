package net.larsan.protobuf.typeframe.parser.node;

public class OptionNode implements NamedNode {

	private String name;
	private String value;
	private boolean isCustom;
	
	public boolean setValue(String value) {
		this.value = value;
		return true;
	}
	
	public boolean isCustom() {
		return isCustom;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public boolean setName(String name) {
		if(name.startsWith("(") && name.endsWith(")")) {
			// HACK: this is a custom option...
			isCustom = true;
			this.name = name.substring(1, name.length() - 1);
		} else {
			this.name = name;
			isCustom = false;
		}
		return true;
	}
}
