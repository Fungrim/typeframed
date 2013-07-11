package net.larsan.protobuf.typeframe.netty;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import net.larsan.protobuf.typeframe.MessageEnvelope;
import net.larsan.protobuf.typeframe.MessageReceiver;
import net.larsan.protobuf.typeframe.MessageSender;
import net.larsan.protobuf.typeframe.ServerSession;

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
