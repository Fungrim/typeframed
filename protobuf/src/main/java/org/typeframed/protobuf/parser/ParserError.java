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

import java.util.Arrays;

public class ParserError extends RuntimeException {
	
	public static class Error {
		public final int startIndex;
		public final String msg; 
		public final int endIndex;
		public Error(String msg, int startIndex, int endIndex) {
			this.msg = msg;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
		}
		@Override
		public String toString() {
			return "Message: " + msg + " (location: " + startIndex + "->" + endIndex + ")";
		}
	}

	private static final long serialVersionUID = 7024966705801752310L;
	
	private Error[] indexes;

	public ParserError(Error[] indexes) {
		super(toMessage(indexes));
		this.indexes = indexes;	
	}
	
	private static String toMessage(Error[] indexes) {
		return Arrays.toString(indexes);
	}

	public Error[] getIndexes() {
		return indexes;
	}
}

