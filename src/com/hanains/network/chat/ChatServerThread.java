package com.hanains.network.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServerThread extends Thread {

	private Socket socket;
	private String nickName;
	private BufferedReader bufferReader = null;
	private PrintWriter printWriter = null;

	private ArrayList<ClientInfo> clientList = null;

	private static final String TOKEN_DIVISION = ":";
	private static final String REGIST_NICKNAME = "join";
	private static final String MESSAGE = "message";
	private static final String TALK_TO_PERSON = "talk";	
	private static final String PERSON_LIST = "list";
	private static final String EXIT = "exit";
	
	private static final int TALK_MSG_FORMAT_LENGTH = 3;

	public ChatServerThread(Socket socket, ArrayList<ClientInfo> clientList) {
		this.socket = socket;
		this.clientList = clientList;
	}

	@Override
	public void run() {

		try {
			bufferReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), "UTF-8"));
			printWriter = new PrintWriter(new OutputStreamWriter(
					socket.getOutputStream(), "UTF-8"));
			String opcode = null;
			String token[] = null;
			while (true) {
				// Get Requst From Client
				String request = bufferReader.readLine();
				if (request == null) {
					ChatServer.log("[error] Close Connect From Client");
					break;
				}

				// Analyze protocol
				token = request.split(TOKEN_DIVISION);
				if (token.length == 1) {
					// 그냥 엔터 치는 경우
					continue;
				}

				opcode = token[0];
				System.out.println("[info]전달된 OPCODE:" + opcode);

				// Analyze Start
				if (opcode.equals(REGIST_NICKNAME)) // 등록
				{
					boolean isSuccess = false;
					isSuccess = doJoin(token[1], printWriter);
					// 성공 여부에 따라 msg를 다르게 보낸다.
					if (isSuccess == true) {
						sendAck(token[0], printWriter);
					} else {
						sendNck(token[0], printWriter, "fail", "ID 등록 실패");
					}
				} else if (opcode.equals(MESSAGE)) // 메시지
				{
					System.out.println("[info] message 시작");
					broatCast(nickName, token[1], printWriter);
				} else if (opcode.equals(TALK_TO_PERSON)) // 귓속말
				{
					if (token.length == TALK_MSG_FORMAT_LENGTH) {
						doTalk(token[1], printWriter, token[2]);
					} else {
						// 메세지 포맷 잘못 지켜 보내는 경우
						sendNck(opcode, printWriter, "전송실패", "예)talk:닉네임:내용");
					}
				} else if (opcode.equals(PERSON_LIST)) // 사용자 목록 요청
				{
					// 목록 보내기
					sendList();
				} else if (opcode.equals(EXIT)) {
					// 종료하기
					doQuit(nickName, printWriter, bufferReader, socket);
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			ChatServer.log("[error] ChatServerThread IO Exception");
		}

	}

	private void sendList() {
		String str = "";
		for (ClientInfo client : clientList) {
			str = str + "[" + client.getNickName() + "] ";
		}

		printWriter.write("사용자 목록 : " + str + "\r\n");
		printWriter.flush();

	}

	// nickName: 받는 사람, printWriter : 나의 printWriter Stream, data : 보낼 데이터
	private void doTalk(String nickName, PrintWriter printWriter, String data) {

		for (ClientInfo client : clientList) {
			if (client.getNickName().equals(nickName)) {
				// data 보내기
				client.getPrintWriter().write(
						this.nickName + "님이 보낸 메시지 : " + data + "\r\n");
				client.getPrintWriter().flush();
				return;
			}
		}

		printWriter.write("해당 사용자는 없습니다.\r\n");
		printWriter.flush();

	}

	synchronized private void doQuit(String nickName, PrintWriter printWriter,
			BufferedReader bufferReader, Socket socket) {
		int removeIndex = -1;
		for (int i = 0; i < clientList.size(); i++) {
			ClientInfo client = clientList.get(i);
			if (client.getNickName().equals(nickName)) {
				removeIndex = i;
				break;
			}
		}

		if (removeIndex != -1) {
			clientList.remove(removeIndex);
			try {
				if (bufferReader != null) {
					bufferReader.close();
				}

				if (printWriter != null) {
					printWriter.close();
				}
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			String strMsg = "[info]" + nickName + "님이 퇴장 하였습니다.";
			ChatServer.log(strMsg);
			broatCast(nickName, "퇴장하였습니다", printWriter);

		}

	}

	private void sendAck(String protocol, PrintWriter printWriter) {

		printWriter.write(protocol + TOKEN_DIVISION + "ok\r\n");
		printWriter.flush();

	}

	private void sendNck(String protocol, PrintWriter printWriter,
			String failWhy, String result) {
		printWriter.write(protocol + TOKEN_DIVISION + failWhy + TOKEN_DIVISION
				+ result + "\r\n");
		printWriter.flush();

	}

	synchronized private void broatCast(String nickName, String data,
			PrintWriter cPrintWriter) {

		for (ClientInfo client : clientList) {
			// 자기 자신은 메시지 안 날림
			if (client.getPrintWriter().equals(cPrintWriter)) {
				continue;
			}
			client.getPrintWriter().write(
					"[" + nickName + "] :" + data + "\r\n");
			client.getPrintWriter().flush();
		}

	}

	synchronized private boolean doJoin(String nickName, PrintWriter printWriter) {
		boolean retValue = false;

		// 중복 닉네임 검색
		for (ClientInfo client : clientList) {
			if (client.getNickName().equals(nickName)) {
				return retValue;
			}
		}

		// 중복 닉네임이 없을떄
		broatCast(nickName, "님이 들어 오셨습니다.", printWriter);

		ClientInfo clientInfo = new ClientInfo(nickName, printWriter);
		clientList.add(clientInfo);

		this.nickName = nickName;

		System.out.println(nickName + "를 List에 추가");

		return true;
	}

}
