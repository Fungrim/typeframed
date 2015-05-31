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
		"github.com/golang/protobuf/proto",
		"bitbucket.org/fungrim/go.typeframed",
		"reflect"
	};
	
	public GoCodeGenerator(GoConfig config, CodegenLogger logger) {
		super(config, logger);
		super.createDirs = false;
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
			println(wr, "\"" + s + "\"", 1);
		}
		for (String s : ((GoConfig)super.config).getExtraImports()) {
			println(wr, "\"" + s + "\"", 1);
		}
		println(wr, ")");
		wr.println();
	}

	private void generateDictionary(DictionaryParser parser, Set<MessageDescriptor> descriptors, PrintWriter wr) {
		println(wr, "type GoTypeDictionary struct{}");
		wr.println();
		println(wr, "func (d *GoTypeDictionary) GetId(msg proto.Message) (int, error) {");
		println(wr, "msgType := reflect.TypeOf(msg).Elem()", 1);
		boolean qualifiedNames = ((GoConfig)super.config).getExtraImports().length > 0;
		String reflectMethod = (qualifiedNames ? ".String()" : ".Name()");
		for (MessageDescriptor d : descriptors) {
			// TODO check package?
			println(wr, "if msgType" + reflectMethod +" == \"" + d.getGoStructName(qualifiedNames) + "\" {", 1);
			println(wr, "return " + d.getTypeId() + ", nil", 2);
			println(wr, "}", 1);
		}
		println(wr, "return -1, typeframed.NewUnknownMessage(msgType.Name())", 1);
		println(wr, "}");
		wr.println();
		println(wr, "func (d *GoTypeDictionary) NewMessageFromId(id int) (proto.Message, error) {");
		for (MessageDescriptor d : descriptors) {
			println(wr, "if id == " + d.getTypeId() + " {", 1);
			println(wr, "return &" + d.getGoStructName(qualifiedNames) + "{}, nil", 2);
			println(wr, "}", 1);
		}
		println(wr, "return nil, typeframed.NewNoSuchType(id)", 1);
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
