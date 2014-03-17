package net.larsan.protobuf.typeframe.netty;

import org.typeframed.api.Client;
import org.typeframed.api.ClientBuilder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClientBuilder<H> extends ClientBuilder<H, NettyClientConfig> {

	public static <H> ClientBuilder<H, NettyClientConfig> newInstance() {
		return new NettyClientBuilder<H>();
	}

	private final Bootstrap boot;
	private final NioEventLoopGroup workerGroup;
	
	private NettyClientBuilder() {
		super(new NettyClientConfig());
		workerGroup = new NioEventLoopGroup();
		boot = new Bootstrap();
		boot.group(workerGroup)
			.channel(NioSocketChannel.class)
			.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    new ChannelConfigUtil(config).init(ch);
                }
            });
	}
	
	@Override
	protected Client doBuild(final NettyClientConfig config) {
		return new NettyClient(boot, config.getAddress(), config.getPort());
	}
}
