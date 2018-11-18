package com.greatgaming;

import java.io.*;
import java.net.*;

public class ConsoleHandler extends DataHandler {
	@Override
	public String handleData(String data){
		System.out.println("received: " + data);
		
		return data;
	}
}
