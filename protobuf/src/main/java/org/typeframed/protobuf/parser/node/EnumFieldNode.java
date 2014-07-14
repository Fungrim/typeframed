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

import java.util.LinkedList;
import java.util.List;

public class EnumFieldNode implements FieldNode {

	private String name;
	private int ordinal;
	
	private final List<OptionNode> options = new LinkedList<OptionNode>();

	public EnumFieldNode(String name) {
		this.name = name;
	}
	
	public EnumFieldNode() { }

	public int getOrdinal() {
		return ordinal;
	}
	
	public boolean setOrdinal(int ordinal) {
		this.ordinal = ordinal;
		return true;
	}
	
	@Override
	public boolean addOption(OptionNode option) {
		options.add(option);
		return true;
	}
	
	public List<OptionNode> getOptions() {
		return options;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean setName(String name) {
		this.name = name;
		return true;
	}
}
