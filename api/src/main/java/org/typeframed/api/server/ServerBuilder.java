package org.typeframed.api.server;

import java.net.InetAddress;

import org.typeframed.api.ChecksumProvider;
import org.typeframed.api.HeaderParser;
import org.typeframed.api.TypeDictionary;

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
	
	public ServerBuilder<H, C> withHeader(HeaderParser<H> provider) {
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
