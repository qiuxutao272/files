package cn.yunzhisheng.vui.assistant.model;

import android.content.Context;
import android.content.Intent;
import cn.yunzhisheng.vui.assistant.WeiBoActivity;
import cn.yunzhisheng.vui.assistant.preference.UserPreference;

public abstract class BaseWeiboClient {
	public  	Context mContext = null;
	public		WeiBoActivity mWeiBoActivity = null;
	protected IWeiboClientListener mWeiboClientListener = null;

	protected String mAccessToken = "";
	protected String mAccessTokenSecret = "";

	protected long mExpiresTimeMillis = 0;
	protected boolean mChecked = false;
	protected UserPreference mPreference;

	public BaseWeiboClient(Context context) {
		mContext = context;
		mPreference = new UserPreference(context);
	}

	public abstract void init();

	public abstract void setListener(IWeiboClientListener weiboClientListener);

	public abstract void authorize();

	public abstract boolean isAuthorized();

	public abstract void authorizeCallBack(int requestCode, int resultCode, Intent data);

	public abstract void update(String message, String imagePath);

	public abstract int getClientId();
}
