package net.larsan.protobuf.typeframe.parser;

import junit.framework.Assert;
import net.larsan.protobuf.typeframe.Echo.EchoRequest;

import org.junit.Test;

public class MessageDescriptorTest {

	@Test
	public void testCanonicalName() throws Exception {
		MessageDescriptor desc = new MessageDescriptor(1, EchoRequest.class.getName());
		Assert.assertEquals("net.larsan.protobuf.typeframe.Echo.EchoRequest", desc.getJavaCanonicalClassName());
		Assert.assertEquals("net.larsan.protobuf.typeframe.Echo$EchoRequest", desc.getJavaClassName());
	}
}
