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
