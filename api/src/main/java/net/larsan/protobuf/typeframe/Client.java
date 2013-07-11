package net.larsan.protobuf.typeframe;

public interface Client {

	public void connect() throws InterruptedException;
	
	public ClientSession getSession();
	
	public void disconnect() throws InterruptedException;
	
}
