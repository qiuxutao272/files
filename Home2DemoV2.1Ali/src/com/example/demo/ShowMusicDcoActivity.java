package com.example.demo;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.yunzhisheng.voicetv.R;

public class ShowMusicDcoActivity extends Activity  {

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onStop");
		super.onStop();
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onPause");
		super.onPause();
	}
	private static final String TAG = "ShowPictuerActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.show_dco);
	}
	 private MediaPlayer mMediaPlayer;
	private int musicid;
	
	private boolean isLongPressKey=true;
	@Override	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d(TAG,"keyCode==="+keyCode);	
	    if(keyCode==227&&event.getAction()==KeyEvent.ACTION_DOWN)
			{	    	
	    		if(isLongPressKey==true)
	    		{
	    		Intent openvoice = new Intent();
	  		    Log.d(TAG,"onKeyDown!");
	  			openvoice.setAction("cn.yunzhisheng.intent.voice.start");
	  			sendBroadcast(openvoice);	
	  			isLongPressKey=false;
	  			}
	    			  
			}else if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER) || (keyCode == KeyEvent.KEYCODE_ENTER)){
				Intent intentstart = new Intent(this,MusicService.class);
				startService(intentstart);
				
				Intent intent = new Intent(this, VideoPayActivity.class);
				intent.putExtra("localvideoplay", 11);
				this.startActivity(intent);
				finish();
			}
			
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d(TAG,"event==="+keyCode);
		if(keyCode==227&&event.getAction()==KeyEvent.ACTION_UP){
			Log.d(TAG,"onKeyUp!");
			Intent closedvoice = new Intent();
			closedvoice.setAction("cn.yunzhisheng.intent.voice.stop");
			sendBroadcast(closedvoice);
			isLongPressKey=true;
		}
		return super.onKeyUp(keyCode, event);
	}
}