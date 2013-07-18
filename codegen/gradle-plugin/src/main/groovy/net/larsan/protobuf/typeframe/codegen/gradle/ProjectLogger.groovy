package net.larsan.protobuf.typeframe.codegen.gradle

import org.gradle.api.Project;

import net.larsan.protobuf.typeframe.codegen.CodegenLogger


class ProjectLogger implements CodegenLogger {

	private Project project;

	public ProjectLogger(Project project) {
		this.project = project;
	}
	
	@Override
	public void warn(String msg) {
		project.logger.warn msg
	}

	@Override
	public void debug(String msg) {
		project.logger.debug msg
	}
}
