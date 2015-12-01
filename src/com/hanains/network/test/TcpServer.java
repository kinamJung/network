package com.hanains.network.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {

	private static final int PORT = 5055;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;

		try {
			// Create Socket
			serverSocket = new ServerSocket();

			// Bind
			InetAddress inetAddress = InetAddress.getLocalHost();
			String hostAddress = inetAddress.getHostAddress();

			serverSocket.bind(new InetSocketAddress(hostAddress, PORT));
			System.out.println("[서버]바인딩 " + hostAddress + ":" + PORT);

			// Accept
			Socket socket = serverSocket.accept();

			// Success Connect
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socket
					.getRemoteSocketAddress();
			String remoteHostAddress = inetSocketAddress.getAddress()
					.getHostAddress();
			int remoteHostPort = inetSocketAddress.getPort();
			System.out.println("[서버]연결됨 from " + remoteHostAddress + " port "
					+ remoteHostPort);

			// Get IOStream
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();

			

			// Read Data
			try {
				byte[] buffer = new byte[128];
				while (true) {
					int readByteCount = inputStream.read(buffer);
					if (readByteCount < 0) {
						System.out.println("[서버]클라이언트로 부터 연결 끊김");
						break;
					}

					String  data = new String(buffer, 0, readByteCount);
					System.out.println("[서버]수신 데이터:" + data);
					
					// Send Data
					outputStream.write(data.getBytes("UTF-8"));
					outputStream.flush();

				}
			} catch (IOException e) {
				System.out.println("[서버]에러:" + e);
			} finally {
				// Arrage Resource
				inputStream.close();
				outputStream.close();
				if (socket.isClosed() == false) {
					socket.close();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Close Server Socket
			if (serverSocket != null && serverSocket.isClosed() == false) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
