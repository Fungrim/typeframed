package org.typeframed.api;

public interface MessageReceiver<H> {

	public void receive(MessageEnvelope<H> envelope);
	
}
