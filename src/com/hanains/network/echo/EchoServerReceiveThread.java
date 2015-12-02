package com.hanains.network.echo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
			BufferedReader buffereReader = new BufferedReader( new InputStreamReader( socket.getInputStream(), "UTF-8"));
			BufferedWriter bufferedWriter = new BufferedWriter( new OutputStreamWriter( socket.getOutputStream(),"UTF-8"));
			

			// Read Data
			try {
				while (true) {
					String data = buffereReader.readLine();
					// '\r\n'이 안오면 계속 Blocking
					if (data == null) {
						// '\r\n'을 상대방이 보낼때 null이 온다
						System.out.println("[서버]클라이언트로 부터 연결 끊김");
						break;
					}

					//String data = new String(buffer, 0, readCharCount);
					System.out.println("[서버]수신 데이터:" + data);

					// Send Data
					bufferedWriter.write( new String (data+"\r\n") );
					bufferedWriter.flush();

				}

			} catch (IOException e) {
				System.out.println("[서버]에러:" + e);
			} finally {
				
				//Resource Clear
				if (buffereReader != null) {
					buffereReader.close();
				}
				if (bufferedWriter != null) {
					bufferedWriter.close();
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
