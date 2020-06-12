package server.controller;

import server.dbLogic.DBConnector;

public class SystemManager {

	
	private DBConnector db;
	
	public SystemManager(DBConnector db) {
		this.db = db;		
	}
	
}
