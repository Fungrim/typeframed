package net.larsan.protobuf.typeframe.parser.node;

import java.util.LinkedList;
import java.util.List;

import net.larsan.protobuf.typeframe.parser.FieldRule;

public class TypeNode implements FieldNode {

	private FieldRule rule;
	private String name;
	private String type;
	private int tag;
	
	private final List<OptionNode> options = new LinkedList<OptionNode>();
	
	public FieldRule getRule() {
		return rule;
	}
	
	@Override
	public boolean addOption(OptionNode option) {
		options.add(option);
		return true;
	}
	
	public List<OptionNode> getOptions() {
		return options;
	}
	
	public boolean setRule(FieldRule rule) {
		this.rule = rule;
		return true;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean setName(String name) {
		this.name = name;
		return true;
	}
	
	public String getType() {
		return type;
	}
	
	public boolean setType(String type) {
		this.type = type;
		return true;
	}
	
	public int getTag() {
		return tag;
	}
	
	public boolean setTag(int tag) {
		this.tag = tag;
		return true;
	}

	@Override
	public String toString() {
		return "TypeNode [rule=" + rule + ", name=" + name + ", type=" + type
				+ ", tag=" + tag + "]";
	}
}
