package com.greatgaming;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class OpenNewConnectionHandler extends DataHandler {
	private ConnectionPool connectionPool;

	public OpenNewConnectionHandler(ConnectionPool pool) {
		this.connectionPool = pool;
	}
	@Override
	public String handleData(String data){
		try {
			MessagingHandler newHandler = new MessagingHandler(connectionPool);
			Integer port = this.connectionPool.startPersistentClientConnection(newHandler);
			return port.toString();
		} catch (IOException ex) {
			ex.printStackTrace(System.out);
			return "";
		}
	}
}
