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

import java.io.IOException;
import java.util.Set;

import org.typeframed.api.MessageTypeDictionary;

public interface DictionaryParser {
	
	/**
	 * Generate a type dictionary from a set of source files. 
	 * 
	 * @param protoFiles Source files, never null
	 * @return A type dictionary mapping ID to types, never null
	 * @throws IOException On IO errors
	 * @throws ClassNotFoundException
	 */
	public MessageTypeDictionary parseDictionary(Source[] protoFiles) throws IOException, ClassNotFoundException;
	
	/**
	 * Parse a set of message descriptors from a set of source files. This
	 * is what the code generators will use in order to correctly handle the
	 * message types.
	 * 
	 * @param protoFiles Source files, never null
	 * @return A set of message descriptors, never null
	 * @throws IOException On IO errors
	 */
	public Set<MessageDescriptor> parseMessageDescriptors(Source[] protoFiles) throws IOException;
	
}
