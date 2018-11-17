package com.greatgaming;

import java.io.*;
import java.net.*;
import java.util.*;

public class Connection implements Runnable {
	protected DataHandler dataHandler;
	protected ConnectionPool connectionPool;
	protected ServerSocket socket;
	protected static final String TERMINATE_CONNECTION = "KILL_CONN";
	
	public Connection(
			DataHandler dataHandler,
			ServerSocket socket,
			ConnectionPool connectionPool) {
		this.dataHandler = dataHandler;
		this.socket = socket;
		this.connectionPool = connectionPool;
	}
	
	@Override
	public void run() {
		boolean wait = true;
		Socket connectionSocket = null;
		while (wait) {
			try {
				connectionSocket = this.socket.accept();
				BufferedReader inFromClient =
					new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				
				String clientInput = inFromClient.readLine();
					
				String handlerOutput = this.dataHandler.handleData(clientInput);
				handlerOutput = handlerOutput + System.lineSeparator();
			
				outToClient.writeBytes(handlerOutput);	

				if (clientInput.equals("null")) {
					wait = false;
					System.out.println("NULL STUFF");
				}				
			} catch (IOException ex) {
				System.out.println("ERROR");
				wait = false;
			}
		}
		System.out.println("Exited");
		try {
			connectionSocket.close();
			this.socket.close();
		} catch (IOException ex) {
			System.out.println("Failed to close connection");
		}
		this.connectionPool.connectionClosed(this);
		
	}
}
