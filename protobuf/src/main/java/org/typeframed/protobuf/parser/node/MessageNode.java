package org.typeframed.protobuf.parser.node;

import java.util.LinkedList;
import java.util.List;

import org.typeframed.protobuf.parser.EnumsHolder;

public class MessageNode implements FieldNode, EnumsHolder {

	private String name;
	private MessageBodyNode body = new MessageBodyNode();
	private final List<EnumNode> enums = new LinkedList<EnumNode>();
	private final List<OptionNode> options = new LinkedList<OptionNode>();
	
	public MessageNode(String name) {
		this.name = name;
	}
	
	public MessageNode() { }
	
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
	
	public MessageBodyNode getBody() {
		return body;
	}
	
	public void setBody(MessageBodyNode body) {
		this.body = body;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean setName(String name) {
		this.name = name;
		return true;
	}
}
