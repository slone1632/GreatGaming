package com.greatgaming;

import java.io.*;
import java.net.*;

public class OpenNewConnectionHandler extends DataHandler {
	private ConnectionPool connectionPool;
	
	public OpenNewConnectionHandler(ConnectionPool pool) {
		this.connectionPool = pool;
	}
	@Override
	public String handleData(String data){
		if (port == null) {
			System.out.println("Connection was closed");
			return "Goodbye";
		}
		
		try {
			Integer port = this.connectionPool.startPersistentClientConnection(new ConsoleHandler());
			
			return port.toString();
		} catch (IOException ex) {
			throw new RuntimeException();
		}
	}
}
