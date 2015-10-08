package cn.yunzhisheng.vui.assistant.tv;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.yunzhisheng.vui.assistant.Res;

public class GuideActivity extends Activity implements OnPageChangeListener {
	public static final String MSG_VIRTUAL_KEY_BACK = "MSG_VIRTUAL_KEY_BACK";
	public static final String MSG_VIRTUAL_KEY_CENTER = "MSG_VIRTUAL_KEY_ENTER";
	public static final String MSG_VIRTUAL_KEY_LEFT = "MSG_VIRTUAL_KEY_LEFT";
	public static final String MSG_VIRTUAL_KEY_RIGHT = "MSG_VIRTUAL_KEY_RIGHT";
	private ViewPager viewPager;
	private MyPagerAdapter adapter;
	private List<View> views;
	public static final int[] pics = { Res.drawable.mobile01, Res.drawable.mobile02, Res.drawable.mobile03,
		Res.drawable.mobile04 };
	// 底部小点
	private ImageView[] dots;
	// 记住当前位置
	private int current;

	private static GuideActivity activity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(Res.layout.help_guide_view);
		activity = this;
		views = new ArrayList<View>();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.MATCH_PARENT);
		for (int i = 0; i < pics.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(params);
			// iv.setBackgroundResource(pics[i]);
			iv.setImageBitmap(readBitMap(this, pics[i]));
			views.add(iv);
		}
		viewPager = (ViewPager) findViewById(Res.id.help_view_pager);
		Log.i("life", "" + viewPager);
		adapter = new MyPagerAdapter(views);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);
		init();
	}

	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	private void init() {
		LinearLayout ll = (LinearLayout) findViewById(Res.id.help_lay);
		// ll.setOnKeyListener(this);
		dots = new ImageView[pics.length];
		for (int i = 0; i < pics.length; i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);
			dots[i].setTag(i);
		}
		current = 0;
		dots[current].setEnabled(false);
	}

	public static void finishActivity() {
		if (activity != null) {
			activity.finish();
			activity = null;
		}
	}

	/*
	 * private void setViews(int position) { if (position < 0 || position >=
	 * pics.length) { return; } viewPager.setCurrentItem(position); }
	 */

	private void setDots(int position) {
		if (position < 0 || position > pics.length - 1 || current == position) {
			return;
		}
		dots[position].setEnabled(false);
		dots[current].setEnabled(true);
		current = position;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		setDots(arg0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			if (current == 0) {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
