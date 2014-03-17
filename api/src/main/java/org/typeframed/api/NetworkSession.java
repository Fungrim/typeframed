package org.typeframed.api;

import java.net.InetSocketAddress;

public interface NetworkSession {

	public InetSocketAddress getRemoteAddress();
	
	public <H> void setReceiver(MessageReceiver<H> receiver);
	
	public <H> MessageSender<H> getSender();
	
}
