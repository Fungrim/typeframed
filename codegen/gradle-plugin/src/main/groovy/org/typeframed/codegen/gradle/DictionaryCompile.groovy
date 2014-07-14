package org.typeframed.codegen.gradle

import org.typeframed.codegen.CodeGenerator
import org.typeframed.codegen.CodegenLogger;
import org.typeframed.codegen.Config
import org.typeframed.codegen.JavaCodeGenerator

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.SourceTask;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.compile.AbstractCompile;

class DictionaryCompile extends AbstractCompile {

	public void compile() {
		TypeframedConvention conf = project.convention.plugins.typeframed
		logger.debug "DictionaryCompile using properties: failOnDuplicates=${conf.failOnDuplicates}; failOnMissingId=${conf.failOnMissingId}; codegenPackage=${conf.codegenPackage}; typeIdName=${conf.typeIdName}"
		def config = new Config()
		config.setIdOptionName(conf.typeIdName)
		config.setCodegenPackage(conf.codegenPackage)
		config.setFailOnDuplicates(conf.failOnDuplicates)
		config.setFailOnMissingId(conf.failOnMissingId)
		config.setOutputDir(getDestinationDir())
		config.setProtoFiles(getSource().getFiles() as File[])
		logger.debug "Full codegenerator config: ${config}"
		JavaCodeGenerator generator = new JavaCodeGenerator(config, new ProjectLogger(project) as CodegenLogger)
		generator.generate()
	}
}
