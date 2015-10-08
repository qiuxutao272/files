package cn.yunzhisheng.vui.assistant.tv.session;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;
import cn.yunzhisheng.common.util.LogUtil;
import cn.yunzhisheng.vui.assistant.WeiBoActivity;
import cn.yunzhisheng.vui.assistant.model.BaseWeiboClient;
import cn.yunzhisheng.vui.assistant.model.IWeiboClientListener;
import cn.yunzhisheng.vui.assistant.model.RenrenWeiboClient;
import cn.yunzhisheng.vui.assistant.model.SinaWeiboClient;
import cn.yunzhisheng.vui.assistant.model.TencentWeiboClient;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;

public class WeiBoShowSession extends BaseSession {
	private static final String TAG = "WeiBoStartSession";
	private String sendContent = "";
	private String sendImagePath = "";
	private BaseWeiboClient mCurrentWeiboClient = null;
	private IWeiboClientListener mWeiboClientListener = new IWeiboClientListener() {
		@Override
		public synchronized void onUpdateSuccess() {
			LogUtil.d(TAG, "onUpdateSuccess");
			Toast.makeText(mContext, "微博分享成功", Toast.LENGTH_LONG).show();
			showWeiboDone("微博分享成功");
		}

		@Override
		public void onUpdateError(int error) {
			LogUtil.d(TAG, "onUpdateError " + error);
			Toast.makeText(mContext, "微博分享失败", Toast.LENGTH_LONG).show();
			showWeiboDone("微博分享失败");
		}

		@Override
		public synchronized void onAuthorSuccess() {
			LogUtil.d(TAG, "onAutorSuccess");
			mCurrentWeiboClient.update(sendContent, sendImagePath);
			Activity weiboActivity = (Activity) WeiBoActivity.mCurrWeiboClient.mContext;
			weiboActivity.finish();
		}

		@Override
		public synchronized void onAuthorError(int error) {
			LogUtil.d(TAG, "onAuthorError");
			Toast.makeText(mContext, "微博授权出错", Toast.LENGTH_LONG).show();
			showWeiboDone("微博授权出错");
		}

		@Override
		public synchronized void onAuthorCancel() {
			LogUtil.d(TAG, "onAuthorCancel");
			Toast.makeText(mContext, "微博授权取消", Toast.LENGTH_LONG).show();
			showWeiboDone("微博授权取消");
		}
	};

	public void showWeiboDone(String msg) {
		addAnswerViewText(msg);
		playTTS(msg);
	}

	private void disMissWeiBoActivity() {

		Activity weiboActivity = (Activity) WeiBoActivity.mCurrWeiboClient.mContext;
		weiboActivity.finish();
	}

	WeiBoShowSession(Context context, Handler handle) {
		super(context, handle);
	}

	public void putProtocol(JSONObject jsonObject) {
		if (jsonObject == null) {

		}
		super.putProtocol(jsonObject);
		JSONObject jsonData = getJSONObject(mDataObject, SessionPreference.KEY_RESULT);

		sendContent = getJsonValue(jsonData, SessionPreference.VALUE_TYPE_WEIBO_CONTENT_TXT);
		sendImagePath = getJsonValue(jsonData, SessionPreference.DOMAIN_ALARM);
		sendImagePath = WeiBoInputShowSession.mImagePath;
		String vendor = WeiBoInputShowSession.mWeiBoType;

		if (vendor.equals(SessionPreference.VALUE_WEIBO_VENDOR_SINA)) {
			mCurrentWeiboClient = new SinaWeiboClient(mContext);
		} else if (vendor.equals(SessionPreference.VALUE_WEIBO_VENDOR_TENCENT)) {
			mCurrentWeiboClient = new TencentWeiboClient(mContext);
		} else if (vendor.equals(SessionPreference.VALUE_WEIBO_VENDOR_RENREN)) {
			mCurrentWeiboClient = new RenrenWeiboClient(mContext);
		}
		mCurrentWeiboClient.init();
		mCurrentWeiboClient.setListener(mWeiboClientListener);

		if (mCurrentWeiboClient.isAuthorized()) {
			mCurrentWeiboClient.update(sendContent, sendImagePath);
		} else {
			Intent dialogIntent = new Intent(mContext, WeiBoActivity.class);
			dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			WeiBoActivity.mCurrWeiboClient = mCurrentWeiboClient;
			mContext.startActivity(dialogIntent);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case SessionPreference.REQUEST_CODE_WEIBO_SINA_AUTHOR:
		case SessionPreference.REQUEST_CODE_WEIBO_TENCENT_AUTHOR: {
			mCurrentWeiboClient.authorizeCallBack(requestCode, resultCode, data);
		}
		}
	}
}
