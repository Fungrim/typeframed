package net.larsan.protobuf.typeframe;

public class ServerConfig extends BuilderConfig {

	private ServerHandler handler;
	
	@Override
	public ServerConfig validate() {
		if(handler == null) {
			throw new IllegalStateException("Misssing server handler!");
		}
		super.validate();
		return this;
	}
	
	public void setHandler(ServerHandler handler) {
		this.handler = handler;
	}
	
	public ServerHandler getHandler() {
		return handler;
	}
}
