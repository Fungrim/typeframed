package net.larsan.protobuf.typeframe.netty;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;

import junit.framework.Assert;
import net.larsan.protobuf.typeframe.Messages;

import org.junit.Before;
import org.junit.Ignore;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.typeframed.api.Server;
import org.typeframed.api.ServerHandler;
import org.typeframed.api.ServerSession;
import org.typeframed.api.TypeDictionary;

import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;

public class NettyDryServerTest {
	
	@Mock
	private ServerHandler handler;
	
	private Server server;
	
	@Before
	public void init() throws Exception {
		MockitoAnnotations.initMocks(this);
		server = NettyServerBuilder.newInstance().withDictionary(createTypeDictionary())
							.bindTo(InetAddress.getByName("localhost"), 9000)
							.withHandler(handler).build();
		server.start();
	}
	
	// @Test
	@Ignore
	public void testServerConnectDisconnect() throws Exception {
		Socket sock = new Socket("localhost", 9000);
		Thread.sleep(100); // urgh...
		verify(handler, times(1)).connected(Mockito.any(ServerSession.class));
		sock.close();
		Thread.sleep(100); // urgh...
		verify(handler, times(1)).disconnected(Mockito.any(UUID.class));
	}
	
	public void destroy() throws Exception {
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
