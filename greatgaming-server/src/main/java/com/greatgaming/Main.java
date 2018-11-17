package com.greatgaming;

import java.io.*;
import java.net.*;

public class Main {
	public static void main(String[] args) throws Exception {
		ConnectionPool pool = new ConnectionPool(2);
		pool.startWelcomeConnection();
		while(true) {
			Thread.sleep(10000);
		}
	}
}
