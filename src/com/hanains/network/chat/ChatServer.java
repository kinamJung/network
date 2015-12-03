package com.hanains.network.chat;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


/*
 * 								채팅 프로그램
 * 
 * 
 * 
 * 													정기남
 * 서버 실행 : java 패키지.ChatServer
 * 클라이언트 실행 : java 패키지.ChatClient
 * 
 * */



public class ChatServer {

	private static final int PORT = 6060;
	
	public static void main(String[] args) {
		
		ServerSocket serverSocket = null;
		ArrayList<ClientInfo> clientList = new ArrayList<ClientInfo>();
		
		try
		{
			serverSocket = new ServerSocket();
			
			// 2. 바인딩
			String hostAddress = InetAddress.getLocalHost().getHostAddress();
			serverSocket.bind( new InetSocketAddress( hostAddress, PORT ) );			
			log( "연결 기다림 " + hostAddress + ":" + PORT );

			// 3. 요청 대기 
			while( true ) {
			   Socket socket = serverSocket.accept();
			   Thread chatServerThread = new ChatServerThread(socket,clientList);
			   chatServerThread.start();
			   
			}
			
		}catch( Exception e )
		{
			e.printStackTrace();
			log("서버 오류");
		}finally{
			if( serverSocket != null && serverSocket.isClosed() == false )
			{
				try
				{
					serverSocket.close();
				}catch(IOException e)
				{
					e.printStackTrace();
					log("[error]Close Serve Socket IO");
				}
			}
			
		}	
		
	}
	
	public static void log(String str){
		System.out.println(str);
	}
	
	
}
