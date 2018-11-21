package com.greatgaming.server;

public class MessagingHandler extends DataHandler{
    public static final String HEARTBEAT_STRING = "HEARTBEAT";
    private ConnectionPool connectionPool;

    public MessagingHandler(ConnectionPool pool) {
        this.connectionPool = pool;
    }

    public String handleData(String data) {
        System.out.println("received: " + data);

        if (data.equals(HEARTBEAT_STRING)) {
            return null;
        }
        connectionPool.sendBroadcastMessage(data);
        return null;
    }
}
