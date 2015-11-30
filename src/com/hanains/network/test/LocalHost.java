package com.hanains.network.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHost {

	public static void main(String[] args) {
	
		try
		{
			InetAddress inetAddress = InetAddress.getLocalHost();
			System.out.println("Host Name: "+inetAddress.getHostName());
			System.out.println("Host IP Address: " + inetAddress.getHostAddress());
			
			byte[] address= inetAddress.getAddress();
			for(int i = 0 ; i < address.length; i++)
			{
				System.out.print(address[i]&0xff);
				if( i+ 1 < address.length )
				{
					System.out.print(":");
				}
			}
			
			
			
		}catch(UnknownHostException e)
		{
			e.printStackTrace();
		}
		
	}
}
