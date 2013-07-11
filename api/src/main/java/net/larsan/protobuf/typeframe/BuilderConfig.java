package net.larsan.protobuf.typeframe;

import java.net.InetAddress;

public class BuilderConfig {
	
	private ChecksumProvider checksum;
	private TypeDictionary dictionary;
	private HeaderProvider<?> header;
	private InetAddress address;
	private int port;
	
	public BuilderConfig validate() {
		if(dictionary == null) {
			throw new IllegalStateException("Misssing dictionary!");
		}
		return this;
	}

	public ChecksumProvider getChecksum() {
		return checksum;
	}

	public void setChecksum(ChecksumProvider checksum) {
		this.checksum = checksum;
	}

	public TypeDictionary getDictionary() {
		return dictionary;
	}

	public void setDictionary(TypeDictionary dictionary) {
		this.dictionary = dictionary;
	}

	public HeaderProvider<?> getHeader() {
		return header;
	}

	public void setHeader(HeaderProvider<?> header) {
		this.header = header;
	}

	public InetAddress getAddress() {
		return address;
	}

	public void setAddress(InetAddress address) {
		this.address = address;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}