package com.example.demo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.yunzhisheng.voicetv.R;

public class gouwuActivity extends Activity  {

	private static final String TAG = "ShowMoviePictureActivity";
	private LinearLayout showpicture;
	private ImageView showmoviepicture;
	private gouwuActivity mContext;
	
	private String TV_JU="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		mContext = this;
		setContentView(R.layout.show_introduce);
        init();
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
		showmoviepicture = (ImageView) findViewById(R.id.show_picture_image);
	    Log.d(TAG,"init!!!!");
	    
	    
		if(getIntent().getIntExtra("gouwu", 0)==0){
	    	showmoviepicture.setBackgroundResource(R.drawable.chongdian);
	    	TV_JU="0";
	    }else if(getIntent().getIntExtra("gouwu", 1)==1){
			showmoviepicture.setBackgroundResource(R.drawable.chongdian);
			TV_JU="1";			
		}else if(getIntent().getIntExtra("gouwu", 2)==2){
			showmoviepicture.setBackgroundResource(R.drawable.fengkuang_gouwu);
			TV_JU="2";
		}else if(getIntent().getIntExtra("gouwu", 3)==3){
			showmoviepicture.setBackgroundResource(R.drawable.zhuanzhi);
			TV_JU="3";
		}else if(getIntent().getIntExtra("gouwu", 4)==4){
			showmoviepicture.setBackgroundResource(R.drawable.haochi);
			TV_JU="4";
		}else if(getIntent().getIntExtra("gouwu", 5)==5){
			showmoviepicture.setBackgroundResource(R.drawable.aijiu);
			TV_JU="5";
		}else if(getIntent().getIntExtra("gouwu", 6)==6){
			showmoviepicture.setBackgroundResource(R.drawable.jiejie);
			TV_JU="6";
		}else if(getIntent().getIntExtra("gouwu", 7)==7){
			showmoviepicture.setBackgroundResource(R.drawable.chunzhuang);
			TV_JU="7";
		}
		 
		//final VideoView videoView = new VideoView(this); 	//新建一个VideoView，并在此界面下调用此videoView直接播放视频
		 showmoviepicture.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				if(TV_JU.equals("1")){
//					Intent intent = new Intent(mContext, VideoPayActivity.class);
//					intent.putExtra("localvideoplay", 1);
//					mContext.startActivity(intent);
//				}else if(TV_JU.equals("2")){
//					Intent intent = new Intent(mContext, VideoPayActivity.class);
//					intent.putExtra("localvideoplay", 1);
//					mContext.startActivity(intent);
//				}
				
				
			}
		} );
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