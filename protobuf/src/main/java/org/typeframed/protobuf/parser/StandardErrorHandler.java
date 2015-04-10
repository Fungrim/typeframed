/**
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
package org.typeframed.protobuf.parser;

import org.typeframed.api.codegen.DuplicateIdException;

public class StandardErrorHandler implements ErrorHandler {

	private ParserLogger log;

	public StandardErrorHandler(ParserLogger log) {
		this.log = log;
	}

	@Override
	public void typeMissingId(String name) {
		log.warn("Did not find any type ID in message '" + name + "'");	
	}
	
	@Override
	public void duplicateId(int id, String oldName, String newName) throws DuplicateIdException {
		throw new DuplicateIdException(id, oldName, newName);	
	}
}
