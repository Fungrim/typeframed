package org.typeframed.codegen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import org.typeframed.protobuf.parser.DictionaryParser;
import org.typeframed.protobuf.parser.ErrorHandler;
import org.typeframed.protobuf.parser.MessageDescriptor;
import org.typeframed.protobuf.parser.OptionInspector;
import org.typeframed.protobuf.parser.StandardDictionaryParser;

import com.google.common.io.Files;

public abstract class BaseCodeGenerator implements CodeGenerator {

	protected Config config;
	protected CodegenLogger logger;
	protected boolean createDirs = true;

	protected BaseCodeGenerator(Config config, CodegenLogger logger) {
		this.config = config;
		this.logger = logger;
	}
	
	@Override
	public final void generate() throws IOException {
		ErrorHandler handler = new ConfigErrorHandler(config, logger);
		DictionaryParser parser = new StandardDictionaryParser(new OptionInspector(config.getIdOptionName()), handler, logger);
		Set<MessageDescriptor> descriptors = parser.parseMessageDescriptors(config.getProtoFiles());
		String packageName = findPackageName(parser, config);
		File outDir = findOutputDir(packageName, config);
		generate(parser, descriptors, outDir, packageName);
	}
	
	protected abstract void generate(DictionaryParser parser, Set<MessageDescriptor> descriptors, File outDir, String packageName) throws IOException;

	private File findOutputDir(String packageName, Config config) {
		String javaDir = packageName.replace('.', File.separatorChar);
		if(createDirs && javaDir.length() > 0) {
			return new File(config.getOutputDir(), javaDir);
		} else {
			return config.getOutputDir();
		}
	}
	
	protected PrintWriter openFile(File f) throws IOException {
		if(createDirs) {
			Files.createParentDirs(f);
		} 
		return createWriter(f);
	}
	
	private PrintWriter createWriter(File output) throws IOException {
		return new PrintWriter(new BufferedWriter(new FileWriter(output)));
	}
	
	protected String findPackageName(DictionaryParser parser, Config config) {
		// TODO find package from protofile? but what if there are multiple files?
		String s = config.getCodegenPackage();
		return s == null ? "" : s;
	}
}
