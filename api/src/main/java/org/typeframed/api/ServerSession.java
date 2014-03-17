package org.typeframed.api;

import java.util.UUID;
import java.util.concurrent.Future;

public interface ServerSession extends NetworkSession {

	public UUID getId();
	
	public Future<Boolean> disconnect();
	
}
