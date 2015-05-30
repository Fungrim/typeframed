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
package org.typeframed.codegen;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import org.typeframed.protobuf.parser.DictionaryParser;
import org.typeframed.protobuf.parser.MessageDescriptor;

public class JavaCodeGenerator extends BaseCodeGenerator {

	private static final String TAB = "    ";

	private static final String DICTIONARY_CLASSNAME = "JavaTypeDictionary";
	private static final String HANDLER_CLASSNAME = "JavaTypeSwitchTarget";
	private static final String SWITCH_CLASSNAME = "JavaTypeSwitch";
	
	public JavaCodeGenerator(Config config, CodegenLogger logger) {
		super(config, logger);
	}
	
	@Override
	protected void generate(DictionaryParser parser, Set<MessageDescriptor> descriptors, File outDir, String packageName) throws IOException {
		generateDictionary(parser, descriptors, outDir, packageName);
		generateSwitch(parser, descriptors, outDir, packageName);
		generateHandler(parser, descriptors, outDir, packageName);
	}

	private void generateSwitch(DictionaryParser parser, Set<MessageDescriptor> descriptors, File dir, String packageName) throws IOException {
		try (PrintWriter wr = openFile(new File(dir, getSwitchFileName()))) {
			writePackageHead(packageName, wr);
			println(wr, "import org.typeframed.api.util.TypeSwitch;");
			println(wr, "import org.typeframed.api.util.TypeSwitch.Target;");
			println(wr, "import org.typeframed.api.UnknownMessageException;");
			println(wr, "import org.typeframed.api.MessageTypeDictionary;");
			wr.println();
			println(wr, "import com.google.protobuf.Message;");
			wr.println();
			println(wr, "public class " + SWITCH_CLASSNAME + " extends TypeSwitch {");
			wr.println();
			println(wr, "protected final " + HANDLER_CLASSNAME + " target;", 1);
			println(wr, "protected final MessageTypeDictionary dictionary;", 1);
			wr.println();
			println(wr, "public " + SWITCH_CLASSNAME + "(" + HANDLER_CLASSNAME + " target) {", 1);
			println(wr, "this(target, new " + DICTIONARY_CLASSNAME + "());", 2);
			println(wr, "}", 1);
			wr.println();
			println(wr, "public " + SWITCH_CLASSNAME + "(" + HANDLER_CLASSNAME + " target, MessageTypeDictionary dictionary) {", 1);
			println(wr, "this.dictionary = dictionary;", 2);
			println(wr, "this.target = target;", 2);
			println(wr, "}", 1);
			wr.println();
			println(wr, "@Override", 1);
			println(wr, "public void forward(Message msg) {", 1);
			println(wr, "Class<? extends Message> cl = msg.getClass();", 2);
			for (MessageDescriptor desc : descriptors) {
				println(wr, "if(" + desc.getJavaCanonicalClassName() + ".class.equals(cl)) {", 2);
				println(wr, "target.handle((" + desc.getJavaCanonicalClassName() + ") msg);", 3);
				println(wr, "return;", 3);
				println(wr, "}", 2);
			}
			println(wr, "throw new UnknownMessageException(cl);", 2);
			println(wr, "}", 1);
			println(wr, "}");
		}
	}
	
	private void generateHandler(DictionaryParser parser, Set<MessageDescriptor> descriptors, File dir, String packageName) throws IOException {
		try (PrintWriter wr = openFile(new File(dir, getHandleFileName()))) {
			writePackageHead(packageName, wr);
			println(wr, "import org.typeframed.api.util.TypeSwitch.Target;");
			wr.println();
			println(wr, "public abstract class " + HANDLER_CLASSNAME + " implements Target {");
			wr.println();
			for (MessageDescriptor desc : descriptors) {
				println(wr, "public abstract void handle(" + desc.getJavaCanonicalClassName() + " msg);", 1);
				wr.println();
			}
			println(wr, "}");
		}
	}

	private void generateDictionary(DictionaryParser parser, Set<MessageDescriptor> descriptors, File dir, String packageName) throws IOException {
		try (PrintWriter wr = openFile(new File(dir, getDictionaryFileName()))) {
			writePackageHead(packageName, wr);
			println(wr, "import org.typeframed.api.NoSuchTypeException;");
			println(wr, "import org.typeframed.api.MessageTypeDictionary;");
			println(wr, "import org.typeframed.api.UnknownMessageException;");
			wr.println();
			println(wr, "import com.google.protobuf.Message;");
			println(wr, "import com.google.protobuf.Message.Builder;");
			wr.println();
			println(wr, "import java.lang.reflect.Method;");
			wr.println();
			println(wr, "public class " + DICTIONARY_CLASSNAME + " implements MessageTypeDictionary {");
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

	private void println(PrintWriter wr, String string) {
		println(wr, string, 0);
	}

	private void println(PrintWriter wr, String string, int tab) {
		for (int i = 0; i < tab; i++) {
			wr.print(TAB);
		}
		wr.println(string);
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
}
