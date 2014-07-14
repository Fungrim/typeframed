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

import org.typeframed.protobuf.parser.node.OptionNode;

public class Utilities {
	
	public static String getRootClassFromFile(Source file) {
		boolean nextUpper = true;
		StringBuilder b = new StringBuilder();
		String name = file.getName();
		int i = name.lastIndexOf('.');
		if(i != -1) {
			name = name.substring(0, i);
		}
		for (char ch : name.toCharArray()) {
			if(nextUpper) {
				ch = Character.toUpperCase(ch);
				nextUpper = false;
			}
			if(ch == '_') {
				nextUpper = true;
			} else {
				b.append(ch);
			}
		}
		return b.toString();
	}
	
	public static String getOption(OptionsHolder root, String name, boolean unquoteString) {
		for (OptionNode node : root.getOptions()) {
			if(node.getName().equals(name)) {
				return (unquoteString ? unquoteString(node.getValue()) : node.getValue());
			}
		}
		return null;
	}

	private static String unquoteString(String value) {
		if(value.startsWith("\"") && value.endsWith("\"")) {
			value = value.substring(0, value.length() - 1).substring(1);
		}
		return value;
	}

	public static boolean hasOption(OptionsHolder root, String name) {
		return getOption(root, name, false) != null;
	}
}
