package com.example.demo;

import cn.yunzhisheng.voicetv.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Bailunxie extends Activity {

	int[] images = new int[] { R.drawable.bailun1, R.drawable.bailun2,
			R.drawable.bailun3, R.drawable.bailun4 };

	int currentImage = 0;
	Handler handler;
	public ImageView image;
	public long exitTime = 0;
	Toast toast;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_shop);

		context = this;

		toast = new Toast(context);
		handler = new Handler();
		// 获取LinearLayout布局器
		LinearLayout main = (LinearLayout) this.findViewById(R.id.root);
		// 创建ImageView组件
		image = (ImageView) findViewById(R.id.image);
		// 将image组件添加到layout中

		// 初始化显示第一张图片
		image.setImageResource(images[0]);

		image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (currentImage >= images.length - 1) {
					currentImage = -1;
				}
				// 改变ImageView图片里的内容
				image.setImageResource(images[++currentImage]);
			}
		});

	}

	private boolean isLongPressKey = true;

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			if (event.getAction() == KeyEvent.ACTION_DOWN) {

				// 改变ImageView图片里的内容

				if (currentImage == 3) {
					if ((System.currentTimeMillis() - exitTime) > 2000) {
						toast = Toast.makeText(getApplicationContext(),
								"再按一次返回!", Toast.LENGTH_LONG);
						toast.setGravity(Gravity.TOP, 0, 0);
						toast.show();
						// Toast.makeText(getApplicationContext(),
						// "演示完成！再按一次确定退出程序", Toast.LENGTH_SHORT).show();
						exitTime = System.currentTimeMillis();
					} else {
						// finish();
						image.setImageResource(images[0]);
						currentImage = 0;
					}
				} else {
					image.setImageResource(images[++currentImage]);
				}

				return true;
			}

		case KeyEvent.KEYCODE_BACK:

			if (currentImage == 0) {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					toast = Toast.makeText(getApplicationContext(), "再按一次返回!",
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP, 0, 0);
					toast.show();
					exitTime = System.currentTimeMillis();
				} else {
					finish();

				}
			} else {
				image.setImageResource(images[--currentImage]);
			}

			return true;

		case KeyEvent.KEYCODE_DPAD_LEFT:

			if (currentImage == 0) {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					toast = Toast.makeText(getApplicationContext(), "再按一次返回!",
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP, 0, 0);
					toast.show();
					exitTime = System.currentTimeMillis();
				} else {
					finish();

				}
			} else {
				image.setImageResource(images[--currentImage]);
			}

			return true;

		case KeyEvent.KEYCODE_DPAD_RIGHT:

			if (currentImage == 3) {
				if ((System.currentTimeMillis() - exitTime) > 2000) {
					toast = Toast.makeText(getApplicationContext(), "再按一次返回!",
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.TOP, 0, 0);
					toast.show();
					// Toast.makeText(getApplicationContext(),
					// "演示完成！再按一次确定退出程序", Toast.LENGTH_SHORT).show();
					exitTime = System.currentTimeMillis();
				} else {
					// finish();
					image.setImageResource(images[0]);
					currentImage = 0;

				}
			} else {
				image.setImageResource(images[++currentImage]);
			}
			return true;
		default:
			break;
		}
		return false;

	};
}
