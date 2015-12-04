package com.hanains.network.echo;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class UDPEchoClient {

	private static final String HOST_ADDRESS = "127.0.0.1";
	private static final int PORT = 6060;
	private static final int BUFFER_SIZE = 1024;

	public static void main(String[] args) {
		DatagramSocket datagramSocket = null;
		Scanner scan = new Scanner(System.in);
		try {
			// Create UDP Socket
			datagramSocket = new DatagramSocket();
			
			while( true )
			{
				// Create Send Packet
				System.out.print(">>");
				String data = scan.nextLine();
				
				byte[] sendData = data.getBytes("UTF-8");
				DatagramPacket sendPacket = new DatagramPacket(sendData,
						sendData.length, new InetSocketAddress(HOST_ADDRESS, PORT));
				
				// Send Data
				datagramSocket.send(sendPacket);
				
				//Receive Data

				DatagramPacket receivePacket = new DatagramPacket(
						new byte[BUFFER_SIZE], BUFFER_SIZE);			
				datagramSocket.receive(receivePacket);	
				String strData = new String(receivePacket.getData(), 0, receivePacket.getLength(), "UTF-8");
				System.out.println("<<" + strData);

			}
						
			
		} catch (Exception e) {
			log("error: " + e);
		} finally {
			if (datagramSocket != null) {
				datagramSocket.close();
			}
		}
	}

	public static void log(String message) {
		System.out.println("[UDP Echo Client] " + message);
	}
}
