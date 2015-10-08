package cn.yunzhisheng.vui.assistant.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import cn.yunzhisheng.common.util.LogUtil;
import cn.yunzhisheng.vui.assistant.preference.PrivatePreference;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.vui.assistant.preference.UserPreference;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;

public class SinaWeiboClient extends BaseWeiboClient {
	public static final String TAG = "SinaWeiboClient";
	public static final int MESSAGE_START_TALK = 1001;

	private static final String SINA_CALLBACK_URL = "http://www.sina.com";
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
										+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
										+ "follow_app_official_microblog," + "invitation_write" + "statuses_to_me_read";

	private WeiboAuth mWeiboAuth = null;
	private Oauth2AccessToken authToken = null;
	private Handler mWeiboSendDataHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			LogUtil.d(TAG, "handleWeiboMessage:" + msg);
			switch (msg.what) {
			case SessionPreference.REQUEST_CODE_WEIBO_SINA_STATUS_OK:
				if (mWeiboClientListener != null) {
					mWeiboClientListener.onUpdateSuccess();
				}
				break;
			case SessionPreference.REQUEST_CODE_WEIBO_SINA_STATUS_FAILED:
				if (mWeiboClientListener != null) {
					int errorCode = Integer.parseInt((String) msg.obj);
					mWeiboClientListener.onUpdateError(errorCode);
				}
				break;

			}
		}
	};
	private RequestListener requestListener = new RequestListener() {

		@Override
		public void onComplete(String result) {
			if (result.contains("created_at")) {
				mWeiboSendDataHandler.sendEmptyMessage(SessionPreference.REQUEST_CODE_WEIBO_SINA_STATUS_OK);
			} else {
				// 20006 Image size too large 图片太大
				// 20016 Out of limit 发布内容过于频繁
				// 20017 Repeat content 提交相似的信息
				// 20019 Repeat content 提交相同的信息
				// 20020 Contain advertising 包含广告信息
				// 20021 Content is illegal 包含非法内容
				// 21602 Contains forbid world 含有敏感词
				// 20111 Repeated weibo text 不能发布相同的微博
				// 21315 Token expired Token已经过期
				// 21316 Token revoked Token不合法
				// 21317 Token rejected Token不合法
				// 21327 token过期
				// 21332 access_token 无效
				// 21319 Accessor was revoked 授权关系已经被解除
				// 10010 Job expired 任务超时

				mWeiboSendDataHandler.sendEmptyMessage(SessionPreference.REQUEST_CODE_WEIBO_SINA_STATUS_FAILED);
			}
		}

		@Override
		public void onComplete4binary(ByteArrayOutputStream arg0) {

		}

		@Override
		public void onError(WeiboException arg0) {
			arg0.printStackTrace();
		}

		@Override
		public void onIOException(IOException arg0) {
			arg0.printStackTrace();
		}

	};
	private WeiboAuthListener mAuthorDialogListener = new WeiboAuthListener() {
		@Override
		public void onComplete(Bundle values) {

			authToken = Oauth2AccessToken.parseAccessToken(values);
			if (authToken.isSessionValid()) {
				mAccessToken = authToken.getToken();
				mExpiresTimeMillis = authToken.getExpiresTime();
				mPreference.putString(UserPreference.KEY_SINA_WEIBO_ACCESS_TOKEN, mAccessToken);
				mPreference.putLong(UserPreference.KEY_SINA_WEIBO_EXPIRE_TIME_MILLIS, mExpiresTimeMillis);
				mPreference.putBoolean(UserPreference.KEY_WEIBO_SINA_BIND, true);

				if (mWeiboClientListener != null) {
					mWeiboClientListener.onAuthorSuccess();
				}
			} else {
				String code = values.getString("code");
				if (mWeiboClientListener != null) {
					mWeiboClientListener.onAuthorError(Integer.parseInt(code));
				}
			}
		}

		@Override
		public void onCancel() {
			if (mWeiboClientListener != null) {
				mWeiboClientListener.onAuthorCancel();
			}
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			if (mWeiboClientListener != null) {
				mWeiboClientListener.onAuthorError(-1);
			}
		}

	};

	public SinaWeiboClient(Context context) {
		super(context);
	}

	@Override
	public void init() {
		mAccessToken = mPreference.getString(UserPreference.KEY_SINA_WEIBO_ACCESS_TOKEN, "");
		mExpiresTimeMillis = mPreference.getLong(UserPreference.KEY_SINA_WEIBO_EXPIRE_TIME_MILLIS, 0);
		if (!mAccessToken.equals("")) {
			authToken = new Oauth2AccessToken();
			authToken.setToken(mAccessToken);
		}
	}

	@Override
	public void setListener(IWeiboClientListener weiboClientListener) {
		mWeiboClientListener = weiboClientListener;
	}

	@Override
	public void authorize() {
		// Weibo.getInstance().authorize((Activity) mContext,
		// mAuthorDialogListener);
		mWeiboAuth = new WeiboAuth(mContext, PrivatePreference.SINA_CONSUMER_KEY, SINA_CALLBACK_URL, SCOPE);
		mWeiboAuth.anthorize(mAuthorDialogListener);
	}

	@Override
	public void update(String status, String imagePath) {

		StatusesAPI weibo = new StatusesAPI(authToken);
		if (imagePath != null && !imagePath.equals("")) weibo.upload(status, imagePath, "0", "0", requestListener);
		else weibo.update(status, "0", "0", requestListener);
	}

	@Override
	public boolean isAuthorized() {
		long currentTimeMillis = System.currentTimeMillis();
		boolean binded = (!TextUtils.isEmpty(mAccessToken)) && currentTimeMillis < mExpiresTimeMillis;
		mPreference.putBoolean(UserPreference.KEY_WEIBO_SINA_BIND, binded);
		return binded;
	}

	@Override
	public void authorizeCallBack(int requestCode, int resultCode, Intent data) {
		// Weibo.getInstance().authorizeCallBack(requestCode, resultCode, data);
	}

	@Override
	public int getClientId() {
		return SessionPreference.VALUE_WEIBO_CLIENT_ID_SINA;
	}

}
