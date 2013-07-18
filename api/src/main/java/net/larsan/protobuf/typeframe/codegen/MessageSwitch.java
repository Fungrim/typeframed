package net.larsan.protobuf.typeframe.codegen;

import com.google.protobuf.Message;

public interface MessageSwitch {

	public void doSwitch(Message msg);
	
}
