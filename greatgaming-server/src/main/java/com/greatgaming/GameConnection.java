package com.greatgaming;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

public class GameConnection implements Runnable {
	protected DataHandler dataHandler;
	protected ConnectionPool connectionPool;
	protected ServerSocket server;
	protected Socket serverSocket;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	public static final String DISCONNECT_STRING = "TCENNOCSID";
	private Queue<String> outputMessages;
	
	public GameConnection(
			DataHandler dataHandler,
			ServerSocket server,
			ConnectionPool connectionPool) {
		this.dataHandler = dataHandler;
		this.server = server;
		this.connectionPool = connectionPool;
		this.outputMessages= new LinkedList<String>();
	}

	public void sendMessage(String message) {
		this.outputMessages.add(message);
	}

	private Boolean openSocket(int numRetries) {
		if (numRetries == 0) {
			serverSocket = null;
			return false;
		}
		try {
			serverSocket = this.server.accept();
			serverSocket.setKeepAlive(true);
			inFromClient = new BufferedReader(new InputStreamReader(this.serverSocket.getInputStream()));
			outToClient = new DataOutputStream(this.serverSocket.getOutputStream());
			return true;
		} catch (IOException ex) {
			openSocket(numRetries - 1);
		}
		return false;
	}
	
	public void run() {
		boolean shouldKeepConnectionOpen = true;
		openSocket(3);
		while (shouldKeepConnectionOpen) {
			try {
				if (this.inFromClient.ready()) {
					String clientInput = this.inFromClient.readLine();
					String handlerOutput = this.dataHandler.handleData(clientInput);
					if (handlerOutput != null) {
						this.outputMessages.add(handlerOutput);
					}

					if (clientInput.contains(DISCONNECT_STRING)) {
						shouldKeepConnectionOpen = false;
						System.out.println("Client closed connection");
					}

				}
				while  (this.outputMessages.peek() != null) {
					String message = this.outputMessages.poll();
					this.outToClient.writeBytes(message + System.lineSeparator());
					this.outToClient.flush();
				}
			} catch (IOException ex) {
				Boolean wasAbleToReopenConnection = openSocket(3);
				if (!wasAbleToReopenConnection) {
					shouldKeepConnectionOpen = false;
					System.out.println("ERROR");
					System.out.println(ex.getMessage());
					for (StackTraceElement line : ex.getStackTrace()) {
						System.out.println(line.toString());
					}
				}
			}
		}
		System.out.println("Exited");
		closeConnection();
	}

	private void closeConnection() {
		try {
			this.server.close();
		} catch (IOException ex) {
			System.out.println("Failed to close connection");
		}
		this.connectionPool.connectionClosed(this);
	}
}
