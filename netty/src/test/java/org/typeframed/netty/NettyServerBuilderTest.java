package org.typeframed.netty;

import java.util.UUID;

import junit.framework.Assert;
import net.larsan.protobuf.typeframe.Messages;

import org.junit.Test;
import org.typeframed.api.Server;
import org.typeframed.api.ServerHandler;
import org.typeframed.api.ServerSession;
import org.typeframed.api.TypeDictionary;
import org.typeframed.api.digest.CRC32ChecksumProvider;
import org.typeframed.netty.NettyServerBuilder;

import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;

public class NettyServerBuilderTest {

	@Test
	public void testStartAndStop() throws Exception {
		Server server = NettyServerBuilder.newInstance()
							.bindTo(null, 8999)
							.withChecksum(new CRC32ChecksumProvider())
							.withDictionary(createTypeDictionary())
							.withHandler(new ServerHandler() {
								
								@Override
								public void disconnected(UUID session) {
									// TODO Auto-generated method stub
									
								}
								
								@Override
								public void connected(ServerSession session) {
									// TODO Auto-generated method stub
									
								}
							})
							.build();
		
		server.start();
		Thread.sleep(100);
		server.stop();
	}

	private TypeDictionary createTypeDictionary() {
		return new TypeDictionary() {
			
			@Override
			public int getId(Message msg) {
				return 666;
			}
			
			@Override
			public Builder getBuilderForId(int id) {
				Assert.assertEquals(666, id);
				return Messages.Person.newBuilder();
			}
		};
	}
}
