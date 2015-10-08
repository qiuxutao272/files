package com.example.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.yunzhisheng.voicetv.R;
import cn.yunzhisheng.vui.assistant.tv.MessageReceiver;
import cn.yunzhisheng.vui.assistant.tv.WindowService;


import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

public class YuYinHighQingJiaoYu1 extends Activity {
	ArrayList<String> packagNameList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pyyhighyouerjiaoyu1);
		Appliaction.YuYinHighQingJiaoYu1 = this;
		StartServerTask startServerTaskTask = new StartServerTask();
		startServerTaskTask.execute();
	}
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d("bill","event==="+keyCode);
		if(keyCode==227&&event.getAction()==KeyEvent.ACTION_UP){
			Log.d("bill","onKeyUp!");
			Intent closedvoice = new Intent();
			closedvoice.setAction("cn.yunzhisheng.intent.voice.stop");
			sendBroadcast(closedvoice);
			Appliaction.isLongPressKey=true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==227&&event.getAction()==KeyEvent.ACTION_DOWN)
		{	 
			if(Appliaction.isLongPressKey==true)
    		{
    		Intent openvoice = new Intent();
  		    Log.d("bill","onKeyDown227!");
  			openvoice.setAction("cn.yunzhisheng.intent.voice.start");
  			sendBroadcast(openvoice);
  			Appliaction.isLongPressKey=false;
    		}
		}
		else
		{
		//Intent intent = new Intent(picture7YuYin.this,picture8.class);  
		Intent intentright = new Intent(YuYinHighQingJiaoYu1.this,YuYinHighQingJiaoYu2.class);
		switch (keyCode) {

		case KeyEvent.KEYCODE_DPAD_LEFT:
			finish();
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:  
    		startActivity(intentright);  
           	finish();
    		Log.v("URI", "right");
			return true;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			startActivity(intentright);  
           	finish();
		
    		Log.v("URI", "center");
			return true;
		default:
			break;
		}
		}
		return super.onKeyDown(keyCode, event);
	}
	class StartServerTask extends AsyncTask<Integer, Integer, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Integer... params) {

			Intent i = new Intent(YuYinHighQingJiaoYu1.this, WindowService.class);
			startService(i);

			Intent vritualKey = new Intent(MessageReceiver.ACTION_VIRTUAL_KEY_SERVER);
			startService(vritualKey);
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}

	}
}
