package org.typeframed.protobuf.parser.node;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.typeframed.protobuf.parser.EnumsHolder;
import org.typeframed.protobuf.parser.OptionsHolder;

public class RootNode implements FieldsNode, OptionsHolder, EnumsHolder {

	private String rootPackage;
	private final Deque<FieldNode> messages = new LinkedList<FieldNode>();
	private final List<OptionNode> options = new LinkedList<OptionNode>();
	private final List<EnumNode> enums = new LinkedList<EnumNode>();
	
	@Override
	public boolean addEnum(EnumNode node) {
		enums.add(node);
		return true;
	}
	
	public List<EnumNode> getEnums() {
		return enums;
	}
	
	@Override
	public boolean addOption(OptionNode option) {
		options.add(option);
		return true;
	}
	
	public List<OptionNode> getOptions() {
		return options;
	}
	
	public String getRootPackage() {
		return rootPackage;
	}
	
	public boolean setRootPackage(String rootPackage) {
		this.rootPackage = rootPackage;
		return true;
	}
	
	public Deque<FieldNode> getNodes() {
		return messages;
	}
}
