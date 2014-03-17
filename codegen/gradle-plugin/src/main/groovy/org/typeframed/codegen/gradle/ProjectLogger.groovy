package org.typeframed.codegen.gradle

import org.gradle.api.Project;

import org.typeframed.codegen.CodegenLogger


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
