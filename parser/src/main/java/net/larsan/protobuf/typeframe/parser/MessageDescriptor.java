package net.larsan.protobuf.typeframe.parser;

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
