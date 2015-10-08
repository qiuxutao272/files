package com.example.demo;

import java.io.File;

import cn.yunzhisheng.proguard.el;
import cn.yunzhisheng.voicetv.R;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends Activity {

	private VideoView videoView = null;
	private MediaController controller = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/Test.mp4");
		Uri uri = Uri.parse(FindFileOnUSB("HomeDemoVideo/"+"gangtie2.mp4"));
		Log.v("URI", uri.toString());
		if (uri == null) {
			Toast.makeText(this, "未找到片源", Toast.LENGTH_SHORT).show();
		} else {
			videoView = (VideoView) this
					.findViewById(R.id.video_view);
			controller = new MediaController(this);
			videoView.setMediaController(controller);
			controller.setMediaPlayer(videoView);
			videoView.setVideoURI(uri);
			videoView.start();
			videoView.requestFocus();
			videoView.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();
					mp.setLooping(true);
				}
			});

			
		}
	}

	private static String FindFileOnUSB(String filename) {
		// TODO Find File On USB function
		String filepath = "";
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
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			Intent intent = new Intent(MainActivity.this, MainActivityNew.class);
			startActivity(intent);
			finish();
			return true;
			
		default:
			break;
			
		}
		return super.onKeyDown(keyCode, event);
	}
}
