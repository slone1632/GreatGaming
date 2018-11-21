package com.greatgaming.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class WelcomeConnection extends GameConnection {
    public WelcomeConnection(
            DataHandler dataHandler,
            ServerSocket serverSocket,
            ConnectionPool connectionPool) {
        super(dataHandler, serverSocket, connectionPool);
    }

    public void run() {
        boolean wait = true;
        while (wait) {
            try {
                Socket connectionSocket = this.server.accept();
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
        System.out.println("Welcome connection has exited");
    }
}
