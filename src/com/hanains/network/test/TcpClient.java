package com.hanains.network.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TcpClient {

	private static final String SERVER_IP = "192.168.56.1";
	private static final int SERVER_PORT = 5055;

	public static void main(String[] args) {

		Socket socket = null;
		InputStream inputStream = null;
		OutputStream outputStream = null;

		try {
			// create socket
			socket = new Socket();

			// connect to Server
			socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
			System.out.println("[클라이언트]서버연결 성공");

			// Get IOStream
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();

			// Write / Read
			String data = "Hello World";
			outputStream.write(data.getBytes("UTF-8"));

			byte[] buffer = new byte[256];
			int readByteCount = inputStream.read(buffer);

			data = new String(buffer, 0, readByteCount, "UTF-8");
			System.out.println(">> " + data);

		} catch (IOException e) {
			System.out.println("[클라이언트] 에러:" + e);
		} finally {
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
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
