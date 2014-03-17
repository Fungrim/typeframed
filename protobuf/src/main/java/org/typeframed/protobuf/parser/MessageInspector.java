package org.typeframed.protobuf.parser;

import org.typeframed.protobuf.parser.node.MessageNode;

public interface MessageInspector {

	public int getId(MessageNode node);
	
}
