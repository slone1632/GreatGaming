package com.greatgaming;

import java.io.IOException;
import java.net.ServerSocket;

public class ConnectionFactory {
    public Connection build(
            DataHandler dataHandler,
            Integer port,
            ConnectionPool pool) throws IOException {
        ServerSocket socket = new ServerSocket(port);
        return new Connection(dataHandler, socket, pool);
    }


    public void openConection(Connection connection) {
        Thread thread = new Thread(connection);
        thread.start();
    }
}
