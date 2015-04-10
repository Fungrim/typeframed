/*
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
