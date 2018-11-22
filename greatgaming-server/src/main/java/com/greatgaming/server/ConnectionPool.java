package com.greatgaming.server;

import com.greatgaming.comms.messages.Chat;

import java.io.*;
import java.util.*;

public class ConnectionPool {
	public static int WELCOME_PORT = 6789;
	private ConnectionFactory connectionFactory;
	private Stack<Integer> availabilePorts;
	private Map<GameConnection, Integer> portsInUse;
	
	public ConnectionPool(int maxClients, ConnectionFactory factory){
		this.connectionFactory = factory;
		this.portsInUse = new HashMap<GameConnection, Integer>();
		this.availabilePorts = new Stack<Integer>();
		
		for (int i = 1; i < maxClients +1; i++) {
			this.availabilePorts.push(WELCOME_PORT + i);
		}
	}
	
	public synchronized Integer startPersistentClientConnection(String username) throws IOException {
		Integer port = this.availabilePorts.pop();

		System.out.println("Spinning up a connection on port " + port.toString());

		GameConnection connection = this.connectionFactory.build(
				username,
				port,
				this
		);
		this.portsInUse.put(connection, port);

		connectionFactory.openConection(connection);

		return port;
	}

	public void sendBroadcastMessage(Chat chat) {
		System.out.println("Broadcasting: " + chat);
		for (GameConnection gameConnection : this.portsInUse.keySet()) {
			gameConnection.sendMessage(Chat.class, chat);
		}
	}
	
	public void connectionClosed(GameConnection connection) {
		Integer portInUse = this.portsInUse.get(connection);
		this.portsInUse.remove(connection);
		this.availabilePorts.push(portInUse);
		System.out.println("Terminating connection on port " + portInUse);
	}
}