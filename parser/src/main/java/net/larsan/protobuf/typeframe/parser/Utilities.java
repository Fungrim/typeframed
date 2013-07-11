package net.larsan.protobuf.typeframe.parser;

import java.io.File;

import net.larsan.protobuf.typeframe.parser.node.OptionNode;


public class Utilities {

	public static String getRootClassFromFile(File file) {
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
