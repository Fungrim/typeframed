package net.larsan.protobuf.typeframe.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;

import java.net.InetAddress;

import org.typeframed.api.Client;

public class NettyClient implements Client {

	private final Bootstrap boot;
	private final InetAddress address;
	private final int port;
	
	private Channel channel;
	private NettyClientSession session;

	NettyClient(Bootstrap boot, InetAddress address, int port) {
		this.boot = boot;
		this.address = address;
		this.port = port;
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public void connect() throws InterruptedException {
		channel = boot.connect(address, port).await().channel();
		channel.pipeline().addLast(new NettyClientHandler(this));
		session = new NettyClientSession(channel);
	}
	
	@Override
	public NettyClientSession getSession() {
		return session;
	}

	@Override
	public void disconnect() throws InterruptedException {
		try {
			channel.close().await();
		} catch (InterruptedException e) { } 
	}
}
