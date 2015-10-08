/**
 * Copyright (c) 2012-2013 Yunzhisheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : MainActivity.java
 * @ProjectName : letvAssistant
 * @PakageName : cn.yunzhisheng.voicetv
 * @Author : Brant
 * @CreateDate : 2013-5-22
 */
package cn.yunzhisheng.vui.assistant.tv;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.assistant.preference.UserPreference;
import cn.yunzhisheng.vui.assistant.util.MessageManager;
import cn.yunzhisheng.vui.assistant.util.Util;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Brant
 * @CreateDate : 2013-5-22
 * @ModifiedBy : Brant
 * @ModifiedDate: 2013-5-22
 * @Modified: 2013-5-22: 实现基本功能
 */
@SuppressLint("NewApi")
public class MainActivity extends Activity implements View.OnClickListener {
	private static final String TAG = "MainActivity";

	public static final String SHARE_PREFERENCE_NAME = "physicsKeyValue";
	public static boolean mVoiceKeyFlag = false;

	public static final int REMOVE_BIND_KEY = 5;
	private UserPreference mUserPreference;
	private RelativeLayout mInputKey;

	private TextView mKeyResult;

	private Button mComfirmKey;
	private boolean mNeedSetKey = false;

	private List<Integer> mUnableKeys = new ArrayList<Integer>();

	private int mKeyCode;

	private int mCenterCount = 1;
	private Timer mTimer;
	private Intent intent;
	private AlphaAnimation mStartAnimation;
	private TextView mAlpha;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, Res.layout.activity_start, null);
		setContentView(view);

		mAlpha = (TextView) view.findViewById(Res.id.alpha_image);

		mUserPreference = new UserPreference(this);

		String mTvName = mUserPreference.getString(
			UserPreference.TV_FRIENDLY_NAME_KEY,
			UserPreference.TV_FRIENDLY_NAME_DEFAULT);
		if (mTvName.equals(UserPreference.TV_FRIENDLY_NAME_DEFAULT)) {
			mUserPreference.putString(UserPreference.TV_FRIENDLY_NAME_KEY, UserPreference.TV_FRIENDLY_NAME_DEFAULT
																			+ Util.getIpEndStr(this));
		}

		mStartAnimation = new AlphaAnimation(0.7f, 0.0f);
		mStartAnimation.setDuration(3000);

		mStartAnimation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mAlpha.setAlpha(0.0f);
				checkFirstStart();
				StartServerTask startServerTaskTask = new StartServerTask();
				startServerTaskTask.execute();
			}
		});

		mAlpha.startAnimation(mStartAnimation);
	}

	private void checkFirstStart() {
		intent = new Intent();
		intent.setClass(this, SettingActivity.class);
		startActivity(intent);
		finish();
	}

	class StartServerTask extends AsyncTask<Integer, Integer, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Integer... params) {

			Intent i = new Intent(MainActivity.this, WindowService.class);
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

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		Log.d(TAG, "dispatchKeyEvent:event " + event + "mVoiceKeyFlag: " + mVoiceKeyFlag + " mNeedSetKey: "
					+ mNeedSetKey);
		if (mNeedSetKey) {
			if (!mUnableKeys.contains(event.getKeyCode())) {
				String code = (event + "").substring((event + "").indexOf("{")).split(",")[1];
				mKeyResult.setText(code);
				mKeyCode = event.getKeyCode();
				mComfirmKey.setEnabled(true);
				mComfirmKey.requestFocus();
			}

		} else {
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				if (event.getKeyCode() == mUserPreference.getInt(UserPreference.VOICE_KEY, -1)) {
					Log.e(TAG, "VOICE_KEY ...  : " + mUserPreference.getInt(UserPreference.VOICE_KEY, -1));
					if (!mVoiceKeyFlag) {
						mVoiceKeyFlag = true;
						String message = "";
						if (MessageReceiver.ACTION_START_TALK_ARRAY != null
							&& MessageReceiver.ACTION_START_TALK_ARRAY.length > 0) {
							message = MessageReceiver.ACTION_START_TALK_ARRAY[0];
						}
						if (message != null && !message.equals("")) {
							sendMessage(message);
						}
					}
					return true;
				}
				// 语音键解锁设置
				if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
					Log.e(TAG, "unbind key ... mCenterCount: " + mCenterCount);
					if (mCenterCount >= REMOVE_BIND_KEY) {
						Toast.makeText(MainActivity.this, "语音键已解除绑定，请重启程序进行绑定", Toast.LENGTH_SHORT).show();
						mUserPreference.putBoolean(UserPreference.NEED_SET_KEY, true);
					} else {
						mCenterCount++;
						mTimer = new Timer();
						mTimer.schedule(new TimerTask() {

							@Override
							public void run() {
								mCenterCount = 1;
								mTimer.cancel();
							}
						}, 3500);
					}
					return true;
				}
			}
		}
		return super.dispatchKeyEvent(event);
	}

	private void sendMessage(String action) {
		MessageManager.sendCategoryMessage(getApplicationContext(), "cn.yunzhisheng.intent.category.RECOGNIZE", action);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == Res.id.confirm_key) {
			mUserPreference.putInt(UserPreference.VOICE_KEY, mKeyCode);
			mInputKey.setVisibility(View.GONE);
			mUserPreference.putBoolean(UserPreference.NEED_SET_KEY, false);
			mNeedSetKey = false;
		} else if (id == Res.id.skip_set) {
			showDialog();
		}
	}

	private void showDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("温馨提示");
		dialog.setMessage("如果遥控器没有语音键，建议您使用手机遥控器");
		dialog.setPositiveButton("Sure", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				mUserPreference.putBoolean(UserPreference.NEED_SET_KEY, false);
				mInputKey.setVisibility(View.GONE);
				mNeedSetKey = false;
			}
		});
		dialog.show();
	}
}
