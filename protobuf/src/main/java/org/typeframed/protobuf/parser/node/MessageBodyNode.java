package org.typeframed.protobuf.parser.node;

import java.util.Deque;
import java.util.LinkedList;

public class MessageBodyNode implements FieldsNode {

	private final Deque<FieldNode> nodes = new LinkedList<FieldNode>();
	
	@Override
	public Deque<FieldNode> getNodes() {
		return nodes;
	}
}
