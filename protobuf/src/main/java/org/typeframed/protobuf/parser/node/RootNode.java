/**
 * Copyright 2014 Lars J. Nilsson <contact@larsan.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
