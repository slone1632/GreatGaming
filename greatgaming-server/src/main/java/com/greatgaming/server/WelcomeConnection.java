package com.greatgaming.server;

import com.greatgaming.comms.messages.Chat;
import com.greatgaming.comms.messages.LoginRequest;
import com.greatgaming.comms.messages.LoginResponse;
import com.greatgaming.comms.serialization.Serializer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class WelcomeConnection extends GameConnection {
    public WelcomeConnection(
            ServerSocket serverSocket,
            ConnectionPool connectionPool,
            Serializer serializer) {
        super("", serverSocket, connectionPool, serializer);
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
                Object message = this.serializer.deserialize(clientInput);
                if (message instanceof LoginRequest) {
                    LoginRequest request = (LoginRequest)message;
                    Integer port = this.connectionPool.startPersistentClientConnection(request.username);


                    Chat logoutNotification = new Chat();
                    logoutNotification.message = request.username + " has joined the chat";
                    connectionPool.sendBroadcastMessage(logoutNotification);

                    LoginResponse response = new LoginResponse();
                    response.gamePort = port;
                    response.succeeded = true;

                    String payload = this.serializer.serialize(LoginResponse.class, response);
                    payload = payload + System.lineSeparator();

                    outToClient.writeBytes(payload);
                }
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
