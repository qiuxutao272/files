package com.example.demo;

import java.util.HashMap;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import cn.yunzhisheng.voicetv.R;

public class Shop_Activity extends Activity implements OnClickListener{

	private View  topbig, toplit1, toplit2, 
			bottom1, bottom2, bottom3, bottom4,bottom5, intopbig,
			focusview;
	//private int []TextViewID={R.id.lefttab1,R.id.lefttab2,R.id.lefttab3,R.id.lefttab4,R.id.lefttab5,R.id.lefttab6,R.id.lefttab7};
	private int title=R.id.title;
	private HashMap<View, ViewItemPos> initViewPos;
	private int bg9WidthPix = 0;
	private int bg9HeightPix = 0;
	private int bg9TopPix = 0;
	private float scale = 0.06f;
	private float scaletop = 0.03f;
	private int roundCorner = 10;
	private int source;
	private TextView txttitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置无标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 设置全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_filmlist2);
		DisplayMetrics dMetrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		display.getMetrics(dMetrics);
		bg9WidthPix = (int) (dMetrics.density * 26);
		bg9HeightPix = (int) (dMetrics.density * 42);
		bg9TopPix = (int) (dMetrics.density * 14);
		roundCorner = (int) (dMetrics.density * 10);
		findViews();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		initFilm();
		Log.d("MovieActivity","intent");
	}


	private void findViews() {
		
		txttitle=(TextView)findViewById(R.id.title2);
		txttitle.setText(getText(R.string.shop));
		
		topbig = findViewById(R.id.top_big2);
		topbig.setOnFocusChangeListener(new ViewFocusOnchangeListener());
		topbig.setOnClickListener(this);
		toplit1 = findViewById(R.id.top_lit1);
		toplit1.setOnFocusChangeListener(new ViewFocusOnchangeListener());
		toplit1.setOnClickListener(this);
		toplit2 = findViewById(R.id.top_lit2);
		toplit2.setOnFocusChangeListener(new ViewFocusOnchangeListener());
		toplit2.setOnClickListener(this);
		bottom1 = findViewById(R.id.bottom_lit1);
		bottom1.setOnFocusChangeListener(new ViewFocusOnchangeListener());
		bottom1.setOnClickListener(this);
		bottom2 = findViewById(R.id.bottom_lit2);
		bottom2.setOnFocusChangeListener(new ViewFocusOnchangeListener());
		bottom2.setOnClickListener(this);
		bottom3 = findViewById(R.id.bottom_lit3);
		bottom3.setOnFocusChangeListener(new ViewFocusOnchangeListener());
		bottom3.setOnClickListener(this);
		bottom4 = findViewById(R.id.bottom_lit4);
		bottom4.setOnFocusChangeListener(new ViewFocusOnchangeListener());
		bottom4.setOnClickListener(this);
		bottom5 = findViewById(R.id.bottom_lit5);
		bottom5.setOnFocusChangeListener(new ViewFocusOnchangeListener());
		bottom5.setOnClickListener(this);
		intopbig = findViewById(R.id.in_top_big2);
		focusview = findViewById(R.id.focus_view2);
		intopbig.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (intopbig.getMeasuredWidth() > 0) {
							intopbig.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
							initViewPos = new HashMap<View, ViewItemPos>();
							
							initViewPos.put(
									topbig,
									new ViewItemPos(topbig.getRight()
											- topbig.getLeft(), topbig
											.getBottom() - topbig.getTop(),
											topbig.getLeft(), topbig.getTop()));
							initViewPos.put(
									toplit1,
									new ViewItemPos(toplit1.getRight()
											- toplit1.getLeft(), toplit1
											.getBottom() - toplit1.getTop(),
											toplit1.getLeft(), toplit1
													.getTop()));
							initViewPos.put(
									toplit2,
									new ViewItemPos(toplit2.getRight()
											- toplit2.getLeft(), toplit2
											.getBottom() - toplit2.getTop(),
											toplit2.getLeft(), toplit2
													.getTop()));
							
							initViewPos.put(
									bottom1,
									new ViewItemPos(bottom1.getRight()
											- bottom1.getLeft(), bottom1
											.getBottom() - bottom1.getTop(),
											bottom1.getLeft(), bottom1.getTop()));
							initViewPos.put(
									bottom2,
									new ViewItemPos(bottom2.getRight()
											- bottom2.getLeft(), bottom2
											.getBottom() - bottom2.getTop(),
											bottom2.getLeft(), bottom2.getTop()));
							initViewPos.put(
									bottom3,
									new ViewItemPos(bottom3.getRight()
											- bottom3.getLeft(), bottom3
											.getBottom() - bottom3.getTop(),
											bottom3.getLeft(), bottom3.getTop()));
							initViewPos.put(
									bottom4,
									new ViewItemPos(bottom4.getRight()
											- bottom4.getLeft(), bottom4
											.getBottom() - bottom4.getTop(),
											bottom4.getLeft(), bottom4.getTop()));
							initViewPos.put(
									bottom5,
									new ViewItemPos(bottom5.getRight()
											- bottom5.getLeft(), bottom5
											.getBottom() - bottom5.getTop(),
											bottom5.getLeft(), bottom5.getTop()));
							initViewPos.put(
									intopbig,
									new ViewItemPos(intopbig.getRight()
											- intopbig.getLeft(), intopbig
											.getBottom() - intopbig.getTop(),
											intopbig.getLeft(), intopbig
													.getTop()));
						}
					}
				});
		initFilm();
	}
    private void initFilm()
    {
    	
    		source=0;
    	//((TextView)findViewById(title)).setText(R.string.film2);
//    	String[] tabs=getResources().getStringArray(R.array.movietab);
//    	
//    	String[] texts=getResources().getStringArray(R.array.movietext);
    	
    	topbig.setBackgroundResource(R.drawable.shop_1);
    	
    	intopbig.setBackgroundResource(android.R.color.transparent);
    	toplit1.setBackgroundResource(R.drawable.shop_2);
    	toplit2.setBackgroundResource(R.drawable.shop_3);
    	bottom1.setBackground(new BitmapDrawable(getResources(),
				getRoundedCornerBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.shop_bottom1), roundCorner)));
		bottom2.setBackground(new BitmapDrawable(getResources(),
				getRoundedCornerBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.shop_bottom2), roundCorner)));
		bottom3.setBackground(new BitmapDrawable(getResources(),
				getRoundedCornerBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.shop_bottom3), roundCorner)));
		bottom4.setBackground(new BitmapDrawable(getResources(),
				getRoundedCornerBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.shop_bottom4), roundCorner)));
		bottom5.setBackground(new BitmapDrawable(getResources(),
				getRoundedCornerBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.shop_bottom5), roundCorner)));
		
		
    	
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	private boolean isLongPressKey=true;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		else if(keyCode==227&&event.getAction()==KeyEvent.ACTION_DOWN)
		{	    	
    		if(isLongPressKey==true)
    		{
    		Intent openvoice = new Intent();
  		   // Log.d(TAG,"onKeyDown!");
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
		if(keyCode==227&&event.getAction()==KeyEvent.ACTION_UP){
			Intent closedvoice = new Intent();
			closedvoice.setAction("cn.yunzhisheng.intent.voice.stop");
			sendBroadcast(closedvoice);
			isLongPressKey=true;
		}
		return super.onKeyUp(keyCode, event);
	}
	private class ViewFocusOnchangeListener implements
			View.OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				ViewItemPos pos = initViewPos.get(v);
				if (pos == null) {
					focusview.setVisibility(View.INVISIBLE);
				} else {
					if (v == topbig) {
						
						if (focusview.getVisibility() != View.VISIBLE)
							focusview.setVisibility(View.VISIBLE);
						ObjectAnimator
								.ofObject(
										focusview,
										"whxy",
										new WHXYEvaluator(),
										new float[] {
												pos.getWidth() + pos.getWidth()
														* scaletop
														+ bg9WidthPix,
												pos.getHeight()
														+ pos.getHeight()
														* scaletop
														+ bg9HeightPix,
												pos.getLeftMargin()
														- pos.getWidth()
														* scaletop / 2
														- bg9WidthPix / 2,
												pos.getTopMargin()
														- pos.getHeight()
														* scaletop / 2
														- bg9TopPix })
								.setDuration(250).start();
						Animation naAnimation = AnimationUtils.loadAnimation(
								Shop_Activity.this, R.anim.topitemscale);
						naAnimation.setFillAfter(true);
						v.startAnimation(naAnimation);
						ObjectAnimator
								.ofObject(
										intopbig,
										"whxy",
										new WHXYEvaluator(),
										new float[] {
												pos.getWidth() + 2 * scaletop
														* pos.getWidth(),
												pos.getHeight() + 2 * scaletop
														* pos.getHeight(),
												pos.getLeftMargin() - scaletop
														* pos.getWidth(),
												pos.getTopMargin() - 1.5f
														* scaletop
														* pos.getHeight() })
								.setDuration(300).start();
					} else {
						
						if (focusview.getVisibility() != View.VISIBLE)
							focusview.setVisibility(View.VISIBLE);
						ObjectAnimator
								.ofObject(
										focusview,
										"whxy",
										new WHXYEvaluator(),
										new float[] {
												pos.getWidth() + pos.getWidth()
														* scale + bg9WidthPix,
												pos.getHeight()
														+ pos.getHeight()
														* scale + bg9HeightPix,
												pos.getLeftMargin()
														- pos.getWidth()
														* scale / 2
														- bg9WidthPix / 2,
												pos.getTopMargin()
														- pos.getHeight()
														* scale / 2 - bg9TopPix })
								.setDuration(250).start();
						Animation naAnimation = AnimationUtils.loadAnimation(
								Shop_Activity.this, R.anim.itemscale);
						naAnimation.setFillAfter(true);
						v.startAnimation(naAnimation);
					}
				}
			} else {
				ViewItemPos pos = initViewPos.get(v);
				if (pos == null) {

				} else {
					if (v == topbig) {
						Animation naAnimation = AnimationUtils.loadAnimation(
								Shop_Activity.this, R.anim.topitemscaledown);
						naAnimation.setFillAfter(true);
						v.startAnimation(naAnimation);
						ObjectAnimator
								.ofObject(
										intopbig,
										"whxy",
										new WHXYEvaluator(),
										new float[] { pos.getWidth(),
												pos.getHeight(),
												pos.getLeftMargin(),
												pos.getTopMargin() })
								.setDuration(300).start();
					} else {
						Animation naAnimation = AnimationUtils.loadAnimation(
								Shop_Activity.this, R.anim.itemscaledown);
						naAnimation.setFillAfter(true);
						v.startAnimation(naAnimation);
					
					}
				}
			}
		}
	}

