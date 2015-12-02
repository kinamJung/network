package com.hanains.network.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 									웹서버 관련 숙제
 * 
 * 
 * 
 * 								
 *  * 
 * 																@author 정기남
 *    ※  join.html에서 form안의 메소드를 get에서 post로 변경 -> 요구사항6번 페이지에는 post로 명시되어 있으나 실제 파일에는 get으로 명시
 */


public class RequestHandler extends Thread {

	private static final String SPACE_TOKEN = "\\s+";
	private static final String ERROR_PAGE_PATH_400 = "./webapp/error/400.html";
	private static final String ERROR_PAGE_PATH_404 = "./webapp/error/404.html";
	private static final String INDEX_PAGE = "index.html";
	
	
	private Socket socket;
	public RequestHandler(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {

		BufferedReader bufferedReader = null;
		OutputStream outputStream = null;

		try {
			// get IOStream
			bufferedReader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			outputStream = socket.getOutputStream();

			// logging Remote Host IP Address & Port
			InetSocketAddress inetSocketAddress = (InetSocketAddress) socket
					.getRemoteSocketAddress();
			SimpleHttpServer.consolLog("connected from "
					+ inetSocketAddress.getHostName() + ":"
					+ inetSocketAddress.getPort());

			
			String request = "";
			SimpleHttpServer
					.consolLog("=======================Request Infomation=======================");

			while (true) {
				String line = bufferedReader.readLine();
				if (line == null || "".equals(line)) {
					break;
				}
				if ("".equals(request)) {
					request = line;
				}
				SimpleHttpServer.consolLog(line);
			}
			SimpleHttpServer
					.consolLog("===============================================================");

			// 요청 처리
			String[] token = request.split(SPACE_TOKEN);
			
			if ("GET".equals(token[0])) {
				responseStaticResourse(outputStream, token[1], token[2]);

			} else {
				//메소드가 GET 이 외 일때
				response400Error(outputStream, token[2]);
			}

		} catch (Exception ex) {
			SimpleHttpServer.consolLog("error:" + ex);
		} finally {
			// clean-up
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}

				if (outputStream != null) {
					outputStream.close();
				}
				if (socket != null && socket.isClosed() == false) {
					socket.close();
				}

			} catch (IOException ex) {
				SimpleHttpServer.consolLog("error:" + ex);
			}
		}

	}

	private void responseStaticResourse(OutputStream outputStream, String url, String protocol) throws IOException {
		
		File file = null;
		// 파일 객체 생성
		// default html 처리
		
		if (url.equals("/")) {
			file = new File("./webapp" + url + INDEX_PAGE);
		} else {
			file = new File("./webapp" + url);
		}
		
		// file 존재 여부 처리
		if (file.exists() == false) {
			response404Error(outputStream, protocol);
			return;
		}

		Path path = file.toPath();
		byte[] body = Files.readAllBytes(path);

		String mimeType = Files.probeContentType(path);

		outputStream.write((protocol + " 200 OK\r\n").getBytes("UTF-8"));
		outputStream.write(("Content-Type:" + mimeType + "; charset=UTF-8\r\n").getBytes("UTF-8"));
		outputStream.write("\r\n".getBytes());
		outputStream.write(body);

	}

	//Response 400 ERROR
	private void response400Error(OutputStream outputStream, String protocol)
			throws IOException {
		File file = new File(ERROR_PAGE_PATH_400);
		Path path = file.toPath();
		byte[] body = Files.readAllBytes(path);

		outputStream.write((protocol + " 400 Bad Request\r\n").getBytes());
		outputStream.write("Content-Type:text/html; charset=UTF-8\r\n".getBytes());
		outputStream.write("\r\n".getBytes());
		outputStream.write(body);

	}
	
	//Response 404 ERROR 
	private void response404Error(OutputStream outputStream, String protocol)
			throws IOException {

		File file = new File(ERROR_PAGE_PATH_404);
		Path path = file.toPath();
		byte[] body = Files.readAllBytes(path);

		outputStream.write((protocol + " 404 File Not Found\r\n").getBytes());
		outputStream.write("Content-Type:text/html; charset=UTF-8\r\n".getBytes());
		outputStream.write("\r\n".getBytes());
		outputStream.write(body);

	}

}
