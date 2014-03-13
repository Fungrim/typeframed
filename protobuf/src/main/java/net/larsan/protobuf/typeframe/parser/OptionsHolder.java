package net.larsan.protobuf.typeframe.parser;

import java.util.List;

import net.larsan.protobuf.typeframe.parser.node.OptionNode;

public interface OptionsHolder {

	public boolean addOption(OptionNode option);
	
	public List<OptionNode> getOptions();
	
}
