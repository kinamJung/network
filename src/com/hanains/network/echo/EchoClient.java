package com.hanains.network.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class EchoClient {

	private static final String SERVER_IP = "192.168.1.9";
	private static final int SERVER_PORT = 5050;
	private final static int BUFFER_SIZE = 128;
	private final static int CHAR_BUFFER_SIZE = 128;
	private final static String EXIT = "exit";
	
	public static void main(String[] args) {
		Socket socket = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;
		Scanner scan = null;
		
		try {
			scan = new Scanner(System.in);
			
			// create socket
			socket = new Socket();

			// Connect to Server
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			System.out.println("[클라이언트]서버연결 성공");

			// Get IOStream
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();

			// Write / Read
			while(true)
			{
				System.out.print("[클라이언트]송신 데이터:");				
				String data = scan.nextLine();
			
				if(data.equals(EXIT))
				{
					System.out.println("[info] Exit Program");
					break;
				}
				
				outputStream.write(data.getBytes("UTF-8"));
				outputStream.flush();

				//Reader - InputStream
				Reader reader = new InputStreamReader(inputStream,"UTF-8");
				char[] recvStr = new char[CHAR_BUFFER_SIZE];
				int readCount = reader.read(recvStr);
				System.out.println("[클라이언트]수신데이터:" + new String(recvStr,0, readCount));
				
				//InputStream
				/*byte[] buffer = new byte[BUFFER_SIZE];
				int readByteCount = inputStream.read(buffer);

				data = new String(buffer, 0, readByteCount, "UTF-8");
				System.out.println("[클라이언트]수신데이터:" + data);*/
			}
			
		} catch (IOException e) {
			System.out.println("[클라이언트] 에러:" + e);
		} finally {
			//Resource Clear
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
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
