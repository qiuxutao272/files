/**
 * Copyright (c) 2012-2013 Yunzhisheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : WindowContainer.java
 * @ProjectName : letvAssistant
 * @PakageName : cn.yunzhisheng.voicetv.view
 * @Author : Brant
 * @CreateDate : 2013-5-24
 */
package cn.yunzhisheng.vui.assistant.tv.view;
import cn.yunzhisheng.common.util.LogUtil;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Brant
 * @CreateDate : 2013-5-24
 * @ModifiedBy : Brant
 * @ModifiedDate: 2013-5-24
 * @Modified: 2013-5-24: 实现基本功能
 */
public class WindowPopupDialog extends Dialog {
	private static final String TAG = "WindowPopupDialog";

	private BroadcastReceiver mDismissReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
				dismiss();
			}
		}
	};

	public WindowPopupDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public WindowPopupDialog(Context context, int theme) {
		super(context, theme);
		Window window = getWindow();
		window.setGravity(Gravity.RIGHT);
		WindowManager.LayoutParams params = window.getAttributes();
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
		//params.type = mWindowType;
		//params.type = 2010;
		//params.flags = (0x8 | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL  | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);*/
		window.setAttributes(params);
	}

	public WindowPopupDialog(Context context) {
		this(context, 0);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d(TAG, "onCreate");
		Log.d("SHOW_TIME", "Dialog onCreate");
	}

	@Override
	protected void onStart() {
		super.onStart();
		LogUtil.d(TAG, "onStart");
		Log.d("SHOW_TIME", "Dialog onStart");
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		getContext().getApplicationContext().registerReceiver(mDismissReceiver, filter);
	}

	@Override
	public void onDetachedFromWindow() {
		LogUtil.d(TAG, "onDetachedFromWindow");
		super.onDetachedFromWindow();
		getContext().getApplicationContext().unregisterReceiver(mDismissReceiver);
	}
}
