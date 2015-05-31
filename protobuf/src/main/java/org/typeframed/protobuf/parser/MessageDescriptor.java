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
package org.typeframed.protobuf.parser;

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MessageDescriptor {
	
	private int typeId;
	private String nodeName;
	private MessageDescriptor parent;
	private Map<Option, String> options;

	public MessageDescriptor(int typeId, String nodeName, Map<Option, String> options, MessageDescriptor parent) {
		this.typeId = typeId;
		this.nodeName = nodeName;
		this.options = options;
		this.parent = parent;
	}
	
	public int getTypeId() {
		return typeId;
	}

	public String getJavaClassName() {
		if(parent != null) {
			return parent.getJavaClassName() + "$" + nodeName;
		} else {
			String pack = "";
			if(options.containsKey(Option.JAVA_PACKAGE)) {
				pack = options.get(Option.JAVA_PACKAGE) + ".";
			}
			if(options.containsKey(Option.JAVA_OUTER_CLASSNAME)) {
				pack += options.get(Option.JAVA_OUTER_CLASSNAME);
				return pack + "$" + nodeName;
			} else {
				return pack + "." + nodeName;
			}
		}
	}
	
	public String getCanonicalJavaClassName() {
		return getJavaClassName().replace('$', '.');
	}
	
	@Override
	public String toString() {
		return (parent != null ? parent.toString() + "." : "") + nodeName;
	}
	
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public String getNodeName() {
		return nodeName;
	}
}
