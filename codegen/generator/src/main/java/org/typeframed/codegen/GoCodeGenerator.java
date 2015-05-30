package org.typeframed.codegen;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import org.typeframed.protobuf.parser.DictionaryParser;
import org.typeframed.protobuf.parser.MessageDescriptor;

public class GoCodeGenerator extends BaseCodeGenerator {

	private static final String TAB = "    ";
	private static final String[] IMPORTS = new String[] {
		"github.com/golang/protobuf/proto"
	};
	
	public GoCodeGenerator(Config config, CodegenLogger logger) {
		super(config, logger);
	}

	@Override
	protected void generate(DictionaryParser parser, Set<MessageDescriptor> descriptors, File outDir, String packageName) throws IOException {
		try (PrintWriter wr = openFile(new File(outDir, "typeframed.go"))) {
			writePackageHead(packageName, wr);
			writeImports(wr);
			generateDictionary(parser, descriptors, wr);
			
		}
	}

	private void writeImports(PrintWriter wr) {
		println(wr, "import (");
		for (String s : IMPORTS) {
			println(wr, s, 1);
		}
		println(wr, ")");
		wr.println();
	}

	private void generateDictionary(DictionaryParser parser, Set<MessageDescriptor> descriptors, PrintWriter wr) {
		println(wr, "type GoTypeDictionary struct{}");
		wr.println();
		println(wr, "func (d *GoTypeDictionary) GetId(msg proto.Message) (int, error) {");
		
		println(wr, "}");
		wr.println();
		println(wr, "func (d *GoTypeDictionary) NewMessageFromId(id int) (proto.Message, error) {");
		
		println(wr, "}");
		wr.println();
	}

	private void writePackageHead(String packageName, PrintWriter wr) {
		println(wr, "// GENERATED CODE !!");
		if(packageName.length() > 0) {
			println(wr, "package " + packageName);
			wr.println();
		}
	}
	
	private void println(PrintWriter wr, String string) {
		println(wr, string, 0);
	}

	private void println(PrintWriter wr, String string, int tab) {
		for (int i = 0; i < tab; i++) {
			wr.print(TAB);
		}
		wr.println(string);
	}
}
