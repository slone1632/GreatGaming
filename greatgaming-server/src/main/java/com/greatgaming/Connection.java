package com.greatgaming;

import java.io.*;
import java.net.*;
import java.util.*;

public class Connection implements Runnable {
	protected DataHandler dataHandler;
	protected ConnectionPool connectionPool;
	protected ServerSocket socket;
	public static final String DISCONNECT_STRING = "TCENNOCSID";
	
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

				if (clientInput.contains(DISCONNECT_STRING)) {
					wait = false;
					System.out.println("Client closed connection");
				}
				connectionSocket.close();
			} catch (IOException ex) {
				System.out.println("ERROR");
				wait = false;
				System.out.println(ex.getMessage());
				for (StackTraceElement line : ex.getStackTrace()) {
					System.out.println(line.toString());
				}
			}
		}
		System.out.println("Exited");
		try {
			this.socket.close();
		} catch (IOException ex) {
			System.out.println("Failed to close connection");
		}
		this.connectionPool.connectionClosed(this);
		
	}
}
