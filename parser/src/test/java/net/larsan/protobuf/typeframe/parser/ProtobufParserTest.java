package net.larsan.protobuf.typeframe.parser;

import java.io.FileReader;
import java.util.Iterator;
import java.util.List;

import net.larsan.protobuf.typeframe.parser.node.EnumNode;
import net.larsan.protobuf.typeframe.parser.node.ExtendNode;
import net.larsan.protobuf.typeframe.parser.node.FieldNode;
import net.larsan.protobuf.typeframe.parser.node.MessageNode;
import net.larsan.protobuf.typeframe.parser.node.OptionNode;
import net.larsan.protobuf.typeframe.parser.node.RootNode;
import net.larsan.protobuf.typeframe.parser.node.TypeNode;

import org.junit.Assert;
import org.junit.Test;
import org.parboiled.Parboiled;
import org.parboiled.errors.ParseError;
import org.parboiled.parserunners.TracingParseRunner;
import org.parboiled.support.ParsingResult;

import com.google.common.io.CharStreams;

@SuppressWarnings({ "rawtypes" })
public class ProtobufParserTest {

	@Test
	public void testReadSimple1() throws Exception {
		ProtobufParser parser = Parboiled.createParser(ProtobufParser.class);
		TracingParseRunner runner = new TracingParseRunner(parser.File());
		String file = CharStreams.toString(new FileReader("src/test/resources/simple1.proto"));
		ParsingResult result = runner.run(file);
		checkForErrors(result);
		Object head = result.valueStack.pop();
		Assert.assertTrue(head instanceof RootNode);
		MessageNode node = (MessageNode) ((RootNode) head).getNodes().getFirst();
		Assert.assertEquals("Human", node.getName());
		Assert.assertEquals(3, node.getBody().getNodes().size());
		// TODO check nodes here...
	}
	
	@Test
	public void testReadPackage() throws Exception {
		ProtobufParser parser = Parboiled.createParser(ProtobufParser.class);
		TracingParseRunner runner = new TracingParseRunner(parser.File());
		String file = CharStreams.toString(new FileReader("src/test/resources/package.proto"));
		ParsingResult result = runner.run(file);
		checkForErrors(result);
		Object head = result.valueStack.pop();
		RootNode node = (RootNode) head;
		Assert.assertEquals("tutorial", node.getRootPackage());
	}
	
	@Test
	public void testReadOptions() throws Exception {
		ProtobufParser parser = Parboiled.createParser(ProtobufParser.class);
		TracingParseRunner runner = new TracingParseRunner(parser.File());
		String file = CharStreams.toString(new FileReader("src/test/resources/options.proto"));
		ParsingResult result = runner.run(file);
		checkForErrors(result);
		Object head = result.valueStack.pop();
		RootNode node = (RootNode) head;
		Assert.assertEquals(3, node.getOptions().size());
		OptionNode n = node.getOptions().get(0);
		Assert.assertEquals("java_package", n.getName());
		Assert.assertEquals("\"com.example.tutorial\"", n.getValue());
		n = node.getOptions().get(1);
		Assert.assertEquals("java_generic_services", n.getName());
		Assert.assertEquals("true", n.getValue());
		n = node.getOptions().get(2);
		Assert.assertEquals("optimize_for", n.getName());
		Assert.assertEquals("LITE_RUNTIME", n.getValue());
	}
	
	@Test
	public void testReadFieldOptions() throws Exception {
		ProtobufParser parser = Parboiled.createParser(ProtobufParser.class);
		TracingParseRunner runner = new TracingParseRunner(parser.File());
		String file = CharStreams.toString(new FileReader("src/test/resources/field-options.proto"));
		ParsingResult result = runner.run(file);
		checkForErrors(result);
		Object head = result.valueStack.pop();
		MessageNode node = (MessageNode) ((RootNode) head).getNodes().getFirst();
		Assert.assertEquals("Screen", node.getName());
		Assert.assertEquals(2, node.getBody().getNodes().size());
		Iterator<FieldNode> it = node.getBody().getNodes().iterator();
		TypeNode field = (TypeNode) it.next();
		Assert.assertEquals(1, field.getOptions().size());
		Assert.assertEquals("default", field.getOptions().get(0).getName());
		Assert.assertEquals("70", field.getOptions().get(0).getValue());
		field = (TypeNode) it.next();
		Assert.assertEquals(1, field.getOptions().size());
		Assert.assertEquals("default", field.getOptions().get(0).getName());
		Assert.assertEquals("0.11", field.getOptions().get(0).getValue());
	}
	
	@Test
	public void testReadMessageOption() throws Exception {
		ProtobufParser parser = Parboiled.createParser(ProtobufParser.class);
		TracingParseRunner runner = new TracingParseRunner(parser.File());
		String file = CharStreams.toString(new FileReader("src/test/resources/message-options.proto"));
		ParsingResult result = runner.run(file);
		checkForErrors(result);
		Object head = result.valueStack.pop();
		Assert.assertTrue(head instanceof RootNode);
		MessageNode node = (MessageNode) ((RootNode) head).getNodes().getFirst();
		Assert.assertEquals("Hood", node.getName());
		Assert.assertEquals(1, node.getOptions().size());
		Assert.assertEquals("message_set_wire_format", node.getOptions().get(0).getName());
		Assert.assertEquals("true", node.getOptions().get(0).getValue());
	}
	
