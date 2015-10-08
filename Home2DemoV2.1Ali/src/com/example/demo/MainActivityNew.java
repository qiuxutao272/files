package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import cn.yunzhisheng.voicetv.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivityNew extends Activity implements OnPageChangeListener{
	private ViewPager viewPager;
	private GuideAdapter adapter;
	long touchTime = 0;
	long waitTime = 2000;
//	private LinearLayout dotContain;
//	private Button btnSure;
	private List<View> mViews=new ArrayList<View>();
	private int[] mResIds=new int[]{
			R.drawable.ali1,
			R.drawable.ali2,
			R.drawable.ali3,
			R.drawable.ali4,
			R.drawable.ali5,
			R.drawable.ali6,
			R.drawable.ali7,
			R.drawable.ali8,
			R.drawable.ali9,
			R.drawable.ali10,
			R.drawable.ali11,
			R.drawable.ali12,
			R.drawable.ali13,
			R.drawable.ali14,
			R.drawable.ali15,
			R.drawable.ali16,
			R.drawable.ali17
			};
/*	private int[] mResIds=new int[]{
			R.drawable.g51_1,
			R.drawable.g51_2,
			R.drawable.g51_3,
			R.drawable.g51_4,
			R.drawable.g51_5,
			R.drawable.g51_6,
			R.drawable.g51_7,
			R.drawable.g51_8,
			R.drawable.g51_9,
			R.drawable.g51_10,
			R.drawable.g51_11
			};*/
/*	private int[] mResIds=new int[]{
			R.drawable.u51_1,
			R.drawable.u51_2,
			R.drawable.u51_3,
			R.drawable.u51_4,
			R.drawable.u51_5,
			R.drawable.u51_6,
			R.drawable.u51_7,
			R.drawable.u51_8,
			R.drawable.u51_9,
			R.drawable.u51_10,
			R.drawable.u51_11			
			};*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.multiscreen_viewpager);
		initViews();
		initDatas();
	}
	protected void initViews() {
		viewPager=(ViewPager) findViewById(R.id.multiscreen_viewpager);
//		dotContain=(LinearLayout) findViewById(R.id.layout_dot_contain);
//		btnSure=(Button) findViewById(R.id.btn_sure);
		
		adapter=new GuideAdapter(mViews);
	}

	protected void initDatas() {
		
		for (int i = 0; i < mResIds.length; i++) {
			ImageView img=new ImageView(this);
			//延迟设置图片，在PagerAdapter内设置，解决OOM问题
			/*ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(  
	                ViewGroup.LayoutParams.MATCH_PARENT,  
	                ViewGroup.LayoutParams.MATCH_PARENT);
			img.setBackgroundResource(mResIds[i]);
			img.setLayoutParams(params);*/
			mViews.add(img);
		}
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);
		viewPager.setCurrentItem(0);
		viewPager.setOffscreenPageLimit(1);
//		dotContain.getChildAt(0).setSelected(true);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		long currentTime = System.currentTimeMillis();
		if ((currentTime - touchTime) >= waitTime) {
			Toast.makeText(this, R.string.exit_app, Toast.LENGTH_SHORT).show();
			touchTime = currentTime;
		} else {
			
			finish();
			return;
		}
//		super.onBackPressed();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == 227 && event.getAction() == KeyEvent.ACTION_DOWN) {
			if (Appliaction.isLongPressKey == true) {
				Intent openvoice = new Intent();
				Log.d("bill", "onKeyDown227!");
				openvoice.setAction("cn.yunzhisheng.intent.voice.start");
				sendBroadcast(openvoice);
				Appliaction.isLongPressKey = false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}    
    

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.d("bill", "event===" + keyCode);
		if (keyCode == 227 && event.getAction() == KeyEvent.ACTION_UP) {
			Log.d("bill", "onKeyUp!");
			Intent closedvoice = new Intent();
			closedvoice.setAction("cn.yunzhisheng.intent.voice.stop");
			sendBroadcast(closedvoice);
			Appliaction.isLongPressKey = true;
		}
		return super.onKeyUp(keyCode, event);
	}
	
	/**
	 * 
	 * 2014-12-19 上午10:56:19
	 * @param position
	 * @TODO 改变底部图标状态
	 */
//	private void chageDotState(final int position){
//		int count=dotContain.getChildCount();
//		for (int i = 0; i < count; i++) {
//			View view=dotContain.getChildAt(i);
//			if(position%count==i){
//				view.setSelected(true);
//			}else{
//				view.setSelected(false);
//			}
//		}
//	}
	/**
	 * 
	 * @Create_date 2014-12-19 上午11:09:48
	 * @TODO 适配器
	 */
	class GuideAdapter extends PagerAdapter{
		private List<View> views;
		
		public GuideAdapter(List<View> views) {
			this.views = views;
		}

		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0==arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(views.get(position));
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			//在此设置背景图片，提高加载速度，解决OOM问题
			View view=views.get(position);
			int count=getCount();
			ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(  
	                ViewGroup.LayoutParams.MATCH_PARENT,  
	                ViewGroup.LayoutParams.MATCH_PARENT);
			view.setBackgroundResource(mResIds[position%count]);
			view.setLayoutParams(params);
			container.addView(view,0);
			return views.get(position);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}
	@Override
	public void onPageSelected(int arg0) {
//		if(arg0<adapter.getCount()-1){
//			dotContain.setVisibility(View.VISIBLE);
//			btnSure.setVisibility(View.GONE);
//			chageDotState(arg0);
//		}else{
//			dotContain.setVisibility(View.GONE);
//			btnSure.setVisibility(View.VISIBLE);
//		}
	}
}