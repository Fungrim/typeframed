package org.typeframed.protobuf.parser;

import junit.framework.Assert;
import net.larsan.protobuf.typeframe.Echo.EchoRequest;

import org.junit.Test;
import org.typeframed.protobuf.parser.MessageDescriptor;

public class MessageDescriptorTest {

	@Test
	public void testCanonicalName() throws Exception {
		MessageDescriptor desc = new MessageDescriptor(1, "", EchoRequest.class.getName());
		Assert.assertEquals("net.larsan.protobuf.typeframe.Echo.EchoRequest", desc.getJavaCanonicalClassName());
		Assert.assertEquals("net.larsan.protobuf.typeframe.Echo$EchoRequest", desc.getJavaClassName());
	}
}
