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
import static org.junit.Assert.fail;

import java.io.File;

import net.larsan.protobuf.typeframe.Echo.EchoRequest;
import net.larsan.protobuf.typeframe.Echo.EchoResponse;
import net.larsan.protobuf.typeframe.Messages.Person;

import org.junit.Test;
import org.typeframed.api.MessageTypeDictionary;
import org.typeframed.api.NoSuchTypeException;
import org.typeframed.api.UnknownMessageException;
import org.typeframed.api.codegen.DuplicateIdException;

public class StandardDictionaryParserTest {

	@Test
	public void testDictionaryParsing() throws Exception {
		StandardDictionaryParser parser = new StandardDictionaryParser(new OptionInspector("type_id"), new DummyLogger());
		parser.parseClassMap(toSourceArray(new File("src/test/proto/hard.proto")));
	}
	
	@Test(expected=DuplicateIdException.class)
	public void testDuplicateId() throws Exception {
		StandardDictionaryParser parser = new StandardDictionaryParser(new OptionInspector("type_id2"), new DummyLogger());
		parser.parseClassMap(toSourceArray(new File("src/test/proto/duplicateid.proto")));
	}
	
	@Test
	public void testTypeDictionaryParsing() throws Exception {
		StandardDictionaryParser parser = new StandardDictionaryParser(new OptionInspector("type_id"), new DummyLogger());
		MessageTypeDictionary dict = parser.parseDictionary(toSourceArray(new File("src/test/proto/echo.proto")));
		assertEquals(1, dict.getId(EchoRequest.getDefaultInstance()));
		assertEquals(2, dict.getId(EchoResponse.getDefaultInstance()));
		try {
			dict.getId(Person.getDefaultInstance());
			fail();
		} catch(UnknownMessageException e) { }
		assertTrue(dict.getBuilderForId(1) instanceof EchoRequest.Builder);
		assertTrue(dict.getBuilderForId(2) instanceof EchoResponse.Builder);
		try {
			dict.getBuilderForId(3);
			fail();
		} catch(NoSuchTypeException e) { }
	}

	private Source[] toSourceArray(File file) {
		return new Source[] { new FileSource(file) };
	}
}
