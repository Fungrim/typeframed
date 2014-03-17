package net.larsan.protobuf.typeframe.netty;

import org.typeframed.api.BuilderConfig;

import io.netty.channel.socket.SocketChannel;

public class ChannelConfigUtil {

	private BuilderConfig config;

	public ChannelConfigUtil(BuilderConfig config) {
		this.config = config;
	}

	public void init(SocketChannel ch) {
		ch.pipeline().addLast(new TypeFrameDecoder<Object>(config));
		ch.pipeline().addLast(new MessageEnvolopeDecoder<Object>());
		ch.pipeline().addLast(new MessageEnvelopeEncoder<Object>(config));
	}
}
