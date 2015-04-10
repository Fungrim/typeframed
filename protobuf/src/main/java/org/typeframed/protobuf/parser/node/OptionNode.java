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

public class OptionNode implements NamedNode {

	private String name;
	private String value;
	private boolean isCustom;
	
	public boolean setValue(String value) {
		this.value = value;
		return true;
	}
	
	public boolean isCustom() {
		return isCustom;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public boolean setName(String name) {
		if(name.startsWith("(") && name.endsWith(")")) {
			// HACK: this is a custom option...
			isCustom = true;
			this.name = name.substring(1, name.length() - 1);
		} else {
			this.name = name;
			isCustom = false;
		}
		return true;
	}
}
