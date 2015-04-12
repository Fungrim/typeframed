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

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.internal.file.DefaultSourceDirectorySet
import org.gradle.api.tasks.SourceSet;

class TypeframedPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		project.convention.plugins.typeframed = new TypeframedConvention(project)
		project.sourceSets.all { SourceSet sourceSet ->
			def generateJavaTaskName = sourceSet.getTaskName('generate', 'dictionary')
			DictionaryCompile compileTask = project.tasks.create(generateJavaTaskName, DictionaryCompile)
			configureForSourceSet(project, sourceSet, compileTask)
			String compileJavaTaskName = sourceSet.getCompileTaskName("java");
			Task compileJavaTask = project.tasks.getByName(compileJavaTaskName);
			compileJavaTask.dependsOn(compileTask)
		}
	}
	
	void configureForSourceSet(Project project, final SourceSet sourceSet, DictionaryCompile compile) {
		def final defaultSource = new DefaultSourceDirectorySet("${sourceSet.displayName} Protobuf Source", project.fileResolver);
		defaultSource.include("**/*.proto")
		defaultSource.filter.include("**/*.proto")
		defaultSource.srcDir("src/${sourceSet.name}/proto")
		compile.conventionMapping.map("classpath") {
			return sourceSet.getCompileClasspath()
		}
		compile.conventionMapping.map('source') {
			return defaultSource
		}
		compile.conventionMapping.map('destinationDir') {
			return new File(getGeneratedSourceDir(project, sourceSet))
		}
	}
	
	String getGeneratedSourceDir(Project project, SourceSet sourceSet) {
		def generatedSourceDir = "${project.buildDir}/generated-sources"
		return "${generatedSourceDir}/${sourceSet.name}"
	}
}
