package org.typeframed.codegen.maven;

import org.apache.maven.plugin.logging.Log;
import org.typeframed.codegen.CodegenLogger;

public class MavenLogger implements CodegenLogger {

	private final Log logger;
	
	public MavenLogger(Log logger) {
		this.logger = logger;
	}

	@Override
	public void debug(String arg0) {
		logger.debug(arg0);
	}

	@Override
	public void warn(String arg0) {
		logger.warn(arg0);
	}
}
