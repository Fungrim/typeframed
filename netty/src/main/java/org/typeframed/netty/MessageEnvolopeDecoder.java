package org.typeframed.netty;

import org.typeframed.api.NetworkFrame;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.MessageList;
import io.netty.handler.codec.MessageToMessageDecoder;

public class MessageEnvolopeDecoder<H> extends MessageToMessageDecoder<NetworkFrame<H>> {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, NetworkFrame<H> msg, MessageList<Object> out) throws Exception {
		out.add(msg.toEnvelope());
	}
}
