package org.typeframed.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import org.apache.log4j.Logger;
import org.typeframed.api.MessageEnvelope;

public class NettyClientHandler<H> extends SimpleChannelInboundHandler<MessageEnvelope<H>> {

	private final NettyClient client;
	private final Logger log = Logger.getLogger(getClass());

	public NettyClientHandler(NettyClient client) {
		this.client = client;
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, MessageEnvelope<H> msg) throws Exception {
		NettyClientSession session = client.getSession();
		if(session == null || !session.offer(msg)) {
			log.debug("Message received of type " + msg.getMessage().getClass().getName() + " while missing receiver; Dropping...");
		}
	}
}
