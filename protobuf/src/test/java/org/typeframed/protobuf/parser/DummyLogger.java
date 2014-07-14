package org.typeframed.protobuf.parser;

public class DummyLogger implements ParserLogger {

	@Override
	public void debug(String msg) {
		System.out.println("DEBUG: " + msg);
	}

	@Override
	public void info(String msg) {
		System.out.println("INFO: " + msg);
	}

	@Override
	public void warn(String msg) {
		System.out.println("WARN: " + msg);
	}

}
