package com.greatgaming.server;

public class ConsoleHandler extends DataHandler {
	@Override
	public String handleData(String data){
		System.out.println("received: " + data);
		
		return data;
	}
}
