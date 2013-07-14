package net.larsan.protobuf.typeframe.codegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;

import net.larsan.protobuf.typeframe.parser.DictionaryParser;
import net.larsan.protobuf.typeframe.parser.OptionInspector;
import net.larsan.protobuf.typeframe.parser.StandardDictionaryParser;

import com.google.common.io.Files;
import com.google.protobuf.Message;

public class JavaCodeGenerator implements CodeGenerator {

	private static final String DICTIONARY_CLASSNAME = "JavaTypeDictionary";
	private static final String TAB = "    ";
	
	private final Config config;

	public JavaCodeGenerator(Config config) {
		this.config = config;
	}
	
	public void generate() throws ClassNotFoundException, IOException {
		DictionaryParser parser = new StandardDictionaryParser(new OptionInspector(config.getIdOptionName()));
		Map<Integer, Class<? extends Message>> map = parser.parse(config.getProtoFiles());
		String packageName = config.getProperty(Config.JAVA_PACKAGE_NAME, "");
		File output = new File(getOutputDir(packageName), getOutputFileName());
		Files.createParentDirs(output);
		try (PrintWriter wr = createWriter(output)) {
			println(wr, "// GENERATED CODE !!");
			if(packageName.length() > 0) {
				println(wr, "package " + packageName + ";");
				wr.println();
			}
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
			println(wr, "public int getId(Message msg) throws UnknownMessageException {", 1);
			println(wr, "Class<? extends Message> cl = msg.getClass();", 2);
			for (Entry<Integer, Class<? extends Message>> e : map.entrySet()) {
				println(wr, "if(" + e.getValue().getCanonicalName() + ".class.equals(cl)) {", 2);
				println(wr, "return " + e.getKey() + ";", 3);
				println(wr, "}", 2);
			}
			println(wr, "throw new UnknownMessageException(cl);", 2);
			println(wr, "}", 1);
			wr.println();
			println(wr, "public Message.Builder getBuilderForId(int id) throws NoSuchTypeException {", 1);
			println(wr, "Class<? extends Message> cl = null;", 2);
			for (Entry<Integer, Class<? extends Message>> e : map.entrySet()) {
				println(wr, "if(id == " + e.getKey() + ") {", 2);
				println(wr, "cl = " + e.getValue().getCanonicalName() + ".class;", 3);
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

	private String getOutputFileName() {
		return DICTIONARY_CLASSNAME + ".java";
	}

	private File getOutputDir(String packageName) {
		String javaDir = packageName.replace('.', File.separatorChar);
		if(javaDir.length() > 0) {
			return new File(config.getOutputDir(), javaDir);
		} else {
			return config.getOutputDir();
		}
	}
}
