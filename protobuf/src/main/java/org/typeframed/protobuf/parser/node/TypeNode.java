/**
 * Copyright 2015 Lars J. Nilsson <contact@larsan.net>
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

import org.typeframed.protobuf.parser.FieldRule;

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
