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
