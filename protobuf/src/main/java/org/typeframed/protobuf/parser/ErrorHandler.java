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
package org.typeframed.protobuf.parser;

import org.typeframed.api.codegen.DuplicateIdException;
import org.typeframed.api.codegen.MissingIdException;

public interface ErrorHandler {

	public void typeMissingId(String name) throws MissingIdException;

	public void duplicateId(int id, String oldName, String newName) throws DuplicateIdException;

}
