package org.typeframed.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

import org.typeframed.api.MessageEnvelope;
import org.typeframed.api.server.ServerHandler;

public class NettyServerHandler<H> extends SimpleChannelInboundHandler<MessageEnvelope<H>> {

	private final ServerHandler handler;
	private final UUID id;

	private NettyServerSession session;
	
	public NettyServerHandler(ServerHandler handler) {
		this.id = UUID.randomUUID();
		this.handler = handler;
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		handler.disconnected(id);
	}
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		session = new NettyServerSession(id, ctx.channel());
		handler.connected(session);
	}
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, MessageEnvelope<H> msg) throws Exception {
		session.forward(msg);
	}
}
