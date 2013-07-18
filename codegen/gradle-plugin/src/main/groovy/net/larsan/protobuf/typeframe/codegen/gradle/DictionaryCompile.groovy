package net.larsan.protobuf.typeframe.codegen.gradle

import net.larsan.protobuf.typeframe.codegen.CodeGenerator
import net.larsan.protobuf.typeframe.codegen.CodegenLogger;
import net.larsan.protobuf.typeframe.codegen.Config
import net.larsan.protobuf.typeframe.codegen.JavaCodeGenerator

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
