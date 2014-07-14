/**
 * Copyright 2014 Lars J. Nilsson <contact@larsan.net>
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
package org.typeframed.codegen;

import org.typeframed.api.codegen.DuplicateIdException;
import org.typeframed.api.codegen.MissingIdException;
import org.typeframed.protobuf.parser.ErrorHandler;

public class ConfigErrorHandler implements ErrorHandler {
	
	private final boolean failOnDuplicates;
	private final boolean failOnMissingId;
	
	private final CodegenLogger logger;

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
