package client.controller;

import ocsf.client.AbstractClient;

public class ClientController extends AbstractClient {

	public ClientController(String host, int port) {
		super(host, port);
	}

	@Override
	protected void handleMessageFromServer(Object msg) {

	}

}