	@Test
	public void testReadCustomOption() throws Exception {
		ProtobufParser parser = Parboiled.createParser(ProtobufParser.class);
		TracingParseRunner runner = new TracingParseRunner(parser.File());
		String file = CharStreams.toString(new FileReader("src/test/resources/custom-options.proto"));
		ParsingResult result = runner.run(file);
		checkForErrors(result);
		Object head = result.valueStack.pop();
		Assert.assertTrue(head instanceof RootNode);
		Iterator<FieldNode> it = ((RootNode) head).getNodes().iterator();
		MessageNode node = (MessageNode) it.next();
		Assert.assertTrue(node instanceof ExtendNode);
		Assert.assertEquals("google.protobuf.MessageOptions", node.getName());
		Assert.assertEquals(1, node.getBody().getNodes().size());
		node = (MessageNode) it.next();
		OptionNode opt = node.getOptions().get(0);
		Assert.assertEquals("my_option", opt.getName());
		Assert.assertEquals("\"Hello world!\"", opt.getValue());
		Assert.assertTrue(opt.isCustom());
	}
	
	@Test
	public void testReadEnum() throws Exception {
		ProtobufParser parser = Parboiled.createParser(ProtobufParser.class);
		TracingParseRunner runner = new TracingParseRunner(parser.File());
		String file = CharStreams.toString(new FileReader("src/test/resources/enums.proto"));
		ParsingResult result = runner.run(file);
		checkForErrors(result);
		RootNode root = (RootNode) result.valueStack.pop();
		Assert.assertEquals(1, root.getEnums().size());
		EnumNode enm = root.getEnums().get(0);
		Assert.assertEquals("Flag", enm.getName());
		Assert.assertEquals("ACK", enm.getFields().get(0).getName());
		Assert.assertEquals("NAK", enm.getFields().get(1).getName());
		List<OptionNode> opts = enm.getFields().get(0).getOptions();
		Assert.assertEquals(1, opts.size());
		Assert.assertEquals("my_enum_value_option", opts.get(0).getName());
		Assert.assertEquals("321", opts.get(0).getValue());
		Assert.assertTrue(opts.get(0).isCustom());
	}
	
	@Test
	public void testReadImports() throws Exception {
		ProtobufParser parser = Parboiled.createParser(ProtobufParser.class);
		TracingParseRunner runner = new TracingParseRunner(parser.File());
		String file = CharStreams.toString(new FileReader("src/test/resources/imports.proto"));
		ParsingResult result = runner.run(file);
		checkForErrors(result);
	}
	
	@Test
	public void testReadNested1() throws Exception {
		ProtobufParser parser = Parboiled.createParser(ProtobufParser.class);
		TracingParseRunner runner = new TracingParseRunner(parser.File());
		String file = CharStreams.toString(new FileReader("src/test/resources/nested1.proto"));
		ParsingResult result = runner.run(file);
		checkForErrors(result);
		Object head = result.valueStack.pop();
		MessageNode node = (MessageNode) ((RootNode) head).getNodes().getFirst();
		Assert.assertEquals("Company", node.getName());
		Assert.assertEquals(2, node.getBody().getNodes().size());
		Iterator<FieldNode> it = node.getBody().getNodes().iterator();
		Assert.assertTrue(it.next() instanceof TypeNode);
		Object tmp = it.next();
		Assert.assertTrue(tmp instanceof MessageNode);
		MessageNode nested = (MessageNode) tmp;
		Assert.assertEquals("Address", nested.getName());
		Assert.assertEquals(2, nested.getBody().getNodes().size());
		it = nested.getBody().getNodes().iterator();
		Assert.assertTrue(it.next() instanceof TypeNode);
		tmp = it.next();
		Assert.assertTrue(tmp instanceof MessageNode);
		nested = (MessageNode) tmp;
		Assert.assertEquals("PostCode", nested.getName());
	}
	
	@Test
	public void testReadHard() throws Exception {
		ProtobufParser parser = Parboiled.createParser(ProtobufParser.class);
		TracingParseRunner runner = new TracingParseRunner(parser.File());
		String file = CharStreams.toString(new FileReader("src/test/resources/hard.proto"));
		ParsingResult result = runner.run(file);
		checkForErrors(result);
	}
	
	
	// --- PRIVATE METHODS --- //
	
	private void checkForErrors(ParsingResult result) {
		if(result.hasErrors()) {
			for (Object o : result.parseErrors) {
				ParseError err = (ParseError) o; 
				System.err.println(err.getClass().getSimpleName() + ":" + err.getStartIndex() + "-" + err.getEndIndex());
			}
			Assert.fail();
		}
	}
}
