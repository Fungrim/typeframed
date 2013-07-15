package net.larsan.protobuf.typeframe.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import net.larsan.protobuf.typeframe.Echo.EchoRequest;
import net.larsan.protobuf.typeframe.Echo.EchoResponse;
import net.larsan.protobuf.typeframe.Messages.Person;
import net.larsan.protobuf.typeframe.codegen.IllegalIdException;
import net.larsan.protobuf.typeframe.parser.OptionInspector;
import net.larsan.protobuf.typeframe.parser.StandardDictionaryParser;
import net.larsan.protobuf.typeframe.NoSuchTypeException;
import net.larsan.protobuf.typeframe.TypeDictionary;
import net.larsan.protobuf.typeframe.UnknownMessageException;

import org.junit.Test;

public class StandardDictionaryParserTest {

	@Test
	public void testDictionaryParsing() throws Exception {
		StandardDictionaryParser parser = new StandardDictionaryParser(new OptionInspector("type_id"));
		parser.parseClassMap(toSourceArray(new File("src/test/proto/hard.proto")));
	}
	
	@Test(expected=IllegalIdException.class)
	public void testDuplicateId() throws Exception {
		StandardDictionaryParser parser = new StandardDictionaryParser(new OptionInspector("type_id2"));
		parser.parseClassMap(toSourceArray(new File("src/test/proto/duplicateid.proto")));
	}
	
	@Test
	public void testTypeDictionaryParsing() throws Exception {
		StandardDictionaryParser parser = new StandardDictionaryParser(new OptionInspector("type_id"));
		TypeDictionary dict = parser.parseDictionary(toSourceArray(new File("src/test/proto/echo.proto")));
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
