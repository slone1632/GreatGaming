package com.greatgaming;

import java.io.*;
import java.net.*;

public class Main {
	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		ConnectionPool pool = new ConnectionPool(2, factory);
		
		WelcomeConnection connection = factory.buildWelcomeConnection(pool);
		connection.run();
		
		while(true) {
			Thread.sleep(10000);
		}
	}
}
