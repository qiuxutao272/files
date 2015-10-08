package com.example.demo;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.Toast;
import android.widget.VideoView;
import cn.yunzhisheng.voicetv.R;

public class VideoPayActivity extends Activity {

	private VideoPayActivity mContext;
	private static final String TAG = "VideoPayActivity";
	private int mPositionWhenPaused = -1;
	private VideoView mVideoView;
	private Uri mUri;
	private int videoId;
	private String videoName;
	private VideoView videoView = null;
	private MediaController controller = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.videoplay_app);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		init();

		Log.i("bofang", "====进入播放类=====");
	}

	// @Override
	// protected void onNewIntent(Intent intent) {
	// // TODO Auto-generated method stub
	// super.onNewIntent(intent);
	// setIntent(intent);
	// init();
	// Log.d(TAG,"intent");
	// }

	private void init() {
		mVideoView = (VideoView) findViewById(R.id.video_view);
		// if (getIntent().getIntExtra("localvideoplay", 1) == 1) {
		
		//if (getIntent().getIntExtra("localvideoplay", 1) == 1) {

		// SharedPreferences sharedPreferences = getSharedPreferences(
		// "uplu", Activity.MODE_PRIVATE);
		// // 使用getString方法获得value
		// String path = sharedPreferences.getString("up", "");
//		if (getIntent().getStringExtra("dianying").equals("long")) {
//			String pa = FindFileOnUSB("qianlong.mkv");
//			mUri = Uri.parse(pa);
//			Log.i("bofang", "====潜龙=====");
//		} else if (getIntent().getStringExtra("dianying").equals("kuang")) {
//			String pa = FindFileOnUSB("fengkuang.rmvb");
//			mUri = Uri.parse(pa);
//			Log.i("bofang", "====疯狂=====");
//		}

		// Log.i("pa", "========"+pa);
		// mUri = Uri.parse("android.resource://" + getPackageName() + "/"
		// + R.raw.movie2014);
		// mUri = Uri.parse(path+"/fengkuang.rmvb");

				
				videoId = getIntent().getIntExtra("localvideoplay", 0);
				Log.i(TAG, "videoId==="+videoId);
				if (videoId == 10) {
					videoName = "jieyouxi.mp4";
				}else if (videoId == 11){
					videoName = "chaoti.mp4";
				}
				
				Log.i(TAG, "videoName ==== " + videoName);
				String pa=FindFileOnUSB("HomeDemoVideo/"+videoName);
//				String pa=FindFileOnUSB(videoName);
				Log.i("pa", "========"+pa);
				mUri = Uri.parse(pa);
				if(mUri == null) {
					Toast.makeText(mContext, "未找到此片源", Toast.LENGTH_SHORT).show();
					finish();
				}
				// file:///storage/external_storage/sda1/fengkuang.rmvb
				Log.d(TAG, "====urimovie20141====" + mUri);
				//getIntent().putExtra("localvideoplay", false);
			

		// file:///storage/external_storage/sda1/fengkuang.rmvb
		Log.d(TAG, "====urimovie20141====" + mUri);
		// getIntent().putExtra("localvideoplay", false);
		// movieind = R.raw.movie201411;

		// }

	}

	private static String FindFileOnUSB(String filename) {
		// TODO Find File On USB function
		String filepath = "";
//		File usbroot = new File("/mnt/usb/");
		File usbroot = new File("/storage/external_storage/");
		File targetfile;
		if (usbroot != null && usbroot.exists()) {
			File[] usbitems = usbroot.listFiles();
			int sdx = 0;
			for (; sdx < usbitems.length; sdx++) {
				if (usbitems[sdx].isDirectory()) {
					targetfile = new File(usbitems[sdx].getPath() + "/"
							+ filename);
					if (targetfile != null && targetfile.exists()) {
						filepath = usbitems[sdx].getPath() + "/" + filename;
						break;
					}
				}
			}
		}
		return filepath;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub Stop video when the activity is
		// pause.
		mPositionWhenPaused = mVideoView.getCurrentPosition();
		mVideoView.stopPlayback();
		Log.d(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (mPositionWhenPaused >= 0) {
			mVideoView.seekTo(mPositionWhenPaused);
			mPositionWhenPaused = -1;
		}
		Log.d(TAG, "onResume");
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub Play Video
		controller = new MediaController(this);
		mVideoView.setMediaController(controller);
		controller.setMediaPlayer(mVideoView);
		
		mVideoView.setVideoURI(mUri);
		mVideoView.start();

		mVideoView.setOnPreparedListener(new OnPreparedListener() { // 循环播放
					@Override
					public void onPrepared(MediaPlayer mp) {
						// TODO Auto-generated method stub
						mp.start();
						mp.setLooping(true);
					}
				});
		Log.d(TAG, "onStart");
		super.onStart();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Log.i("bofang", "====返回按键=====");
		finish();
		super.onBackPressed();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
			if (videoView != null) {
				if (videoView.isPlaying()) {
					videoView.pause();
				}else {
					videoView.start();
				}
				
			}
			return true;
		case KeyEvent.KEYCODE_BACK:
			Log.i("bofang", "====返回按键onkeydown=====");
			
			Intent intentstart = new Intent(this,MusicService.class);
			stopService(intentstart);
			sendBroadcast(new Intent("com.ali.xiami.exit.resume"));
			finish();
			return true;
		default:
			break;
		}

		return super.onKeyDown(keyCode, event);
	}
}