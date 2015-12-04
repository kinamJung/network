package com.hanains.network.echo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/*
 * 						UDP Echo 서버/클라이언트	
 * 
 * 
 * 
 * 
 * 										정기남
 * 
 * */


public class UDPEchoServer {

	private static final int PORT = 6060;
	private static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {
		DatagramSocket datagramSocket = null;
		
		try {
			// Create Datagram Socket
			datagramSocket = new DatagramSocket(PORT);

			while(true)
			{
				// Wait Receive data
				DatagramPacket receivePacket = new DatagramPacket(
						new byte[BUFFER_SIZE], BUFFER_SIZE);
				datagramSocket.receive(receivePacket);

				// 데이터 확인
				String strData = new String(receivePacket.getData(), 0,
						receivePacket.getLength(), "UTF-8");
				log("Receive Data :" + strData);

				// Send Data
				DatagramPacket sendPacket = new DatagramPacket(
						receivePacket.getData(), receivePacket.getLength(),
						receivePacket.getAddress(), receivePacket.getPort());

				// Send Data
				datagramSocket.send(sendPacket);
			}
			
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log("error:" + e);
		} finally {
			if (datagramSocket != null) {
				datagramSocket.close();
			}
		}
	}

	public static void log(String message) {
		System.out.println("[UDP Echo Server] " + message);
	}
}
