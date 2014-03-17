package org.typeframed.api;

public interface Client {

	public void connect() throws InterruptedException;
	
	public ClientSession getSession();
	
	public void disconnect() throws InterruptedException;
	
}
