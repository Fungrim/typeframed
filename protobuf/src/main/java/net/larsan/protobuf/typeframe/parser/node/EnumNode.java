package net.larsan.protobuf.typeframe.parser.node;

import java.util.LinkedList;
import java.util.List;

import net.larsan.protobuf.typeframe.parser.OptionsHolder;

public class EnumNode implements NamedNode, OptionsHolder {

	private String name;
	private final List<EnumFieldNode> fields = new LinkedList<EnumFieldNode>();
	private final List<OptionNode> options = new LinkedList<OptionNode>();
	
	public EnumNode() { }
	
	public EnumNode(String name) {
		this.name = name;
	}
	
	@Override
	public boolean addOption(OptionNode option) {
		options.add(option);
		return true;
	}
	
	public List<OptionNode> getOptions() {
		return options;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public boolean setName(String name) {
		this.name = name;
		return true;
	}
	
	public List<EnumFieldNode> getFields() {
		return fields;
	}
}
