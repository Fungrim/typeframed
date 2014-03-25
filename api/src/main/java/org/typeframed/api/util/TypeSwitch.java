package org.typeframed.api.util;

import com.google.protobuf.Message;

public abstract class TypeSwitch {
	
	public static interface Target { }

	protected abstract void forward(Message msg);
	
}
