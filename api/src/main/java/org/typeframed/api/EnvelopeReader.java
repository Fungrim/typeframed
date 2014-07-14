package org.typeframed.api;

import java.io.IOException;

/**
 * The envelope reader is an object that can read
 * envelopes from a specified source. 
 *
 * @author Lars J. Nilsson
 * @param <H> Envelope header type
 */
public interface EnvelopeReader<H> {

	/**
	 * Read an envelope from the underlying source.
	 * 
	 * @return An envelope, never null
	 * @throws IOException On IO errors
	 */
	public Envelope<H> read() throws IOException;
	
}
