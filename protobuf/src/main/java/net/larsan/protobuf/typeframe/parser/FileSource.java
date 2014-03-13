package net.larsan.protobuf.typeframe.parser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class FileSource implements Source {

	private final File file;

	public FileSource(File file) {
		this.file = file;
	}
	
	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public Reader getReader() throws IOException {
		return new FileReader(file);
	}
}
