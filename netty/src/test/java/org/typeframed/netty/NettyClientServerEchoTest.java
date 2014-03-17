package org.typeframed.netty;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

import junit.framework.Assert;
import net.larsan.protobuf.typeframe.Echo.EchoRequest;
import net.larsan.protobuf.typeframe.Echo.EchoResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.typeframed.api.Client;
import org.typeframed.api.ClientBuilder;
import org.typeframed.api.ClientSession;
import org.typeframed.api.Int32HeaderProvider;
import org.typeframed.api.MessageEnvelope;
import org.typeframed.api.MessageReceiver;
import org.typeframed.api.Server;
import org.typeframed.api.ServerBuilder;
import org.typeframed.api.ServerHandler;
import org.typeframed.api.ServerSession;
import org.typeframed.api.TypeDictionary;
import org.typeframed.netty.NettyClientBuilder;
import org.typeframed.netty.NettyClientConfig;
import org.typeframed.netty.NettyServerBuilder;
import org.typeframed.netty.NettyServerConfig;
import org.typeframed.protobuf.parser.FileSource;
import org.typeframed.protobuf.parser.OptionInspector;
import org.typeframed.protobuf.parser.Source;
import org.typeframed.protobuf.parser.StandardDictionaryParser;

public class NettyClientServerEchoTest {
	
	private Server server;
	private TypeDictionary typeDictionary;
	
	@Before
	public void init() throws Exception {
		MockitoAnnotations.initMocks(this);
		StandardDictionaryParser parser = new StandardDictionaryParser(new OptionInspector("type_id"));
		typeDictionary = parser.parseDictionary(new Source[] { new FileSource(new File("src/test/proto/echo.proto")) });
		ServerBuilder<Integer, NettyServerConfig> builder = NettyServerBuilder.newInstance();
		server = builder.withDictionary(typeDictionary)
							.bindTo(InetAddress.getByName("localhost"), 9000)
							.withHeader(new Int32HeaderProvider())
							.withHandler(new EchoHandler()).build();
		server.start();
	}
	
	@Test
	public void testServerEcho() throws Exception {
		ClientBuilder<Integer, NettyClientConfig> builder = NettyClientBuilder.newInstance();
		Client client = builder.withHeader(new Int32HeaderProvider())
							.withDictionary(typeDictionary)
							.connectTo(InetAddress.getByName("localhost"), 9000)
							.build();
		client.connect();
		ClientSession session = client.getSession();
		Assert.assertEquals(new InetSocketAddress(InetAddress.getByName("localhost"), 9000), session.getRemoteAddress());
		final SynchronousQueue<MessageEnvelope<Integer>> q = new SynchronousQueue<MessageEnvelope<Integer>>();
		session.setReceiver(new MessageReceiver<Integer>() {

			@Override
			public void receive(MessageEnvelope<Integer> envelope) {
				q.offer(envelope);
			}
		});
		session.getSender().send(666, EchoRequest.newBuilder().setMsg("Hello").build());
		MessageEnvelope<Integer> e = q.poll(5, SECONDS);
		Assert.assertEquals(new Integer(667), e.getHeader());
		Assert.assertEquals("Hello", ((EchoResponse) e.getMessage()).getMsg());
		client.disconnect();
	}
	
	public void destroy() throws Exception {
		server.stop();
	}
	
	private class EchoHandler implements ServerHandler {
		
		private Map<UUID, ServerSession> sessions = new ConcurrentHashMap<UUID, ServerSession>();
		private ExecutorService sender = Executors.newCachedThreadPool();
		
		@Override
		public void connected(final ServerSession session) {
			sessions.put(session.getId(), session);
			session.setReceiver(new MessageReceiver<Integer>() {
				
				@Override
				public void receive(final MessageEnvelope<Integer> envelope) {
					sender.execute(new Runnable() {
						
						@Override
						public void run() {
							int nextHead = envelope.getHeader() + 1;
							String msg = ((EchoRequest) envelope.getMessage()).getMsg();
							EchoResponse resp = EchoResponse.newBuilder().setMsg(msg).build();
							session.getSender().send(nextHead, resp);
						}
					});
				}
			});
		}
		
		@Override
		public void disconnected(UUID session) {
			ServerSession ses = sessions.remove(session);
			if(ses != null) {
				ses.setReceiver(null);
			}
		}
	}
}
