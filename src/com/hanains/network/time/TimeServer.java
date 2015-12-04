package com.hanains.network.time;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * 							UDP Time Server/Client
 * 
 * 
 * 
 * 															정기남
 * 
 * */

public class TimeServer {

	private static final int PORT = 6060;
	private static final int BUFFER_SIZE = 1024;
	private static final String STR_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss a";
	
	
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
				byte[] sendData = null;
				
				//Analyze and Make Send Data STRING
				if( strData.equals("") )
				{
					SimpleDateFormat format = new SimpleDateFormat( STR_DATE_FORMAT );
					String data = format.format( new Date() );
					sendData = data.getBytes("UTF-8");
					
				}else
				{
					sendData = "Unknown".getBytes("UTF-8");
				}
				// Send Data
				DatagramPacket sendPacket = new DatagramPacket(
						sendData, sendData.length,
						receivePacket.getAddress(), receivePacket.getPort());

				// Send Data
				datagramSocket.send(sendPacket);
			}
			

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("error:" + e);
		} finally {
			if (datagramSocket != null) {
				datagramSocket.close();
			}
		}
		
		
	}
	

}
