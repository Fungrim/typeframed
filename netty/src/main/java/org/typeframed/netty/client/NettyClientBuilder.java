package org.typeframed.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import org.typeframed.api.client.Client;
import org.typeframed.api.client.ClientBuilder;
import org.typeframed.netty.util.ChannelConfigUtil;
import org.typeframed.netty.util.ClientBootstrapHelper;

public class NettyClientBuilder<H> extends ClientBuilder<H, NettyClientConfig> {

	public static <H> ClientBuilder<H, NettyClientConfig> newInstance() {
		return new NettyClientBuilder<H>();
	}

	private Bootstrap boot;
	private ClientBootstrapHelper helper;
	
	private NettyClientBuilder() {
		super(new NettyClientConfig());
		boot = new Bootstrap();
		boot.group(new NioEventLoopGroup())
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    new ChannelConfigUtil(config).init(ch);
                }
            });
		if(helper != null) {
			boot = helper.assist(boot);
		}
	}
	
	public NettyClientBuilder<H> withBootstrapHelper(ClientBootstrapHelper helper) {
		this.helper = helper;
		return this;
	}
	
	@Override
	protected Client doBuild(final NettyClientConfig config) {
		return new NettyClient(boot, config.getAddress(), config.getPort());
	}
}
