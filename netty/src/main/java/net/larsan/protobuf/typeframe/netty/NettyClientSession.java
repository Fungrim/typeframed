package net.larsan.protobuf.typeframe.netty;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

import net.larsan.protobuf.typeframe.ClientSession;
import net.larsan.protobuf.typeframe.MessageEnvelope;
import net.larsan.protobuf.typeframe.MessageReceiver;
import net.larsan.protobuf.typeframe.MessageSender;

public class NettyClientSession implements ClientSession {
	
	private final Channel channel;
	private final AtomicReference<MessageReceiver<?>> receiver;
	
	public NettyClientSession(Channel channel) {
		receiver = new AtomicReference<MessageReceiver<?>>();
		this.channel = channel;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean offer(MessageEnvelope<?> envelope) {
		MessageReceiver rec = this.receiver.get();
		if(rec != null) {
			rec.receive(envelope);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return (InetSocketAddress) channel.remoteAddress();
	}

	@Override
	public <H> void setReceiver(MessageReceiver<H> receiver) {
		this.receiver.getAndSet(receiver);
	}

	@Override
	public <H> MessageSender<H> getSender() {
		return new NettyChannelSender<H>(this.channel);
	}
}
