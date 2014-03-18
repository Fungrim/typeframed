package org.typeframed.api.server;

public interface Server {

	public void start() throws InterruptedException;
	
	public void stop() throws InterruptedException;
	
}
