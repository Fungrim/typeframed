package org.typeframed.api.codegen;

import org.typeframed.api.MessageEnvelope;
import org.typeframed.api.MessageReceiver;

import com.google.protobuf.Message;

public abstract class MessageSwitch<H> implements MessageReceiver<H> {

	@Override
	public void receive(MessageEnvelope<H> envelope) {
		doSwitch(envelope.getMessage());
	}
	
	protected abstract void doSwitch(Message msg);
	
}
