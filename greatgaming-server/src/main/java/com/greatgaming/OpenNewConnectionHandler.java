package com.greatgaming;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class OpenNewConnectionHandler extends DataHandler {
	private ConnectionPool connectionPool;
	private List<MessagingHandler> handlerList = new ArrayList<MessagingHandler>();
	
	public OpenNewConnectionHandler(ConnectionPool pool) {
		this.connectionPool = pool;
	}
	@Override
	public String handleData(String data){
		try {
			MessagingHandler newHandler = new MessagingHandler();
			for (MessagingHandler handler : handlerList) {
				handler.addParticipant(newHandler);
				newHandler.addParticipant(handler);
			}
			handlerList.add(newHandler);

			Integer port = this.connectionPool.startPersistentClientConnection(newHandler);
			
			return port.toString();
		} catch (IOException ex) {
			ex.printStackTrace(System.out);
			return "";
		}
	}
}
