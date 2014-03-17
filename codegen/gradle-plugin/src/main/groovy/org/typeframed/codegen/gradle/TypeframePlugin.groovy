package org.typeframed.codegen.gradle

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.internal.file.DefaultSourceDirectorySet
import org.gradle.api.tasks.SourceSet;

class TypeframePlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
//		apply plugin: 'java'
		project.convention.plugins.typeframe = new TypeframeConvention(project)
		project.sourceSets.all { SourceSet sourceSet ->
			def generateJavaTaskName = sourceSet.getTaskName('generate', 'dictionary')
			DictionaryCompile compileTask = project.tasks.create(generateJavaTaskName, DictionaryCompile)
			configureForSourceSet(project, sourceSet, compileTask)
//			sourceSet.java.srcDir getGeneratedSourceDir(project, sourceSet)
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
