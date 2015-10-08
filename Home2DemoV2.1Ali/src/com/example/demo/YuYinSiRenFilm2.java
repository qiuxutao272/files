package com.example.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.yunzhisheng.voicetv.R;
import cn.yunzhisheng.vui.assistant.tv.MessageReceiver;
import cn.yunzhisheng.vui.assistant.tv.WindowService;

public class YuYinSiRenFilm2 extends Activity {

	
	private ProgressBar xh_ProgressBar;
	private TextView textView;
	protected static final int GUI_STOP_NOTIFIER = 0x108;
	protected static final int GUI_THREADING_NOTIFIER = 0x109;
	public int intCounter = 0;
	private String flag="0";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pyysirenfilm2);
		Appliaction.YuYinSiRenFilm2 = this;
		xh_ProgressBar=(ProgressBar)findViewById(R.id.ProgressBar01);
		textView=(TextView)findViewById(R.id.Text1);
		StartServerTask startServerTaskTask = new StartServerTask();
		startServerTaskTask.execute();
		
	}
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
		//Intent intent = new Intent(YuYinSiRenFilm2.this,picture3.class); 
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
//			case KeyEvent.KEYCODE_DPAD_LEFT:
//	    		Log.v("URI", "right");
//				return true;
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				finish();
	    		Log.v("URI", "right");
				return true;
			case KeyEvent.KEYCODE_DPAD_CENTER:
				//dosomething
	    		//startActivity(intent);  
				
				if(flag.equals("0")){
					textView.setText("缓存中");
					init();
				}else if(flag.equals("1")){
					Intent intent = new Intent(this, VideoPayActivity.class);
					intent.putExtra("localvideoplay", 10);
					this.startActivity(intent);
		    		Log.v("URI", "center");
				}
				
				
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

			Intent i = new Intent(YuYinSiRenFilm2.this, WindowService.class);
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
	
	public void init(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					try {
						//设置进度值
						intCounter = (i + 1) * 20;
						//睡眠1000毫秒
						Thread.sleep(1000);

						if (i == 4) {
							Message m = new Message();

							m.what = YuYinSiRenFilm2.GUI_STOP_NOTIFIER;
							YuYinSiRenFilm2.this.myMessageHandler
									.sendMessage(m);
							break;
						} else {
							Message m = new Message();
							m.what = YuYinSiRenFilm2.GUI_THREADING_NOTIFIER;
							YuYinSiRenFilm2.this.myMessageHandler
									.sendMessage(m);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}).start();
	}
	
	Handler myMessageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			//ProgressBar已经是最大值
			case YuYinSiRenFilm2.GUI_STOP_NOTIFIER:
				xh_ProgressBar.setVisibility(View.VISIBLE);
				textView.setText("播放");
				flag="1";
				Thread.currentThread().interrupted();
				break;
			case YuYinSiRenFilm2.GUI_THREADING_NOTIFIER:
				if (!Thread.currentThread().isInterrupted()) {
					//改变ProgressBar的当前值
					xh_ProgressBar.setProgress(intCounter);

					//设置标题栏中前景的一个进度条进度值
					setProgress(intCounter * 100);
				}
				break;
			}
			super.handleMessage(msg);
		}
	};
}
