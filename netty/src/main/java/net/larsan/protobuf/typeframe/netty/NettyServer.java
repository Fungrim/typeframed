package net.larsan.protobuf.typeframe.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;

import java.net.InetAddress;

import net.larsan.protobuf.typeframe.Server;

public class NettyServer implements Server {

	private final EventLoopGroup bossGroup;
	private final EventLoopGroup workerGroup;
	private final ServerBootstrap boot;
	
	private Channel channel;
	
	private final InetAddress address;
	private final int port;
	
	NettyServer(EventLoopGroup bossGroup, EventLoopGroup workerGroup, ServerBootstrap boot, InetAddress address, final int port) {
		this.bossGroup = bossGroup;
		this.workerGroup = workerGroup;
		this.boot = boot;
		this.address = address;
		this.port = port;
	}

	@Override
	public void start() throws InterruptedException {
		channel = boot.bind(address, port).await().channel();
	}

	@Override
	public void stop() throws InterruptedException {
		try {
			if(channel != null) {
				channel.close().await();
			}
		} finally {
			workerGroup.shutdownGracefully();
	        bossGroup.shutdownGracefully();	
		}
	}
}
