package org.typeframed.api;

import java.util.UUID;

public interface ServerHandler {

	public void connected(ServerSession session);
	
	public void disconnected(UUID session);
	
}
