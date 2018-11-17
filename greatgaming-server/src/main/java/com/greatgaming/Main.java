package com.greatgaming;

import java.io.*;
import java.net.*;

public class Main {
	public static void main(String[] args) throws Exception {
		ConnectionPool pool = new ConnectionPool(2);
		
		OpenNewConnectionHandler handler = new OpenNewConnectionHandler(pool);
		ServerSocket socket = new ServerSocket(ConnectionPool.WELCOME_PORT);
		Connection connection = new Connection(handler, socket, pool);
		connection.run();
		
		while(true) {
			Thread.sleep(10000);
		}
	}
}
