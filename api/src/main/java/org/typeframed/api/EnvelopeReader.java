package org.typeframed.api;

import java.io.IOException;

public interface EnvelopeReader<H> {

	public Envelope<H> read() throws IOException;
	
}
