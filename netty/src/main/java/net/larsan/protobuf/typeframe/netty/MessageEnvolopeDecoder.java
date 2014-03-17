package net.larsan.protobuf.typeframe.netty;

import org.typeframed.api.TypeFrame;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.MessageList;
import io.netty.handler.codec.MessageToMessageDecoder;

public class MessageEnvolopeDecoder<H> extends MessageToMessageDecoder<TypeFrame<H>> {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, TypeFrame<H> msg, MessageList<Object> out) throws Exception {
		out.add(msg.toEnvelope());
	}
}
