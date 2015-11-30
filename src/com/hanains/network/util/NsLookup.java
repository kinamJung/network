package com.hanains.network.util;

import java.util.Scanner;

public class NsLookup {

	private static final String EXIT = "exit";

	public static void main(String[] args) {

		Scanner scan = new Scanner(System.in);

		while (true) {
			String hostName = scan.nextLine();

			if (EXIT.equals(hostName)) {
				break;
			}
		}

	}
}
