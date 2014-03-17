package org.typeframed.protobuf.codegen.gradle

import org.typeframed.protobuf.codegen.CodeGenerator
import org.typeframed.protobuf.codegen.CodegenLogger;
import org.typeframed.protobuf.codegen.Config
import org.typeframed.protobuf.codegen.JavaCodeGenerator

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.SourceTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.compile.AbstractCompile;

class DictionaryCompile extends AbstractCompile {

	public void compile() {
		TypeframeConvention conf = project.convention.plugins.typeframe
		logger.debug "DictionaryCompile using properties: failOnDuplicates=${conf.failOnDuplicates}; failOnMissingId=${conf.failOnMissingId}; javaPackage=${conf.javaPackage}; typeIdName=${conf.typeIdName}"
		def config = new Config()
		config.setIdOptionName(conf.typeIdName)
		config.getProperties().setProperty(Config.JAVA_PACKAGE_NAME, conf.javaPackage)
		config.setFailOnDuplicates(conf.failOnDuplicates)
		config.setFailOnMissingId(conf.failOnMissingId)
		config.setOutputDir(getDestinationDir())
		config.setProtoFiles(getSource().getFiles() as File[])
		logger.debug "Full codegenerator config: ${config}"
		JavaCodeGenerator generator = new JavaCodeGenerator(config)
		generator.setCodegenLogger(new ProjectLogger(project) as CodegenLogger)
		generator.generate()
	}
}
