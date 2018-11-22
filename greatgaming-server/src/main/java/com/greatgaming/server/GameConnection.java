package com.greatgaming.server;

import com.greatgaming.comms.messages.*;
import com.greatgaming.comms.serialization.Serializer;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;

public class GameConnection implements Runnable {
	protected ConnectionPool connectionPool;
	protected ServerSocket server;
	protected Socket serverSocket;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	protected Serializer serializer;
	private Queue<MessageClassPair> outputMessages;
	private String userName;
	
	public GameConnection(
			String userName,
			ServerSocket server,
			ConnectionPool connectionPool,
			Serializer serializer) {
		this.userName = userName;
		this.server = server;
		this.connectionPool = connectionPool;
		this.outputMessages= new LinkedList<>();
		this.serializer = serializer;
	}
	private class MessageClassPair{
		public Class clazz;
		public Object message;
	}

	public <T> void sendMessage(Class<T> clazz, Object message) {
		MessageClassPair pair = new MessageClassPair();
		pair.clazz = clazz;
		pair.message = message;
		this.outputMessages.add(pair);
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
					Object messge = this.serializer.deserialize(clientInput);
					if (messge instanceof DisconnectRequest) {
						shouldKeepConnectionOpen = false;
						sendMessage(DisconnectResponse.class, new DisconnectResponse());
						Chat logoutNotification = new Chat();
						logoutNotification.message = userName + " has left the game";
						connectionPool.sendBroadcastMessage(logoutNotification);
						System.out.println("Client closed connection");
					} else if (messge instanceof Chat) {
						Chat chat = (Chat)messge;
						chat.message = userName + ": " + chat.message;
						connectionPool.sendBroadcastMessage(chat);
					} else if (messge instanceof HeartbeatRequest) {
						HeartbeatAcknowledge ack = new HeartbeatAcknowledge();
						ack.isAlive = true;
						sendMessage(HeartbeatAcknowledge.class, ack);
					}
				}
				while  (this.outputMessages.peek() != null) {
					MessageClassPair message = this.outputMessages.poll();
					String payload = this.serializer.serialize(message.clazz, message.message);
					this.outToClient.writeBytes(payload + System.lineSeparator());
					this.outToClient.flush();
				}
				Thread.sleep(10);
			} catch (IOException | InterruptedException ex) {
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
