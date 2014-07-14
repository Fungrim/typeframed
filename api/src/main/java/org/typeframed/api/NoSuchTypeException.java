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
package org.typeframed.api;

/**
 * This is an illegal argument exception to indicate that
 * the system cannot find a type for a given ID.
 * 
 * @author Lars J. Nilsson
 */
public class NoSuchTypeException extends IllegalArgumentException {

	private static final long serialVersionUID = 8663537933223095321L;

	private int typeId;

	/**
	 * @param typeId The type ID that had no match
	 */
	public NoSuchTypeException(int typeId) {
		this.typeId = typeId;
	}
	
	/**
	 * @return The type ID that had no match
	 */
	public int getTypeId() {
		return typeId;
	}
}
