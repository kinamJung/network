package com.hanains.network.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

	private static final String SERVER_IP = "192.168.1.9";
	private static final int SERVER_PORT = 6060;
	private static final String TOKEN_DIVISION = ":";
	private static final String REGIST_NICKNAME = "join";
	private static final String PERSON_LIST = "list";
	private static final String TALK_TO_PERSON = "talk";
	private static final String EXIT = "exit";
	private static final String MESSAGE = "message";

	public static void main(String[] args) {

		Socket socket = null;
		BufferedReader buffereReader = null;
		PrintWriter printWriter = null;
		Scanner scan = null;

		try {
			scan = new Scanner(System.in);

			// create socket
			socket = new Socket();

			// Connect to Server
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			System.out.println("[클라이언트]서버연결 성공");

			// Get IOStream
			buffereReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), "UTF-8"));
			printWriter = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream(), "UTF-8"));

			// Get Nick Name
			String nickName = null;
			while (true) {
				System.out.print("닉네임  > ");
				nickName = scan.nextLine();

				// Send Nick Name
				printWriter.write(REGIST_NICKNAME + TOKEN_DIVISION + nickName
						+ "\r\n");
				printWriter.flush();

				String opcode = null;
				String serverMsg = buffereReader.readLine();
				String token[] = serverMsg.split(TOKEN_DIVISION);
				opcode = token[0];

				// If Regist Nick Name than Sended join:ok
				if (opcode.equals(REGIST_NICKNAME) && token[1].equals("ok")) {
					System.out.println("닉네임 등록 완료.");
					break;
				} else // Fail Regist NickName
				{
					System.out.println("[" + serverMsg + "]");
				}
			}
			// Create Receive Thread
			Thread receiveThread = new ChatClientRecvThread(buffereReader);
			receiveThread.start();

			System.out.println("----채팅 도움말----");
			System.out.println("귓속말 -> talk:받는사람:메시지");
			System.out.println("종료 -> exit");
			System.out.println("채팅 사용자 목록 조회 -> list");
			System.out.println("--------------");
			while (true) {
				// Get Msg From User
				String msg = scan.nextLine();

				// Requst User List
				if (msg.equals(PERSON_LIST)) {
					printWriter.write(msg + ": \r\n");
					printWriter.flush();
					continue;
				} else if(msg.length() > 4) {
					// Requst 귓속말
					String opCode = msg.substring(0, 4);
					if (opCode.equals(TALK_TO_PERSON)) {
						printWriter.write(msg + "\r\n");
						printWriter.flush();
						continue;
					}
				}else if (msg.equals(EXIT)) {
					// 종료
					printWriter.write(EXIT + TOKEN_DIVISION + " \r\n");
					printWriter.flush();

					break;
				} else {
					printWriter.write(MESSAGE + TOKEN_DIVISION + msg + "\r\n");
					printWriter.flush();
				}
			}

		} catch (IOException e) {
			System.out.println("[클라이언트] 에러:" + e);
		} finally {
			// Resource Clear
			try {
				if (buffereReader != null) {
					buffereReader.close();
				}
				if (printWriter != null) {
					printWriter.close();
				}
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}
				scan.close();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
