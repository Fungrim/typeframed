package org.typeframed.protobuf.parser;

import org.typeframed.protobuf.parser.node.MessageNode;


public class OptionInspector implements MessageInspector {

	private final String optionName;

	public OptionInspector(String optionName) {
		this.optionName = optionName;
	}
	
	@Override
	public int getId(MessageNode node) {
		String tmp = Utilities.getOption(node, optionName, false);
		return (tmp == null ? -1 : Integer.parseInt(tmp));
	}
}
