package net.larsan.protobuf.typeframe.codegen;

import java.io.File;
import java.util.Properties;

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
}
