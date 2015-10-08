package com.example.demo;

import cn.yunzhisheng.voicetv.R;
import cn.yunzhisheng.vui.assistant.tv.MessageReceiver;
import cn.yunzhisheng.vui.assistant.tv.WindowService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

public class YuYinSiRenFilm1 extends Activity {

	private ImageView imageViewsiren;
	private int picId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pyysirenfilm1);
	}
	
//	private void init() {
//		// TODO Auto-generated method stub
//		imageViewsiren = (ImageView)findViewById(R.id.imageViewsiren);
//		picId = getIntent().getIntExtra("picid", 0);
//		Log.d("demo", "picid==="+picId);
//		
//		if(picId == 0){
//			imageViewsiren.setBackgroundResource(R.drawable.demoyysirenfilm1);
//		}else if(picId == 1){
//			imageViewsiren.setBackgroundResource(R.drawable.wodeyingyuan);
//		}
//		
//	}
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d("bill", "event===" + keyCode);
		if (keyCode == 227 && event.getAction() == KeyEvent.ACTION_UP) {
			Log.d("bill", "onKeyUp!");
			Intent closedvoice = new Intent();
			closedvoice.setAction("cn.yunzhisheng.intent.voice.stop");
			sendBroadcast(closedvoice);
			Appliaction.isLongPressKey = true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == 227 && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (Appliaction.isLongPressKey == true) {
				Intent openvoice = new Intent();
				Log.d("bill", "onKeyDown227!");
				openvoice.setAction("cn.yunzhisheng.intent.voice.start");
				sendBroadcast(openvoice);
				Appliaction.isLongPressKey = false;
			}
		} else {  
			switch (keyCode) {
	case KeyEvent.KEYCODE_DPAD_LEFT:
				 
//	           	finish();
	    		Log.v("URI", "left");
				return true;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				
	           	finish();
	    		Log.v("URI", "right");
				return true;
			case KeyEvent.KEYCODE_DPAD_CENTER:
					Intent intent = new Intent(YuYinSiRenFilm1.this,YuYinSiRenFilm2.class);
		    		startActivity(intent); 
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

			Intent i = new Intent(YuYinSiRenFilm1.this, WindowService.class);
			startService(i);

			Intent vritualKey = new Intent(
					MessageReceiver.ACTION_VIRTUAL_KEY_SERVER);
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
