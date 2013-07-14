package net.larsan.protobuf.typeframe.codegen;

import java.io.File;
import java.util.Properties;

public class Config {
	
	public static final String JAVA_PACKAGE_NAME = "javaPackage";

	private File[] protoFiles;
	private File outputDir;
	
	private final Properties properties = new Properties();
	
	private String idOptionName;
	
	public Properties getProperties() {
		return properties;
	}
	
	public String getProperty(String name, String def) {
		String val = properties.getProperty(name);
		return val == null ? def : val;
	}
	
	public File[] getProtoFiles() {
		return protoFiles;
	}
	
	public void setProtoFiles(File[] protoFiles) {
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
