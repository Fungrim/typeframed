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

public class ParserError extends RuntimeException {
	
	public static class Location {
		public final int index;
		public Location(int index) {
			this.index = index;
		}
	}

	private static final long serialVersionUID = 7024966705801752310L;
	
	private Location[] indexes;

	public ParserError(Location[] indexes) {
		this.indexes = indexes;	
	}
	
	public Location[] getIndexes() {
		return indexes;
	}
}

