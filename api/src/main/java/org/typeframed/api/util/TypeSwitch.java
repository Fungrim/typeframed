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
package org.typeframed.api.util;

import com.google.protobuf.Message;

/**
 * The type switch is used by the code generator as
 * a base class for a type which forwards concrete
 * messages to a target interface. It is auto-generated
 * as a convenience.
 *
 * @author Lars J. Nilsson
 */
public abstract class TypeSwitch {
	
	/**
	 * Marker interface for which will be extended by
	 * the code generator to take "handle" methods for 
	 * concrete message types.
	 * 
	 * @author Lars J. Nilsson
	 */
	public static interface Target { }

	/**
	 * Forward a message to the target listener. The switch will
	 * determine the exact message type and cast it before it is
	 * given to the listener. 
	 * 
	 * @param msg The message to forward, never null
	 */ 
	protected abstract void forward(Message msg);
	
}
