/**
 * Copyright 2014 Lars J. Nilsson <contact@larsan.net>
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
 * The header provider is used to parse and read header
 * bytes. Currently it works on byte arrays only.
 *
 * @author Lars J. Nilsson
 * @param <T> The header type
 */
public interface HeaderProvider<T> {

	public byte[] toBytes(T head) throws IOException;

	public T fromBytes(byte[] bytes);
	
}
