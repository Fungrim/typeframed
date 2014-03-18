package org.typeframed.api.server;

import java.util.UUID;
import java.util.concurrent.Future;

import org.typeframed.api.NetworkSession;

public interface ServerSession extends NetworkSession {

	public UUID getId();
	
	public Future<Boolean> disconnect();
	
}
