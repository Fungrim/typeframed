package net.larsan.protobuf.typeframe.codegen;

import java.io.File;
import java.util.Properties;

import org.apache.commons.lang3.builder.ToStringBuilder;

import net.larsan.protobuf.typeframe.parser.FileSource;
import net.larsan.protobuf.typeframe.parser.Source;

public class Config {
	
	public static final String JAVA_PACKAGE_NAME = "javaPackage";

	private Source[] protoFiles;
	private File outputDir;
	private boolean failOnDuplicates = true;
	private boolean failOnMissingId = false;
	private String idOptionName;
	
	private final Properties properties = new Properties();

	public boolean isFailOnDuplicates() {
		return failOnDuplicates;
	}
	
	public boolean isFailOnMissingId() {
		return failOnMissingId;
	}
	
	public void setFailOnDuplicates(boolean failOnDuplicates) {
		this.failOnDuplicates = failOnDuplicates;
	}
	
	public void setFailOnMissingId(boolean failOnMissingId) {
		this.failOnMissingId = failOnMissingId;
	}
	
	public Properties getProperties() {
		return properties;
	}
	
	public String getProperty(String name, String def) {
		String val = properties.getProperty(name);
		return val == null ? def : val;
	}
	
	public Source[] getProtoFiles() {
		return protoFiles;
	}
	
	public void setProtoFiles(Source[] protoFiles) {
		this.protoFiles = protoFiles;
	}
	
	public void setProtoFiles(File[] files) {
		this.protoFiles = new Source[files.length];
		for (int i = 0; i < files.length; i++) {
			this.protoFiles[i] = new FileSource(files[i]);
		}
	}
	
	public File getOutputDir() {
		return outputDir;
	}
	
	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}

	public String getIdOptionName() {
		return idOptionName;
	}
	
	public void setIdOptionName(String idOptionName) {
		this.idOptionName = idOptionName;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
