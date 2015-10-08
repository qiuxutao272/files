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

public class YuYinHighQingJiaoYu2 extends Activity {
	ArrayList<String> packagNameList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pyyhighyouerjiaoyu2);
		Appliaction.YuYinHighQingJiaoYu2 = this;
		initpackagNameList();
		StartServerTask startServerTaskTask = new StartServerTask();
		startServerTaskTask.execute();
	}
	 private void initpackagNameList() {
		  packagNameList = new ArrayList<String>();
		  PackageManager manager = this.getPackageManager();
		  List<PackageInfo> pkgList = manager.getInstalledPackages(0);
		  for (int i = 0; i < pkgList.size(); i++) {
		   PackageInfo pI = pkgList.get(i);
		   packagNameList.add(pI.packageName.toLowerCase());
		  }

		 }

	 private boolean detectApk(String packageName) {
	  return packagNameList.contains(packageName.toLowerCase());

	 }
	 public void showInstallConfirmDialog(final Context context,
	   final String filePath) {
	  AlertDialog.Builder tDialog = new AlertDialog.Builder(context);
	  tDialog.setIcon(R.drawable.icon);
	  tDialog.setTitle("未安装该程序");
	  tDialog.setMessage("请安装该程序");

	  tDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

	   public void onClick(DialogInterface dialog, int which) {
	    try {
	     String command = "chmod " + "777" + " " + filePath;
	     Runtime runtime = Runtime.getRuntime();
	     runtime.exec(command);
	    } catch (IOException e) {
	     e.printStackTrace();
	    }
	    // install the apk.
	    Intent intent = new Intent(Intent.ACTION_VIEW);
	    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    intent.setDataAndType(Uri.parse("file://" + filePath),
	      "application/vnd.android.package-archive");
	    context.startActivity(intent);
	   }
	  });
	  tDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {

		   public void onClick(DialogInterface dialog, int which) {
		   }
		  });

		  tDialog.show();
		 }
	 public boolean retrieveApkFromAssets(Context context, String fileName,
	   String path) {
	  boolean bRet = false;

	  try {
	   File file = new File(path);
	   if (file.exists()) {
	    return true;
	   } else {
	    file.createNewFile();
	    InputStream is = context.getAssets().open(fileName);
	    FileOutputStream fos = new FileOutputStream(file);

	    byte[] temp = new byte[1024];
	    int i = 0;
	    while ((i = is.read(temp)) != -1) {
	     fos.write(temp, 0, i);
	    }
	    fos.flush();
	    fos.close();
	    is.close();

	    bRet = true;
	   }

	  } catch (IOException e) {
	   Toast.makeText(context, e.getMessage(), 2000).show();
	   Builder builder = new Builder(context);
	   builder.setMessage(e.getMessage());
	   builder.show();
	   e.printStackTrace();
	  }

	  return bRet;
	 }
	private static String FindFileOnUSB(String filename)
	{
		// TODO Find File On USB function
		String filepath = "";
		File usbroot = new File("/storage/external_storage/");
		File targetfile;
		if (usbroot != null && usbroot.exists())
		{
			 File[] usbitems  = usbroot.listFiles();
			 int sdx = 0;
			 for(; sdx < usbitems.length; sdx++)
			 {
				 if (usbitems[sdx].isDirectory())
				 {
					 targetfile = new File(usbitems[sdx].getPath() + "/" + filename);
					 if (targetfile != null && targetfile.exists())
					 {
						 filepath = usbitems[sdx].getPath() + "/" + filename;
						 break;
					 }
				 }
			 }
		}
		return filepath;
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
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
		{
			Intent intentright = new Intent(YuYinHighQingJiaoYu2.this,YuYinHighQingJiaoYu1.class);  
    		startActivity(intentright);  
           	finish();
    		Log.v("URI", "left");
			return true;
		}
		case KeyEvent.KEYCODE_DPAD_RIGHT:
//			Intent intentright = new Intent(YuYinHighQingJiaoYu2.this,picture4K5.class);  
//    		startActivity(intentright);  
           	finish();
    		Log.v("URI", "right");
			return true;
		case KeyEvent.KEYCODE_DPAD_CENTER:
    		boolean installed = detectApk("com.cpsoft.youjiao.ma01");

		    if (installed) {
		     Log.d("time", "getPackageManager start "
		       + System.currentTimeMillis() + "");
		     Intent intent = new Intent();
		     intent.setComponent(new ComponentName("com.cpsoft.youjiao.ma01",
		       "com.unity3d.player.UnityPlayerNativeActivity"));
		     intent.setAction(Intent.ACTION_VIEW);

		     Log.d("time", "setAction start "
		       + System.currentTimeMillis() + "");
		     startActivity(intent);
	    		
		    } else {
		     String path=FindFileOnUSB("wanjudian.apk");
		     Log.v("bill", path);
		     retrieveApkFromAssets(YuYinHighQingJiaoYu2.this,
		       "wanjudian.apk", path);
		     showInstallConfirmDialog(YuYinHighQingJiaoYu2.this, path);
		    }
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

			Intent i = new Intent(YuYinHighQingJiaoYu2.this, WindowService.class);
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
