package cn.yunzhisheng.vui.assistant.tv.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class FloatView extends LinearLayout {

	protected WindowManager.LayoutParams mWindowParams = new WindowManager.LayoutParams();
	protected WindowManager mWindowManager;

	private boolean mIsShowing = false;
	protected Point mWindowSize = new Point();

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public FloatView(Context context) {
		super(context);
		mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = mWindowManager.getDefaultDisplay();
		if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
			mWindowSize.y = display.getHeight();
			mWindowSize.x = display.getWidth();
		} else {
			display.getSize(mWindowSize);
		}
	}

	public boolean isShowing() {
		return mIsShowing;
	}

	public void show() {
		if (mIsShowing) {
			return;
		}
		mIsShowing = true;
		mWindowManager.addView(this, mWindowParams);
	}

	public void hide() {
		if (mIsShowing) {
			mIsShowing = false;
			mWindowManager.removeView(this);
		}
	}
}
