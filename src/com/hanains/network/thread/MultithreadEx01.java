package com.hanains.network.thread;


public class MultithreadEx01 {

	public static void main(String[] args) {
		
		Thread thread = new DigitThread();
		thread.start();
		
		Thread alpabetThread = new LowerCaseAlphabetThread();
		alpabetThread.start();
	}
	
}
