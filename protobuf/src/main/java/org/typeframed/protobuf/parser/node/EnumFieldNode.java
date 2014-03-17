package org.typeframed.protobuf.parser.node;

import java.util.LinkedList;
import java.util.List;

public class EnumFieldNode implements FieldNode {

	private String name;
	private int ordinal;
	
	private final List<OptionNode> options = new LinkedList<OptionNode>();

	public EnumFieldNode(String name) {
		this.name = name;
	}
	
	public EnumFieldNode() { }

	public int getOrdinal() {
		return ordinal;
	}
	
	public boolean setOrdinal(int ordinal) {
		this.ordinal = ordinal;
		return true;
	}
	
	@Override
	public boolean addOption(OptionNode option) {
		options.add(option);
		return true;
	}
	
	public List<OptionNode> getOptions() {
		return options;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean setName(String name) {
		this.name = name;
		return true;
	}
}
