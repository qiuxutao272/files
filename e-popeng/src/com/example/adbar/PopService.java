package com.example.adbar;



import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.hrtvbic.adpop.MyDockPanelEng;

public class PopService extends Service {


	private MyDockPanelEng panel;

	public PopService() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		Log.v("sssssssssssssss", "PopService  onCreate ");
		super.onCreate();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("sssssssssssssss", "PopService  onStartCommand ");
		panel = new MyDockPanelEng(this);
		panel.start();
		return super.onStartCommand(intent, flags, startId);
	}

}
