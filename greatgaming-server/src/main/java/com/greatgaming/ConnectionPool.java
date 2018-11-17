package com.greatgaming;

import java.io.*;
import java.net.*;
import java.util.*;

public class ConnectionPool {
	public static int WELCOME_PORT = 6789;
	private Integer maxClients;
	private Stack<Integer> availabilePorts;
	private Map<Connection, Integer> portsInUse;
	
	public ConnectionPool(int maxClients){
		this.maxClients = maxClients;
		this.portsInUse = new HashMap<Connection, Integer>();
		this.availabilePorts = new Stack<Integer>();
		
		for (int i = 1; i < maxClients +1; i++) {
			this.availabilePorts.push(WELCOME_PORT + i);
		}
	}
	
	public Integer startPersistentClientConnection(DataHandler dataHandler) throws IOException {
		Integer port = this.availabilePorts.pop();
		
		System.out.println("Spinning up a connection on port " + port.toString());
		
		ServerSocket socket = new ServerSocket(port);
		Connection connection = new Connection(dataHandler, socket, this);
		Thread thread = new Thread(connection);
		thread.start();
		
		this.portsInUse.put(connection, port);
		
		return port;
	}
	
	public void connectionClosed(Connection connection) {
		Integer portInUse = this.portsInUse.get(connection);
		this.portsInUse.remove(connection);
		this.availabilePorts.push(portInUse);
		System.out.println("Terminating connection on port " + portInUse.toString());
	}
}