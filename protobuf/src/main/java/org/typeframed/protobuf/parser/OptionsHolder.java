package org.typeframed.protobuf.parser;

import java.util.List;

import org.typeframed.protobuf.parser.node.OptionNode;

public interface OptionsHolder {

	public boolean addOption(OptionNode option);
	
	public List<OptionNode> getOptions();
	
}
