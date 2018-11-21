package com.greatgaming.server;

import java.io.IOException;
import java.net.ServerSocket;

public class ConnectionFactory {
    public GameConnection build(
            DataHandler dataHandler,
            Integer port,
            ConnectionPool pool) throws IOException {
        ServerSocket socket = new ServerSocket(port);
        return new GameConnection(dataHandler, socket, pool);
    }

    public WelcomeConnection buildWelcomeConnection(ConnectionPool pool) throws IOException {
        OpenNewConnectionHandler handler = new OpenNewConnectionHandler(pool);
        ServerSocket socket = new ServerSocket(ConnectionPool.WELCOME_PORT);
        return new WelcomeConnection(handler, socket, pool);
    }


    public void openConection(GameConnection connection) {
        Thread thread = new Thread(connection);
        thread.start();
    }
}
