package com.hanains.network.echo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
/*
 * 					Network Homework(Echo Server, Client)
 * 													   정기남
 * 
 * 
 * */

public class EchoServer {

	private static final int PORT = 5050;
	
	public static void main(String[] args) {
	
		ServerSocket serverSocket = null;
		
		try
		{
			//Create ServerSocket 
			serverSocket = new ServerSocket();
			System.out.println("[info] Create Server Socket");
			
			//Bind
			InetAddress inetAddress =InetAddress.getLocalHost();
			String hostAddress = inetAddress.getHostAddress();
			serverSocket.bind(new InetSocketAddress(hostAddress, PORT));
			
			while(true)
			{
				//ACCEPT
				System.out.println("[info] Wait connect...");
				Socket socket = serverSocket.accept();
				System.out.println("[info] success connect");
				
				//Create Thread and send Socket Pamameter
				Thread receiveThread = new EchoServerReceiveThread(socket);
				receiveThread.start();
				
			}
			
		}catch(IOException e)
		{
			e.printStackTrace();
			System.out.println("[error] server socket IO");
		}finally{
			//Resource Clear
			if( serverSocket != null && serverSocket.isClosed() == false )
			{
				try {
					serverSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("[error] close server socket IO");
				}
			}
			
		}
		
		
		
		
	}
	
}
