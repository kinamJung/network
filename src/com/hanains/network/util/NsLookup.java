package com.hanains.network.util;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NsLookup {

	private static final String EXIT = "exit";

	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);
		System.out.println("[info]프로그램 시작");

		while (true) {
			System.out.print(">");
			String hostName = scan.nextLine();

			// EXIT
			if (hostName.equals(EXIT)) {
				System.out.println("[Info]프로그램 종료");
				break;
			}
			// process
			try {
				InetAddress inetAddress[] = InetAddress.getAllByName(hostName);

				for (int i = 0; i < inetAddress.length; i++) {
					System.out.println(inetAddress[i].getHostName() + ":"
							+ inetAddress[i].getHostAddress());
				}
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
