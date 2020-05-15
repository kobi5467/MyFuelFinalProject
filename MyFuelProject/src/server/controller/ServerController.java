package server.controller;

import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

public class ServerController extends AbstractServer {

	public ServerController(int port) {
		super(port);
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {

	}

}
