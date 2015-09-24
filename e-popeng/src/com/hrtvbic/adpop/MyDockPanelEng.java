package com.hrtvbic.adpop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.webkit.WebView;
import android.widget.ImageView;
import com.ant.liao.GifView;
import com.example.adbar.MessageType;
import com.example.adbar.PopThread;

public class MyDockPanelEng {

	private Context context;
	private WindowManager windowManager = null;
	private WindowManager.LayoutParams windowManagerParams = null;
	View root;
	// GifView iv_gif;
	GifView gif;
	ImageView i;
	ImageView image_top;
	ImageView image_inch;
	private BroadcastReceiver myReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			if (intent.getAction().equals("com.haier.launcher.HOTKEY.TV")) {
				context.sendBroadcast(new Intent("com.my.ad.bar"));
				Log.d("TAG", "信号源");
				stop();
			}

		}

	};

	/**
	 * 鏋勯?鍑芥暟
	 * 
	 * @param context
	 *            Context瀵硅薄
	 */
	public MyDockPanelEng(Context context) {
		this.context = context;
	}

	public void start() {

		new PopThread(handler).start();
		Log.v("sssssssssssssssssssssssss", "sssssssssssssssssss");

	}

	private void createView() {
		// floatView = new GifView(getApplicationContext());//new
		// FloatView(getApplicationContext());
		// floatView.setOnClickListener(this);
		// floatView.setGifImage(R.drawable.gif3);

		root = LayoutInflater.from(context).inflate(R.layout.eng_layout, null);
		// floatView.setImageResource(R.drawable.ic_launcher); //
		
		//for new e-pop
		/*// 这里简单的用自带的icon来做演示
		DisplayMetrics dm = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(dm);
		dm = context.getResources().getDisplayMetrics();

		float density = dm.density;
		density600 = String.valueOf(density);
		wView = (WebView) root.findViewById(R.id.iv_web);
		wView.setHorizontalScrollBarEnabled(false);
		wView.setVerticalScrollBarEnabled(false);
		if (density600.contains("1.0")) {
			Log.d("mogu", "600");
			wView.loadUrl("file:///android_asset/gif.html");
		} else {
			Log.d("mogu", "801");
			wView.loadUrl("file:///android_asset/gif801.html");
		}
		dm = null;
		density = 0;*/

		// gif = (GifView) root.findViewById(R.id.iv_gif1);
		// gif.setGifImage(R.drawable.k_04);

		i = (ImageView) root.findViewById(R.id.iv_main);
		image_top = (ImageView)root.findViewById(R.id.iv_top);
		image_inch = (ImageView)root.findViewById(R.id.iv_inch);

		AnimationDrawable ad = (AnimationDrawable) i.getDrawable();
		ad.start();
		i.setFocusable(true);
		i.setFocusableInTouchMode(true);
		i.setOnKeyListener(keyListener);
//		wView.setOnKeyListener(keyListener);
		
		
		AnimationSet animationset = new AnimationSet(false);
		AlphaAnimation alpha = new AlphaAnimation(0, 1);
//		alpha.setRepeatCount(Animation.INFINITE);
//		alpha.setRepeatMode(Animation.REVERSE);
		animationset.addAnimation(alpha);
		animationset.setDuration(2000);
//		animationset.setStartOffset(3000);
		animationset.setInterpolator(new AccelerateInterpolator());	
		
		image_top.startAnimation(animationset);
		image_top.setFocusable(true);
		image_top.setFocusableInTouchMode(true);
		image_top.setOnKeyListener(keyListener);
		
		image_inch.startAnimation(animationset);
		image_inch.setFocusable(true);
		image_inch.setFocusableInTouchMode(true);
		image_inch.setOnKeyListener(keyListener);
		
		// 获取WindowManager
		windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		// 设置LayoutParams(全局变量）相关参数
		windowManagerParams = new WindowManager.LayoutParams();
		windowManagerParams.type = LayoutParams.TYPE_PHONE; // 设置window type
		windowManagerParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明
		// 设置Window flag

		// windowManagerParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
		// | LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
		 * 注意，flag的值可以为： LayoutParams.FLAG_NOT_TOUCH_MODAL 不影响后面的事件
		 * LayoutParams.FLAG_NOT_FOCUSABLE 不可聚焦 LayoutParams.FLAG_NOT_TOUCHABLE
		 * 不可触摸
		 */
		// 调整悬浮窗口至左上角，便于调整坐标
		// windowManagerParams.gravity = Gravity.RIGHT | Gravity.BOTTOM;
		// 以屏幕左上角为原点，设置x、y初始值
		// windowManagerParams.x = 800;
		// windowManagerParams.y = 0;
		// 设置悬浮窗口长宽数据
		// windowManagerParams.width = 853;
		// windowManagerParams.height =250;
		// 显示myFloatView图像
		windowManager.addView(root, windowManagerParams);
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.haier.launcher.HOTKEY.TV");
		context.registerReceiver(myReceiver, intentFilter);
	}

	// 焦点在搜索结果上时 焦点返回到文本框
	public OnKeyListener keyListener = new OnKeyListener() {

		public boolean onKey(View v, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == event.ACTION_DOWN) {
				context.sendBroadcast(new Intent("com.my.ad.bar"));
				Log.v("sssssssssssssss", "keyListener+sssssssssssssss");
				stop();
				return true;
			}
			return false;
		}
	};

	public void stop() {
		System.out.println("stop");
		if (root != null & root.getParent() != null) {
			windowManager.removeView(root);
			windowManager = null;
			i = null;
			gif = null;
			System.gc();
			Log.e("mogu", "removeview");
		} else {
			// Toast.makeText(mContext, "stop",
			// Toast.LENGTH_SHORT).show();
		}
	}

	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			// 全局搜索
			case MessageType.Pop:
				if (windowManager == null) {
					createView();
				}
				break;

			}
			super.handleMessage(msg);
		}

	};

}
