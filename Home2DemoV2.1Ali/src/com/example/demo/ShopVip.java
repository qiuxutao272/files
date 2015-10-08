package com.example.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import cn.yunzhisheng.voicetv.R;

public class ShopVip extends Activity {

	int[] images = new int[] {R.drawable.on, R.drawable.two,
			R.drawable.three, R.drawable.four, R.drawable.five, R.drawable.six,
			R.drawable.seven, R.drawable.eight, R.drawable.nine, };
	int currentImage = 0;
	Handler handler;
	public ImageView image;
	public long exitTime=0;
	Toast toast;
	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_shop);
		
		context=this;
		
		toast=new Toast(context);
		handler = new Handler();
		// 获取LinearLayout布局器
		LinearLayout main = (LinearLayout) this.findViewById(R.id.root);
		// 创建ImageView组件
		image = (ImageView) findViewById(R.id.image);
		// 将image组件添加到layout中

		// 初始化显示第一张图片
		image.setImageResource(images[0]);
		// 给图片添加点击侦听事件
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

		handler.postDelayed(runnable, 2000);

	}
	private boolean isLongPressKey=true;
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				
				// 改变ImageView图片里的内容
				
					if (currentImage == 8) {
						if((System.currentTimeMillis()-exitTime) > 2000){  
							toast = Toast.makeText(getApplicationContext(),
								     "演示完成！再按一次返回上一层", Toast.LENGTH_LONG);
								   toast.setGravity(Gravity.TOP, 0, 0);
								   toast.show();
				            //Toast.makeText(getApplicationContext(), "演示完成！再按一次确定退出程序", Toast.LENGTH_SHORT).show();                                
				            exitTime = System.currentTimeMillis();   
				        } else {
				            finish();
				            
				        }
					} else {
						image.setImageResource(images[++currentImage]);
					}
				
				
				

				return true;
			}

		case KeyEvent.KEYCODE_BACK:

			if (currentImage == 1) {
				if((System.currentTimeMillis()-exitTime) > 2000){  
					toast = Toast.makeText(getApplicationContext(),
						     "再按一次返回上一层", Toast.LENGTH_LONG);
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

			if (currentImage == 1) {
				if((System.currentTimeMillis()-exitTime) > 2000){  
					toast = Toast.makeText(getApplicationContext(),
						     "再按一次返回上一层", Toast.LENGTH_LONG);
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

			
				if (currentImage == 8) {
					if((System.currentTimeMillis()-exitTime) > 2000){  
						toast = Toast.makeText(getApplicationContext(),
							     "演示完成！再按一次返回上一层", Toast.LENGTH_LONG);
							   toast.setGravity(Gravity.TOP, 0, 0);
							   toast.show();
			            //Toast.makeText(getApplicationContext(), "演示完成！再按一次确定退出程序", Toast.LENGTH_SHORT).show();                                
			            exitTime = System.currentTimeMillis();   
			        } else {
			            finish();
			            
			        }
				} else {
					image.setImageResource(images[++currentImage]);
				}
			

			return true;
			
		case 227://yu yin an jian
			if(event.getAction()==KeyEvent.ACTION_DOWN)
			{	    	
	    		if(isLongPressKey==true)
	    		{
	    		Intent openvoice = new Intent();
	  			openvoice.setAction("cn.yunzhisheng.intent.voice.start");
	  			sendBroadcast(openvoice);	
	  			isLongPressKey=false;
	  			}
	    			  
			}
			return true;
		default:
			break;
		}

	
		
		return false;

	};
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==227&&event.getAction()==KeyEvent.ACTION_UP){
			Intent closedvoice = new Intent();
			closedvoice.setAction("cn.yunzhisheng.intent.voice.stop");
			sendBroadcast(closedvoice);
			isLongPressKey=true;
		}
		return super.onKeyUp(keyCode, event);
	}
	public void dialog() {
		new AlertDialog.Builder(this)
				.setTitle("提示")
				.setMessage("确定要退出购物吗?")				
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						
						finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// 取消按钮事件
						
						
					}
				}).show();
	}

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (currentImage >= images.length - 1) {
				currentImage = -1;
			}
			// 改变ImageView图片里的内容
			image.setImageResource(images[++currentImage]);
		}
	};

}
