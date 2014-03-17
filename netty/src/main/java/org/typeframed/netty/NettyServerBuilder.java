package org.typeframed.netty;

import org.typeframed.api.Server;
import org.typeframed.api.ServerBuilder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServerBuilder<H> extends ServerBuilder<H, NettyServerConfig> {

	public static <H> ServerBuilder<H, NettyServerConfig> newInstance() {
		return new NettyServerBuilder<H>();
	}
	
	private NettyServerBuilder() {
		super(new NettyServerConfig());
	}
	
	@Override
	protected Server doBuild(final NettyServerConfig config) {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap boot = new ServerBootstrap();
		boot.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<SocketChannel>() { 
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                	 new ChannelConfigUtil(config).init(ch);
                	 ch.pipeline().addLast(new NettyServerHandler<H>(config.getHandler()));
                 }
             });
         return new NettyServer(bossGroup, workerGroup, boot, config.getAddress(), config.getPort());
	}
}
