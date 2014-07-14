package org.typeframed.protobuf.parser;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.lang3.builder.ToStringBuilder;

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
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);
	}
}
