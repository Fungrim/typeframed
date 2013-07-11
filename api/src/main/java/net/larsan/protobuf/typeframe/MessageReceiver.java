package net.larsan.protobuf.typeframe;

public interface MessageReceiver<H> {

	public void receive(MessageEnvelope<H> envelope);
	
}
