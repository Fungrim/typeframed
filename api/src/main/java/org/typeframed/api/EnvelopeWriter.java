package org.typeframed.api;

import java.io.IOException;

/**
 * The envelope writer know how to write envelopes,
 * including headers to an underlying source.
 *
 * @author Lars J. Nilsson
 * @param <H> The envelope header type
 */
public interface EnvelopeWriter<H> {

	/**
	 * Write envelope to underlying source.
	 * 
	 * @param env Envelope to write, never null
	 * @throws IOException ONn IO errors
	 */
	public void write(Envelope<H> env) throws IOException;
	
}
