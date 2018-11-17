package com.greatgaming;

import java.io.*;
import java.net.*;

public class WelcomeConnection extends Connection {
	public WelcomeConnection(
			DataHandler handler,
			ServerSocket socket,
			ConnectionPool connectionPool) {
		super(handler, socket, connectionPool);
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("Listening for incoming clients");
				Socket connectionSocket = this.socket.accept();
				BufferedReader inFromClient =
					new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				
				handleInput(inFromClient, outToClient);
			} catch (Exception ex) {
				System.out.println("Welcome connection closed");
			}
		}
	}
}
