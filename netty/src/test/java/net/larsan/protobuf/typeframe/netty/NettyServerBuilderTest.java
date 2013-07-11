package net.larsan.protobuf.typeframe.netty;

import java.util.UUID;

import junit.framework.Assert;
import net.larsan.protobuf.typeframe.Messages;
import net.larsan.protobuf.typeframe.Server;
import net.larsan.protobuf.typeframe.ServerHandler;
import net.larsan.protobuf.typeframe.ServerSession;
import net.larsan.protobuf.typeframe.TypeDictionary;
import net.larsan.protobuf.typeframe.digest.CRC32ChecksumProvider;

import org.junit.Test;

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
