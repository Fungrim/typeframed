package org.typeframed.codegen;

import org.apache.log4j.Logger;

public class Log4jCodegenLogger implements CodegenLogger {

	private final Logger log;

	public Log4jCodegenLogger() {
		this(Logger.getLogger(Log4jCodegenLogger.class));
	}
	
	public Log4jCodegenLogger(Logger log) {
		this.log = log;
	}
	
	@Override
	public void warn(String msg) {
		log.warn(msg);
	}

	@Override
	public void debug(String msg) {
		log.debug(msg);
	}
}
