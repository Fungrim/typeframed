package org.typeframed.netty;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import org.typeframed.api.MessageEnvelope;
import org.typeframed.api.MessageReceiver;
import org.typeframed.api.MessageSender;
import org.typeframed.api.ServerSession;

public class NettyServerSession implements ServerSession {

	private final UUID id;
	private final Channel channel;
	private final AtomicReference<MessageReceiver<?>> receiver;
 	
	public NettyServerSession(UUID id, Channel channel) {
		receiver = new AtomicReference<MessageReceiver<?>>();
		this.id = id;
		this.channel = channel;
	}
	
	@Override
	public UUID getId() {
		return id;
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return (InetSocketAddress) channel.remoteAddress();
	}

	@Override
	public <H> void setReceiver(MessageReceiver<H> receiver) {
		this.receiver.getAndSet(receiver);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void forward(MessageEnvelope e) {
		MessageReceiver next = this.receiver.get();
		if(next != null) {
			next.receive(e);
		} else {
			// TODO ... ?
		}
	}

	@Override
	public <H> MessageSender<H> getSender() {
		return new NettyChannelSender<H>(this.channel);
	}

	@Override
	public Future<Boolean> disconnect() {
		return new ChannelFutureWrapper(channel.disconnect());
	}
}
