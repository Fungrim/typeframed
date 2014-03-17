package org.typeframed.api;

import java.net.InetAddress;

public abstract class ClientBuilder<H, C extends ClientConfig> {
	
	protected C config;
	
	protected ClientBuilder(C config) {
		this.config = config; 
	}
	
	public ClientBuilder<H, C> connectTo(InetAddress address, int port) {
		this.config.setAddress(address);
		this.config.setPort(port);
		return this;
	}
	
	public ClientBuilder<H, C> withHeader(HeaderProvider<H> provider) {
		this.config.setHeader(provider);
		return this;
	}
	
	public ClientBuilder<H, C> withChecksum(ChecksumProvider provider) {
		this.config.setChecksum(provider);
		return this;
	}
	
	public ClientBuilder<H, C> withDictionary(TypeDictionary dictionary) {
		this.config.setDictionary(dictionary);
		return this;
	}
	
	public Client build() {
		return doBuild(this.validateConfig());
	}		
	
	protected C validateConfig() {
		this.config.validate();
		return this.config;
	}

	protected abstract Client doBuild(C config);

}
