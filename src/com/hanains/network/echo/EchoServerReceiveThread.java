package com.hanains.network.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class EchoServerReceiveThread extends Thread {

	private Socket socket;
	private final static int BUFFER_SIZE = 128;

	//Constructor
	public EchoServerReceiveThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		try {
			
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
				byte[] buffer = new byte[BUFFER_SIZE];

				while (true) {
					int readByteCount = inputStream.read(buffer);
					if (readByteCount < 0) {
						System.out.println("[서버]클라이언트로 부터 연결 끊김");
						break;
					}

					String data = new String(buffer, 0, readByteCount);
					System.out.println("[서버]수신 데이터:" + data);

					// Send Data
					outputStream.write(data.getBytes("UTF-8"));
					outputStream.flush();

				}

			} catch (IOException e) {
				System.out.println("[서버]에러:" + e);
			} finally {
				
				//Resource Clear
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
				if ( socket!=null && (socket.isClosed() == false)) {
					socket.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