//	private class ViewOnClickListener implements View.OnClickListener {
//		@Override
//		public void onClick(View v) {
//				Intent intent = new Intent(EducationActivity.this,
//						ShowMoviePictureActivity.class);
//				intent.putExtra("onlinewatch", source);		
//				startActivity(intent);
//			}
//	}

	public static final Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		int color = 0xff424242;
		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, pixels, pixels, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		bitmap.recycle();
		bitmap = null;
		return output;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.top_big2:
			Intent intent0 = new Intent();
			intent0.setClass(Shop_Activity.this, ShopVip.class);
			intent0.putExtra("gouwu", 0);
			startActivity(intent0);
			break;
		case R.id.top_lit1:
			Intent intent1 = new Intent();
			intent1.setClass(Shop_Activity.this, gouwuActivity.class);
			intent1.putExtra("gouwu", 1);
			startActivity(intent1);
			break;
		case R.id.top_lit2:
			Intent intent2 = new Intent();
			intent2.setClass(Shop_Activity.this, gouwuActivity.class);
			intent2.putExtra("gouwu", 4);
			startActivity(intent2);
			break;
		case R.id.bottom_lit1:
			Intent intent3 = new Intent();
			intent3.setClass(Shop_Activity.this, gouwuActivity.class);
			intent3.putExtra("gouwu", 3);
			startActivity(intent3);
			break;
		case R.id.bottom_lit2:
			Intent intent4 = new Intent();
			intent4.setClass(Shop_Activity.this, gouwuActivity.class);
			intent4.putExtra("gouwu", 5);
			startActivity(intent4);
			break;
		case R.id.bottom_lit3:
			Intent intent5 = new Intent();
			intent5.setClass(Shop_Activity.this, gouwuActivity.class);
			intent5.putExtra("gouwu", 5);
			startActivity(intent5);
			break;
		case R.id.bottom_lit4:
			Intent intent6 = new Intent();
			intent6.setClass(Shop_Activity.this, gouwuActivity.class);
			intent6.putExtra("gouwu", 6);
			startActivity(intent6);
			break;
		case R.id.bottom_lit5:
			Intent intent7 = new Intent();
			intent7.setClass(Shop_Activity.this, gouwuActivity.class);
			intent7.putExtra("gouwu", 7);
			startActivity(intent7);
			break;
	
		default:                               
			break;
		}
	}
}
