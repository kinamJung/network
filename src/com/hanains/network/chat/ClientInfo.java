package com.hanains.network.chat;

import java.io.PrintWriter;

public class ClientInfo {

	private String nickName;
	private PrintWriter printWriter;
	
	public ClientInfo(String nickName, PrintWriter printWriter) {
		this.nickName = nickName;
		this.printWriter = printWriter;
	}
	
	
	public String getNickName() {
		return nickName;
	}
	
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public PrintWriter getPrintWriter() {
		return printWriter;
	}
	
	public void setPrintWriter(PrintWriter printWriter) {
		this.printWriter = printWriter;
	}
	
}
