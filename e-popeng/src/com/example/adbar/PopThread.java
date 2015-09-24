package com.example.adbar;

import android.os.Handler;

public class PopThread extends Thread {
	
	
	Handler handler;
	


	public PopThread(Handler handler) {
		super();
		this.handler = handler;
	}



	@Override
	public void run() {
		handler.sendEmptyMessage(MessageType.Pop);
		// TODO Auto-generated method stub
		super.run();
	}
}
