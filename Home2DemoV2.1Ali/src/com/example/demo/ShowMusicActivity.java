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

public class ShowMusicActivity extends Activity  {

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
		mediaPlayer.stop();
		mediaPlayer.release();
		super.onPause();
	}
	private static final String TAG = "ShowPictuerActivity";
	private LinearLayout showpicture;
	private ImageView showpictureimage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.show_introduce);
		init();
	}
	 private MediaPlayer mMediaPlayer;
	private int musicid;
	
	//声明一个音乐播放器
	private MediaPlayer mediaPlayer;

	    private void playLocalFile(int i) {
	    	if ( i  == 30) {
	    		musicid = R.raw.paomo;
	    	}
//	    	else if ( i  == 31) {
//	    		musicid = R.raw.xihuanni;
//	    	}else if ( i  == 32) {
//	    		musicid = R.raw.zaixiwangdetianyeshang;
//	    	}else if ( i  == 33) {
//	    		musicid = R.raw.beijingbeijing;
//	    	}else if ( i  == 34) {
//	    		musicid = R.raw.chongchongnanian;
//	    	}else if ( i  == 35) {
//	    		musicid = R.raw.piaoyangguohailaikanni;
//	    	}else if ( i  == 36) {
//	    		musicid = R.raw.heyiaiqing;
//	    	}else if ( i  == 37) {
//	    		musicid = R.raw.shijiandouqunale;
//	    	}else if ( i  == 38) {
//	    		musicid = R.raw.xiaopingguo;
//	    	}else if ( i  == 39) {
//	    		musicid = R.raw.xiaoqingge;
//	    	}else if ( i  == 40) {
//	    		musicid = R.raw.zuixuanminzufeng;
//	    	}else if ( i  == 41) {
//	    		musicid = R.raw.jingju;
//	    	}else if ( i  == 42) {
//	    		musicid = R.raw.yuju;
//	    	}else if ( i  == 43) {
//	    		musicid = R.raw.xiaowangshu;
//	    	}
	    	
	        /*mMediaPlayer = MediaPlayer.create(this,musicid); 
	        mMediaPlayer.setAudioStreamType(10);
		    sendBroadcast(new Intent("com.ali.xiami.exit.play"));
	    try {    
	        mMediaPlayer.prepare();       
	    }   catch (IllegalStateException e) {                   
	    }    catch (IOException e) { 
	    }
	    
	    mMediaPlayer.start();*/
	    	
	    	AssetManager assetManager = this.getAssets();
	    	AssetFileDescriptor fileDescriptor;
			try {
				fileDescriptor = assetManager.openFd("paomo.mp3");
				mediaPlayer = new MediaPlayer();
		           mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
		                 fileDescriptor.getStartOffset(),
		                 fileDescriptor.getLength());
		           mediaPlayer.setAudioStreamType(10);
//				    sendBroadcast(new Intent("com.ali.xiami.exit.play"));
		           mediaPlayer.prepare();
			        mediaPlayer.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	            
		        

	    
	    	mediaPlayer.setOnCompletionListener(new OnCompletionListener() {                
	        public void onCompletion(MediaPlayer mp) { 
	        	mediaPlayer.start();  
	         }}); 
	    }
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		init();
		Log.d(TAG,"intent");
	}
	private void init(){

		showpicture = (LinearLayout) findViewById(R.id.layout_picture_show);
		showpictureimage = (ImageView) findViewById(R.id.show_picture_image);
	    Log.d(TAG,"init!!!!");
	    if(getIntent().getIntExtra("online", 30)==30) {
			showpictureimage.setBackgroundResource(R.drawable.paomo);
			playLocalFile(30);
		}
		//final VideoView videoView = new VideoView(this); 	//新建一个VideoView，并在此界面下调用此videoView直接播放视频
/*		showpictureimage.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
				Intent intent = new Intent(mContext, VideoPayActivity.class);
				intent.putExtra("localvideoplay", 1);
				mContext.startActivity(intent);
			}
		} );*/
	}
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
	    			  
			}else if (keyCode == KeyEvent.KEYCODE_BACK){
				Intent intentstart = new Intent(ShowMusicActivity.this,ShowMusicDcoActivity.class);
				startActivity(intentstart);
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