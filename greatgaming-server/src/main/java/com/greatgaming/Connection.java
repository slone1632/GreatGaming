package com.greatgaming;

import java.io.*;
import java.net.*;
import java.util.*;

public class Connection implements Runnable {
	protected DataHandler dataHandler;
	protected ConnectionPool connectionPool;
	protected ServerSocket socket;
	
	public Connection(
			DataHandler dataHandler,
			ServerSocket socket,
			ConnectionPool connectionPool) {
		this.dataHandler = dataHandler;
		this.socket = socket;
		this.connectionPool = connectionPool;
	}
	
	protected void handleInput(BufferedReader inFromClient,
		DataOutputStream outToClient) throws IOException {
		String clientInput = inFromClient.readLine();
			
		String handlerOutput = this.dataHandler.handleData(clientInput);
		handlerOutput = handlerOutput + System.lineSeparator();
	
		outToClient.writeBytes(handlerOutput);
	}
	
	@Override
	public void run(){
		Socket connectionSocket;
		BufferedReader inFromClient;
		DataOutputStream outToClient;
		
		try{
			System.out.println("Opening dedicated connection for client");
			connectionSocket = this.socket.accept();
			inFromClient =
				new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		} catch (IOException ex) {
			throw new RuntimeException();
		}

		while (true) {
			try {
				System.out.println("Received input from client");
				handleInput(inFromClient, outToClient);
			} catch (Exception ex) {
				System.out.println("Closing connection");
				try {
					this.socket.close();
				} catch (IOException ex2) {
					throw new RuntimeException();
				}
				this.connectionPool.connectionClosed(this);
				break;
			}
		}
	}
}
