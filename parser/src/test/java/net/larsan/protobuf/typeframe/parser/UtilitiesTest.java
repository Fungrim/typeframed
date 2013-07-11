package net.larsan.protobuf.typeframe.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collections;

import net.larsan.protobuf.typeframe.parser.node.OptionNode;

import org.junit.Test;
import org.mockito.Mockito;

public class UtilitiesTest {

	@Test
	public void testFileName() {
		assertEquals("MyFile", Utilities.getRootClassFromFile(new File("my_file.proto")));
		assertEquals("MyFile", Utilities.getRootClassFromFile(new File("myFile.proto")));
		assertEquals("MyFile", Utilities.getRootClassFromFile(new File("my__file.proto")));
		assertEquals("MyFile", Utilities.getRootClassFromFile(new File("_my__file.proto")));
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
