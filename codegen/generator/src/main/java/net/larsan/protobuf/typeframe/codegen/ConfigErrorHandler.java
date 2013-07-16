package net.larsan.protobuf.typeframe.codegen;

import net.larsan.protobuf.typeframe.parser.StandardErrorHandler;

import org.apache.log4j.Logger;

public class ConfigErrorHandler extends StandardErrorHandler {
	
	private final boolean failOnDuplicates;
	private final boolean failOnMissingId;

	public ConfigErrorHandler(Config config) {
		this(config.isFailOnDuplicates(), config.isFailOnMissingId());
	}

	public ConfigErrorHandler(boolean failOnDuplicates, boolean failOnMissingId) {
		super(Logger.getLogger(ConfigErrorHandler.class));
		this.failOnDuplicates = failOnDuplicates;
		this.failOnMissingId = failOnMissingId;
	}

	@Override
	public void typeMissingId(String name) throws MissingIdException {
		if(failOnMissingId) {
			throw new MissingIdException(name);
		} else {
			super.typeMissingId(name);
		}
	}

	@Override
	public void duplicateId(int id, String oldName, String newName) throws DuplicateIdException {
		if(!failOnDuplicates) {
			log.warn("Message ID duplication! Old type '" + oldName + "' is overwritten by '" + newName + "' for ID " + id);
		} else {
			super.duplicateId(id, oldName, newName);
		}
	}

}
