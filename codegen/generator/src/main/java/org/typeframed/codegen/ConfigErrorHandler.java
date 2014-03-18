package org.typeframed.codegen;

import org.apache.log4j.Logger;
import org.typeframed.api.DuplicateIdException;
import org.typeframed.api.MissingIdException;
import org.typeframed.protobuf.parser.ErrorHandler;

public class ConfigErrorHandler implements ErrorHandler {
	
	private final boolean failOnDuplicates;
	private final boolean failOnMissingId;
	
	private final CodegenLogger logger;

	public ConfigErrorHandler(Config config) {
		this(config, new CodegenLogger() {
			
			private final Logger log = Logger.getLogger(JavaCodeGenerator.class);
			
			@Override
			public void warn(String msg) {
				log.warn(msg);
			}
			
			@Override
			public void debug(String msg) {
				log.debug(msg);
			}
		});
	}
	
	public ConfigErrorHandler(Config config, CodegenLogger logger) {
		this(config.isFailOnDuplicates(), config.isFailOnMissingId(), logger);
	}

	public ConfigErrorHandler(boolean failOnDuplicates, boolean failOnMissingId, CodegenLogger logger) {
		this.failOnDuplicates = failOnDuplicates;
		this.failOnMissingId = failOnMissingId;
		this.logger = logger;
	}

	@Override
	public void typeMissingId(String name) throws MissingIdException {
		if(failOnMissingId) {
			throw new MissingIdException(name);
		} else {
			logger.warn("Did not find any type ID in message '" + name + "'");	
		}
	}

	@Override
	public void duplicateId(int id, String oldName, String newName) throws DuplicateIdException {
		if(!failOnDuplicates) {
			logger.warn("Message ID duplication! Old type '" + oldName + "' is overwritten by '" + newName + "' for ID " + id);
		} else {
			throw new DuplicateIdException(id, oldName, newName);	
		}
	}

}
