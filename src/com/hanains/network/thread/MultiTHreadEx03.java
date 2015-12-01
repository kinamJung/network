package com.hanains.network.thread;

public class MultiTHreadEx03 {
	
	public static void main(String[] args) {
		
		Thread thread = new Thread(new DigitRunnableImpl());
		thread.start();
		
		for (char c = 'a'; c <= 'z'; c++) {
			System.out.print(c);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
