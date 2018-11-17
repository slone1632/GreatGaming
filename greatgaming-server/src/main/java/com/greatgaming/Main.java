package com.greatgaming;

import java.io.*;
import java.net.*;

public class Main {
	public static void main(String[] args) throws IOException {
		String clientSentence;
		String capitalizedSentence;
		ServerSocket welcomeSocket = new ServerSocket(6789);

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			BufferedReader inFromClient =
			new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			
			try {
				clientSentence = inFromClient.readLine();
				System.out.println("Received: " + clientSentence);
			
				capitalizedSentence = clientSentence.toUpperCase() + System.lineSeparator();
			
				outToClient.writeBytes(capitalizedSentence);
			} catch (SocketException ex) {
				System.out.println("Client reset connection");
			}
		}
	}
}
