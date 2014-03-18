package org.typeframed.netty.server;

import org.typeframed.api.Server;
import org.typeframed.api.ServerBuilder;
import org.typeframed.netty.util.ChannelConfigUtil;
import org.typeframed.netty.util.ServerBootstrapHelper;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServerBuilder<H> extends ServerBuilder<H, NettyServerConfig> {

	private ServerBootstrapHelper helper;

	public static <H> ServerBuilder<H, NettyServerConfig> newInstance() {
		return new NettyServerBuilder<H>();
	}
	
	private NettyServerBuilder() {
		super(new NettyServerConfig());
	}
	
	public NettyServerBuilder<H> withBootstrapHelper(ServerBootstrapHelper helper) {
		this.helper = helper;
		return this;
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
		if(helper != null) {
			boot = helper.assist(boot);
			bossGroup = boot.group();
			workerGroup = boot.childGroup();
		}
        return new NettyServer(bossGroup, workerGroup, boot, config.getAddress(), config.getPort());
	}
}
