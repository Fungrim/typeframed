package org.typeframed.api;

import java.net.InetAddress;

public abstract class ServerBuilder<H, C extends ServerConfig> {

	protected C config;
	
	protected ServerBuilder(C config) {
		this.config = config; 
	}
	
	public ServerBuilder<H, C> bindTo(InetAddress address, int port) {
		this.config.setAddress(address);
		this.config.setPort(port);
		return this;
	}
	
	public ServerBuilder<H, C> withHeader(HeaderProvider<H> provider) {
		this.config.setHeader(provider);
		return this;
	}
	
	public ServerBuilder<H, C> withChecksum(ChecksumProvider provider) {
		this.config.setChecksum(provider);
		return this;
	}
	
	public ServerBuilder<H, C> withDictionary(TypeDictionary dictionary) {
		this.config.setDictionary(dictionary);
		return this;
	}
	
	public ServerBuilder<H, C> withHandler(ServerHandler handler) {
		this.config.setHandler(handler);
		return this;
	}
	
	public Server build() {
		return doBuild(this.validateConfig());
	}		
	
	protected C validateConfig() {
		this.config.validate();
		return this.config;
	}

	protected abstract Server doBuild(C config);
	
}
