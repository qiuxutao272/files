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
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.yunzhisheng.common.util.LogUtil;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.assistant.preference.SessionPreferenceOfIntent;
import cn.yunzhisheng.vui.assistant.preference.UserPreference;
import cn.yunzhisheng.vui.assistant.tv.view.SelectNamePopupWindow;
import cn.yunzhisheng.vui.assistant.util.MessageManager;
import cn.yunzhisheng.vui.assistant.util.Util;

@SuppressLint("NewApi")
public class SettingActivity extends Activity implements OnClickListener,OnKeyListener{
	private static final String TAG = "SettingActivity";

	private TextView mConnectNum;
	private TextView mFriendlyName;

	private ImageView mConnectImg;
	private LinearLayout mLinShowGuide;
	private LinearLayout mLinEditName;

	private UserPreference mUserPreference;
	private SelectNamePopupWindow mSelectNamePopupWindow;
	
	public static final String MSG_VIRTUAL_KEY_BACK = "MSG_VIRTUAL_KEY_BACK";
	public static final String MSG_VIRTUAL_KEY_CENTER = "MSG_VIRTUAL_KEY_ENTER";
	public static final String MSG_VIRTUAL_KEY_LEFT = "MSG_VIRTUAL_KEY_LEFT";
	public static final String MSG_VIRTUAL_KEY_RIGHT = "MSG_VIRTUAL_KEY_RIGHT";
	public static final String MSG_VIRTUAL_KEY_UP = "MSG_VIRTUAL_KEY_UP";
	public static final String MSG_VIRTUAL_KEY_DOWN = "MSG_VIRTUAL_KEY_DOWN";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, Res.layout.activity_setting, null);
		setContentView(view);
		registReceiver();
		mUserPreference = new UserPreference(this);
		initView();
		getNameTVList();
	}

	

	private void initView() {
		mConnectNum = (TextView) findViewById(Res.id.connect_mobile_num);
		mFriendlyName = (TextView) findViewById(Res.id.tv_friendlyname);
		mConnectImg = (ImageView) findViewById(Res.id.img_connect_mobile);

		mLinShowGuide = (LinearLayout) findViewById(Res.id.lin_showguide);
		mLinEditName = (LinearLayout) findViewById(Res.id.lin_edit_tv_name);
		mLinShowGuide.setOnClickListener(this);
		mLinEditName.setOnClickListener(this);
		mFriendlyName.setText(mUserPreference.getString(UserPreference.TV_FRIENDLY_NAME_KEY,UserPreference.TV_FRIENDLY_NAME_DEFAULT));
		
		if(WindowService.getService()!=null){
			if( WindowService.getService().getConnectClient() == 0){
				mConnectImg.setImageResource(Res.drawable.ic_no_one);
				mConnectNum.setText("未与手机");
			}else{
				mConnectImg.setImageResource(Res.drawable.ic_n);
				mConnectNum.setText("已与"+WindowService.getService().getConnectClient()+"台手机");
			}
		}else{
			mConnectImg.setImageResource(Res.drawable.ic_no_one);
			mConnectNum.setText("未与手机");
		}
	}

	ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
	
	private void getNameTVList(){
		data.clear();
		data.add(getDataItem("我的电视"+Util.getIpEndStr(SettingActivity.this)));
		data.add(getDataItem("客厅电视1"));
		data.add(getDataItem("客厅电视2"));
		data.add(getDataItem("卧室电视1"));
		data.add(getDataItem("卧室电视2"));
		data.add(getDataItem("卧室电视3"));
		data.add(getDataItem("洗手间电视"));
		data.add(getDataItem("厨房电视"));
		data.add(getDataItem("餐厅电视"));
		data.add(getDataItem("我家的电视"));
		data.add(getDataItem("办公室电视"));
	}
	
	private void getNameMobileList(){
		data.clear();
		data.add(getDataItem(mUserPreference.getString(UserPreference.TV_FRIENDLY_NAME_KEY,UserPreference.TV_FRIENDLY_NAME_DEFAULT)));
		data.add(getDataItem("客厅电视1"));
		data.add(getDataItem("客厅电视2"));
		data.add(getDataItem("卧室电视1"));
		data.add(getDataItem("卧室电视2"));
		data.add(getDataItem("卧室电视3"));
		data.add(getDataItem("洗手间电视"));
		data.add(getDataItem("厨房电视"));
		data.add(getDataItem("餐厅电视"));
		data.add(getDataItem("我家的电视"));
		data.add(getDataItem("办公室电视"));
	}
	
	private ArrayList<HashMap<String, Object>> initNameShowViews() {
		return data;
	}

	private HashMap<String, Object> getDataItem(String tag) {
		HashMap<String, Object> item = new HashMap<String, Object>();
		item.put("Name", tag);
		return item;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			Intent intent = new Intent(this, GuideActivity.class);
			startActivity(intent);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
			if (mLinShowGuide.isFocused()) {
				Intent intent = new Intent(this, GuideActivity.class);
				startActivity(intent);
			} else if (mLinEditName.isFocused()) {
				mSelectNamePopupWindow = new SelectNamePopupWindow(this,
						initNameShowViews());
				mSelectNamePopupWindow
						.setAnimationStyle(Res.style.MusicPoPAnimation);
				mSelectNamePopupWindow.showAtLocation(mLinEditName,
						Gravity.CENTER, 0, 0);
				mSelectNamePopupWindow.setFocusable(true);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		View focusView = null;
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				Intent intent = new Intent(this, GuideActivity.class);
				startActivity(intent);
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				focusView = this.getCurrentFocus();
				if (focusView != null) {
					View nextView = focusView.focusSearch(View.FOCUS_UP);
					if (nextView != null) {
						nextView.requestFocus();
						return true;
					}
				}
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				focusView = this.getCurrentFocus();
				if (focusView != null) {
					View nextView = focusView.focusSearch(View.FOCUS_DOWN);
					if (nextView != null) {
						nextView.requestFocus();
						return true;
					}
				}
				break;
			case KeyEvent.KEYCODE_DPAD_CENTER:
				focusView = this.getCurrentFocus();
				if (focusView != null) {
					focusView.callOnClick();
					return true;
				}
				break;
			case KeyEvent.KEYCODE_BACK:
				if(mSelectNamePopupWindow != null && mSelectNamePopupWindow.isShowing()){
					mSelectNamePopupWindow.dismiss();
				}else{
					finish();
				}
				
				break;
			default:
				break;
			}
		}
		
		return false;
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			LogUtil.d(TAG, "action:  " + action);
			if (SessionPreferenceOfIntent.INTENT_SETTVNAME.equals(action)) {
				mFriendlyName.setText(mUserPreference.getString(UserPreference.TV_FRIENDLY_NAME_KEY,UserPreference.TV_FRIENDLY_NAME_DEFAULT));
			}else if (SessionPreferenceOfIntent.INTENT_CONNECT_CLIENT.equals(action)) {
				mConnectImg.setImageResource(Res.drawable.ic_n);
				if(WindowService.getService().getConnectClient() == 0){
					mConnectImg.setImageResource(Res.drawable.ic_no_one);
					mConnectNum.setText("未与手机");
				}else{
					mConnectImg.setImageResource(Res.drawable.ic_n);
					mConnectNum.setText("已与"+WindowService.getService().getConnectClient()+"台手机");
				}
			}else if (SessionPreferenceOfIntent.INTENT_DISCONNECT_CLIENT.equals(action)) {
				if(WindowService.getService().getConnectClient() == 0){
					mConnectImg.setImageResource(Res.drawable.ic_no_one);
					mConnectNum.setText("未与手机");
				}else{
					mConnectImg.setImageResource(Res.drawable.ic_n);
					mConnectNum.setText("已与"+WindowService.getService().getConnectClient()+"台手机");
				}
			}

			try {
				abortBroadcast();
			} catch (Exception e) {
			} 
		}
	};

	private void registReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(SessionPreferenceOfIntent.INTENT_SETTVNAME);
		filter.addAction(SessionPreferenceOfIntent.INTENT_CONNECT_CLIENT);
		filter.addAction(SessionPreferenceOfIntent.INTENT_DISCONNECT_CLIENT);
		filter = MessageManager.registPrivateIntentFilter(filter, getApplicationContext());
		registerReceiver(mBroadcastReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(mBroadcastReceiver !=null){
			unregisterReceiver(mBroadcastReceiver);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case Res.id.lin_showguide:
			Intent intent = new Intent(this, GuideActivity.class);
			startActivity(intent);
			break;
		case Res.id.lin_edit_tv_name:
			mSelectNamePopupWindow = new SelectNamePopupWindow(this,
					initNameShowViews());
			mSelectNamePopupWindow
					.setAnimationStyle(Res.style.MusicPoPAnimation);
			mSelectNamePopupWindow.showAtLocation(mLinEditName,
					Gravity.CENTER, 0, 0);
			mSelectNamePopupWindow.setFocusable(true);
			break;
		default:
			break;
		}
	}
}
