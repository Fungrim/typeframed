/**
 * Copyright 2015 Lars J. Nilsson <contact@larsan.net>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
