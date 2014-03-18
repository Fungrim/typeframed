package org.typeframed.codegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import org.typeframed.api.CodeGenerator;
import org.typeframed.protobuf.parser.DictionaryParser;
import org.typeframed.protobuf.parser.ErrorHandler;
import org.typeframed.protobuf.parser.MessageDescriptor;
import org.typeframed.protobuf.parser.OptionInspector;
import org.typeframed.protobuf.parser.StandardDictionaryParser;

import com.google.common.io.Files;

public class JavaCodeGenerator implements CodeGenerator {

	private static final String DICTIONARY_CLASSNAME = "JavaTypeDictionary";
	private static final String TAB = "    ";
	private static final String HANDLER_CLASSNAME = "JavaMessageSwitchHandler";
	private static final String SWITCH_CLASSNAME = "JavaMessageSwitch";
	
	private final Config config;
	private CodegenLogger logger;
	
	public JavaCodeGenerator(Config config) {
		this.config = config;
	}
	
	public void setCodegenLogger(CodegenLogger logger) {
		this.logger = logger;
	}

	public void generate() throws IOException {
		ErrorHandler handler = (logger == null ? new ConfigErrorHandler(config) : new ConfigErrorHandler(config, logger));
		DictionaryParser parser = new StandardDictionaryParser(new OptionInspector(config.getIdOptionName()), handler);
		Set<MessageDescriptor> descriptors = parser.parseMessageDescriptors(config.getProtoFiles());
		String packageName = findPackageName(parser, config);
		File outDir = getOutputDir(packageName, config);
		generateDictionary(parser, descriptors, outDir, packageName);
		generateSwitch(parser, descriptors, outDir, packageName);
		generateHandler(parser, descriptors, outDir, packageName);
	}

	private void generateSwitch(DictionaryParser parser, Set<MessageDescriptor> descriptors, File dir, String packageName) throws IOException {
		File output = new File(dir, getSwitchFileName());
		Files.createParentDirs(output);
		try (PrintWriter wr = createWriter(output)) {
			writePackageHead(packageName, wr);
			println(wr, "import net.larsan.protobuf.typeframe.codegen.MessageSwitch;");
			println(wr, "import net.larsan.protobuf.typeframe.UnknownMessageException;");
			println(wr, "import net.larsan.protobuf.typeframe.TypeDictionary;");
			wr.println();
			println(wr, "import com.google.protobuf.Message;");
			wr.println();
			println(wr, "public class " + SWITCH_CLASSNAME + "<H> extends MessageSwitch<H> {");
			wr.println();
			println(wr, "protected final " + HANDLER_CLASSNAME + " handler;", 1);
			println(wr, "protected final TypeDictionary dictionary;", 1);
			wr.println();
			println(wr, "public " + SWITCH_CLASSNAME + "(" + HANDLER_CLASSNAME + " handler) {", 1);
			println(wr, "this(handler, new " + DICTIONARY_CLASSNAME + "());", 2);
			println(wr, "}", 1);
			wr.println();
			println(wr, "public " + SWITCH_CLASSNAME + "(" + HANDLER_CLASSNAME + " handler, TypeDictionary dictionary) {", 1);
			println(wr, "this.dictionary = dictionary;", 2);
			println(wr, "this.handler = handler;", 2);
			println(wr, "}", 1);
			wr.println();
			println(wr, "@Override", 1);
			println(wr, "public void doSwitch(Message msg) {", 1);
			println(wr, "Class<? extends Message> cl = msg.getClass();", 2);
			for (MessageDescriptor desc : descriptors) {
				println(wr, "if(" + desc.getJavaCanonicalClassName() + ".class.equals(cl)) {", 2);
				println(wr, "handler.handle((" + desc.getJavaCanonicalClassName() + ") msg);", 3);
				println(wr, "return;", 3);
				println(wr, "}", 2);
			}
			println(wr, "throw new UnknownMessageException(cl);", 2);
			println(wr, "}", 1);
			println(wr, "}");
		}
	}
	
