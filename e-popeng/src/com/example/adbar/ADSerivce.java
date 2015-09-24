package com.example.adbar;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import com.hrtvbic.adpop.MyDockPanelEng;
import com.hrtvbic.adpop.R;

public class ADSerivce extends Service {
	ActivityManager am;
	private SharedPreferences sps;
	private Editor editors;
	static ComponentName cn;
	static ComponentName temp;
	static long time = 60000;
	static long countTime = 0;
	static long index = 10000;
	static Timer timer;
	static String style;
	private MyDockPanelEng panel = new MyDockPanelEng(this);
	private Context context;
	// 白名单
	private String[] whiteList;
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			try {

				timer.cancel();
				timer = null;
			} catch (Exception e) {
				// TODO: handle exception
			}
			addTiemr();
		}
	};

	public void onDestroy() {
		this.unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		sps = ADSerivce.this.getSharedPreferences("guanggao", MODE_PRIVATE);
		SharedPreferences sharedPreferences = getSharedPreferences("test",
				Activity.MODE_PRIVATE);
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		cn = am.getRunningTasks(1).get(0).topActivity;
		Log.d("adServic", "onStart.....");
		whiteList = getResources().getStringArray(R.array.whiteList);
		panel = new MyDockPanelEng(this);
		try {

			timer.cancel();
			timer = null;
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (sps.getInt("START", 1) == 1) {

			panel.start();
		} else {

			addTiemr();
		}

		// String pkString = cn.getPackageName();
		// if (whiteList.length != 0) {
		// for (int i = 0; i < whiteList.length; i++) {
		// boolean a = pkString.contains(whiteList[i]);
		// if (a) {
		//
		//
		// Log.d("mogu", "这个程序在白名单中");
		// }else{
		// addTiemr();
		// }
		// }
		// }

		time = Integer.valueOf(sharedPreferences.getString("time", ""));

		style = sharedPreferences.getString("style", "");
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		Log.d("adServic", "onBind.....");
		Intent i = arg0;
		Bundle b = i.getExtras();
		time = b.getInt("time");
		style = b.getString("style");
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.d("adServic", "onCreate.....");
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("adServic", "onStartCommand.....");
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		cn = am.getRunningTasks(1).get(0).topActivity;
		// addTiemr();

		IntentFilter filter = new IntentFilter("com.my.ad.bar");
		this.registerReceiver(broadcastReceiver, filter);

		return super.onStartCommand(intent, flags, startId);
	}

	public void addTiemr() {
		if (timer == null) {
			timer = new Timer();

		}
		countTime = 0;
		cn = am.getRunningTasks(1).get(0).topActivity;
		timer.schedule(new MyTimerTask(), index, index);
	}

	class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			temp = am.getRunningTasks(1).get(0).topActivity;
			Log.e("TAG", "pkg = " + temp.getPackageName() + "---ClassName = "
					+ temp.getClassName());
			if (style.equals("0")) {
				countTime += index;
				Log.e("TAG", "countTime000000000000000 = " + countTime);
				Log.e("TAG", "time......." + time);
				if (countTime == time) {
					Log.d("mogu", "启动了");
					timer.cancel();
					timer = null;
					countTime = 0;
					// Intent intent = new Intent(ADSerivce.this,
					// MainActivity.class);
					// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					// startActivity(intent);
					Log.d("mogu", "位置一" + cn.getPackageName());

					Log.d("mogu", "位置一:第二次启动");
					if (sps.getInt("KAIGUAN", 1) == 1) {
						panel.start();
					}

				}

				return;

			}
			String pkString = cn.getPackageName();
			boolean zhuangtai = false;

			if (whiteList.length != 0) {
				for (int i = 0; i < whiteList.length; i++) {
					boolean a = pkString.contains(whiteList[i]);
					if (a) {
						zhuangtai = true;
						Log.d("mogu", "这个程序在白名单中");
					}
				}
			}
			if (zhuangtai) {
				if (cn.getClassName().equals(temp.getClassName())) {
					countTime += index;
					Log.e("TAG", "countTime11111111111 = " + countTime);
					if (countTime == time) {
						timer.cancel();
						timer = null;
						countTime = 0;
						// Intent intent = new Intent(ADSerivce.this,
						// MainActivity.class);
						// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						// startActivity(intent);
						// ActivityManager am = (ActivityManager)
						// getSystemService(ACTIVITY_SERVICE);
						// ComponentName cn =
						// am.getRunningTasks(1).get(0).topActivity;
						// String packageName = cn.getPackageName();
						// Log.d("mogu", "2" + packageName);
						// boolean zhuangtai = true;
						// for (int i = 0; i < whiteList.length; i++) {
						// boolean a = packageName.contains(whiteList[i]);
						// if (a) {
						// zhuangtai = false;
						// }
						// }
						// if (zhuangtai) {

						Log.d("mogu", "位置二:第二次启动");
						if (sps.getInt("KAIGUAN", 1) == 1) {
							panel.start();
						}

						// }else{
						// Log.d("mogu", "这个程序在白名单中");
						// }

					}
				} else {
					Log.e("TAG", "countTime11111111111111 = " + countTime);
					countTime = 0;
				}
			} else {
				Log.e("TAG", "countTime11111111111 = " + countTime);
				countTime = 0;
			}
			cn = temp;
		}

	}

	public void startAd() {

	}

}
