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
package org.typeframed.protobuf.parser;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class MessageDescriptor {
	
	private int typeId;
	private String javaClassName;
	private String javaCanonicalClassName;
	private String nodeName;

	public MessageDescriptor(int typeId, String nodeName, String javaClassName) {
		this.typeId = typeId;
		this.javaClassName = javaClassName;
		this.javaCanonicalClassName = this.javaClassName.replace('$', '.');
		this.nodeName = nodeName;
	}
	
	public int getTypeId() {
		return typeId;
	}

	public String getJavaCanonicalClassName() {
		return javaCanonicalClassName;
	}
	
	public String getJavaClassName() {
		return javaClassName;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
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