	private void generateHandler(DictionaryParser parser, Set<MessageDescriptor> descriptors, File dir, String packageName) throws IOException {
		File output = new File(dir, getHandleFileName());
		Files.createParentDirs(output);
		try (PrintWriter wr = createWriter(output)) {
			writePackageHead(packageName, wr);
			println(wr, "import net.larsan.protobuf.typeframe.codegen.MessageHandler;");
			wr.println();
			println(wr, "public abstract class " + HANDLER_CLASSNAME + " implements MessageHandler {");
			wr.println();
			for (MessageDescriptor desc : descriptors) {
				println(wr, "public abstract void handle(" + desc.getJavaCanonicalClassName() + " msg);", 1);
				wr.println();
			}
			println(wr, "}");
		}
	}

	private void generateDictionary(DictionaryParser parser, Set<MessageDescriptor> descriptors, File dir, String packageName) throws IOException {
		File output = new File(dir, getDictionaryFileName());
		Files.createParentDirs(output);
		try (PrintWriter wr = createWriter(output)) {
			writePackageHead(packageName, wr);
			println(wr, "import net.larsan.protobuf.typeframe.NoSuchTypeException;");
			println(wr, "import net.larsan.protobuf.typeframe.TypeDictionary;");
			println(wr, "import net.larsan.protobuf.typeframe.UnknownMessageException;");
			wr.println();
			println(wr, "import com.google.protobuf.Message;");
			println(wr, "import com.google.protobuf.Message.Builder;");
			wr.println();
			println(wr, "import java.lang.reflect.Method;");
			wr.println();
			println(wr, "public class " + DICTIONARY_CLASSNAME + " implements TypeDictionary {");
			wr.println();
			println(wr, "@Override", 1);
			println(wr, "public int getId(Message msg) throws UnknownMessageException {", 1);
			println(wr, "Class<? extends Message> cl = msg.getClass();", 2);
			for (MessageDescriptor desc : descriptors) {
				println(wr, "if(" + desc.getJavaCanonicalClassName() + ".class.equals(cl)) {", 2);
				println(wr, "return " + desc.getTypeId() + ";", 3);
				println(wr, "}", 2);
			}
			println(wr, "throw new UnknownMessageException(cl);", 2);
			println(wr, "}", 1);
			wr.println();
			println(wr, "@Override", 1);
			println(wr, "public Message.Builder getBuilderForId(int id) throws NoSuchTypeException {", 1);
			println(wr, "Class<? extends Message> cl = null;", 2);
			for (MessageDescriptor desc : descriptors) {
				println(wr, "if(id == " + desc.getTypeId() + ") {", 2);
				println(wr, "cl = " + desc.getJavaCanonicalClassName() + ".class;", 3);
				println(wr, "}", 2);
			}
			println(wr, "if(cl == null) {", 2);
			println(wr, "throw new NoSuchTypeException(id);", 3);
			println(wr, "}", 2);
			println(wr, "try {", 2);
			println(wr, "Method method = cl.getMethod(\"newBuilder\");", 3);
			println(wr, "return (Builder) method.invoke(null);", 3);
			println(wr, "} catch(Exception e) {", 2);
			println(wr, "throw new IllegalStateException(\"Failed to create new builder\", e);", 3);
			println(wr, "}", 2);
			println(wr, "}", 1);
			println(wr, "}");
		}
	}

	private void writePackageHead(String packageName, PrintWriter wr) {
		println(wr, "// GENERATED CODE !!");
		if(packageName.length() > 0) {
			println(wr, "package " + packageName + ";");
			wr.println();
		}
	}

	private String findPackageName(DictionaryParser parser, Config config) {
		// TODO find package from protofile? but what if there are multiple files?
		return config.getProperty(Config.JAVA_PACKAGE_NAME, "");
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

	private PrintWriter createWriter(File output) throws IOException {
		return new PrintWriter(new BufferedWriter(new FileWriter(output)));
	}

	private String getDictionaryFileName() {
		return DICTIONARY_CLASSNAME + ".java";
	}
	
	private String getSwitchFileName() {
		return SWITCH_CLASSNAME + ".java";
	}
	
	private String getHandleFileName() {
		return HANDLER_CLASSNAME + ".java";
	}

	private File getOutputDir(String packageName, Config config) {
		String javaDir = packageName.replace('.', File.separatorChar);
		if(javaDir.length() > 0) {
			return new File(config.getOutputDir(), javaDir);
		} else {
			return config.getOutputDir();
		}
	}
}
