package com.greatgaming.server;

import com.greatgaming.comms.serialization.Serializer;

import java.io.IOException;
import java.net.ServerSocket;

public class ConnectionFactory {
    private static final Serializer serializer = new Serializer();
    public GameConnection build(
            String username,
            Integer port,
            ConnectionPool pool) throws IOException {
        ServerSocket socket = new ServerSocket(port);
        return new GameConnection(username, socket, pool, serializer);
    }

    public WelcomeConnection buildWelcomeConnection(ConnectionPool pool) throws IOException {
        ServerSocket socket = new ServerSocket(ConnectionPool.WELCOME_PORT);
        return new WelcomeConnection(socket, pool, serializer);
    }


    public void openConection(GameConnection connection) {
        Thread thread = new Thread(connection);
        thread.start();
    }
}
