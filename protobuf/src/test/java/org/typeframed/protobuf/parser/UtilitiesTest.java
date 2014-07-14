/**
 * Copyright 2014 Lars J. Nilsson <contact@larsan.net>
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collections;

import org.junit.Test;
import org.mockito.Mockito;
import org.typeframed.protobuf.parser.FileSource;
import org.typeframed.protobuf.parser.OptionsHolder;
import org.typeframed.protobuf.parser.Utilities;
import org.typeframed.protobuf.parser.node.OptionNode;

public class UtilitiesTest {

	@Test
	public void testFileName() {
		assertEquals("MyFile", Utilities.getRootClassFromFile(new FileSource(new File("my_file.proto"))));
		assertEquals("MyFile", Utilities.getRootClassFromFile(new FileSource(new File("myFile.proto"))));
		assertEquals("MyFile", Utilities.getRootClassFromFile(new FileSource(new File("my__file.proto"))));
		assertEquals("MyFile", Utilities.getRootClassFromFile(new FileSource(new File("_my__file.proto"))));
	}
	
	@Test
	public void testOptions() {
		OptionsHolder mock = Mockito.mock(OptionsHolder.class);
		OptionNode node = new OptionNode();
		node.setName("name");
		node.setValue("\"kalle\"");
		Mockito.when(mock.getOptions()).thenReturn(Collections.singletonList(node));
		assertTrue(Utilities.hasOption(mock, "name"));
		assertEquals("kalle", Utilities.getOption(mock, "name", true));
	}
}
