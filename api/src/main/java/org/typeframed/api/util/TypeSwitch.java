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
