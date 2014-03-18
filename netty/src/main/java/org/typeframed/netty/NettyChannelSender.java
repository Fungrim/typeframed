package org.typeframed.netty;

import io.netty.channel.Channel;

import java.util.concurrent.Future;

import org.typeframed.api.MessageEnvelope;
import org.typeframed.api.MessageSender;
import org.typeframed.netty.util.ChannelFutureWrapper;

import com.google.protobuf.Message;

public class NettyChannelSender<H> implements MessageSender<H> {

	private final Channel channel;
	
	public NettyChannelSender(Channel channel) {
		this.channel = channel;
	}
	
	@Override
	public Future<Boolean> send(H header, Message msg) {
		return new ChannelFutureWrapper(channel.write(MessageEnvelope.wrap(header, msg)));
	}
	
	@Override
	public Future<Boolean> send(Message msg) {
		return send(null, msg);
	}
}
