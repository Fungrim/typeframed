package org.typeframed.api;

import java.io.IOException;

public interface EnvelopeWriter<H> {

	public void write(Envelope<H> env) throws IOException;
	
}
