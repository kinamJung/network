package com.hanains.network.chat;

import java.io.BufferedReader;
import java.io.IOException;


public class ChatClientRecvThread extends Thread {

	BufferedReader bufferedReader;

	public ChatClientRecvThread(BufferedReader bufferedReader) {
		super();
		this.bufferedReader = bufferedReader;
	}
	@Override
	public void run() {
		
		String serverMsg = null;
		try {
			while(true)
			{	
				serverMsg = bufferedReader.readLine();
				System.out.println(serverMsg);
				
			}
			
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("[클라이언트] 종료 ...");
			
		}
		
	}
}
