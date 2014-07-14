/**
 * Copyright 2014 Lars J. Nilsson <contact@larsan.net>
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

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.File;
import java.util.Properties;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.typeframed.protobuf.parser.FileSource;
import org.typeframed.protobuf.parser.Source;

public class Config {
	
	private Source[] protoFiles;
	private File outputDir;
	private boolean failOnDuplicates = true;
	private boolean failOnMissingId = false;
	private String idOptionName;
	private String codegenPackage;
	
	private final Properties properties = new Properties();

	public String getCodegenPackage() {
		return codegenPackage;
	}
	
	public void setCodegenPackage(String codegenPackage) {
		this.codegenPackage = codegenPackage;
	}
	
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
		return ToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);
	}
}
