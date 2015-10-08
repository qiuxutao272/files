package cn.yunzhisheng.vui.assistant.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import cn.yunzhisheng.vui.assistant.preference.PrivatePreference;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.vui.assistant.preference.UserPreference;

import com.tencent.weibo.api.TAPI;
import com.tencent.weibo.constants.OAuthConstants;
import com.tencent.weibo.oauthv2.OAuthV2;
import com.tencent.weibo.sdk.android.component.sso.AuthHelper;
import com.tencent.weibo.sdk.android.component.sso.OnAuthListener;
import com.tencent.weibo.sdk.android.component.sso.WeiboToken;

import com.tencent.weibo.webview.OAuthV2AuthorizeWebView;

public class TencentWeiboClient extends BaseWeiboClient {
	private static final String TENCENT_CALLBACK_URL = "null";

	private OAuthV2 mTencentoAuthV2 = null;
	//for auth2.0
	private String  mOpenId = null;
	//private ssoAuth   mSsoAuth = null;
	private UpdateWeiboTask mUpdateWeiboTask = null;
	private OnAuthListener mSsoAuthListener = new OnAuthListener(){

		@Override
		public void onAuthFail(int error, String arg1) {
			if (mWeiboClientListener != null) {
				mWeiboClientListener.onAuthorError(error);
			}
		}

		@Override
		public void onAuthPassed(String arg0, WeiboToken token) {
			mAccessToken = token.accessToken;
			mOpenId = token.openID;
			if(mTencentoAuthV2!=null){
				
				mTencentoAuthV2.setAccessToken(mAccessToken);
				mTencentoAuthV2.setOpenid(mOpenId);
			}
			mPreference.putString(UserPreference.KEY_TECENT_WEIBO_ACCESS_TOKEN, mAccessToken);
			mPreference.putString(UserPreference.KEY_TECENT_WEIBO_ACCESS_OPENID, mOpenId);
			mPreference.putBoolean(UserPreference.KEY_WEIBO_TECENT_BIND, true);

			if (mWeiboClientListener != null) {
				mWeiboClientListener.onAuthorSuccess();
			}
		}

		@Override
		public void onWeiBoNotInstalled() {
			authProcess();
		}

		@Override
		public void onWeiboVersionMisMatch() {
			authProcess();
		}

	};

	public TencentWeiboClient(Context context) {
		super(context);
	}

	@Override
	public void init() {
		mAccessToken = mPreference.getString(UserPreference.KEY_TECENT_WEIBO_ACCESS_TOKEN, "");
		mOpenId = mPreference.getString(UserPreference.KEY_TECENT_WEIBO_ACCESS_OPENID, "");
		mTencentoAuthV2 = new OAuthV2(PrivatePreference.TENCENT_CONSUMER_KEY, PrivatePreference.TENCENT_CONSUMER_SECRET, TENCENT_CALLBACK_URL);
		mTencentoAuthV2.setRedirectUri("http://static.apk.hiapk.com/html/2012/10/891397.html");
		if (!mAccessToken.equals("") && !mOpenId.equals("")) {
			mTencentoAuthV2.setAccessToken(mAccessToken);
			mTencentoAuthV2.setOpenid(mOpenId);
		}
		long appId = Long.parseLong(PrivatePreference.TENCENT_CONSUMER_KEY);
		AuthHelper.register(mContext, appId, PrivatePreference.TENCENT_CONSUMER_SECRET, mSsoAuthListener);
	}

	private void authProcess() {
		Intent authorIntent = new Intent(mContext, OAuthV2AuthorizeWebView.class);
		authorIntent.putExtra("oauth", mTencentoAuthV2);

		((Activity) mContext).startActivityForResult(authorIntent, SessionPreference.REQUEST_CODE_WEIBO_TENCENT_AUTHOR);
	}

	@Override
	public void setListener(IWeiboClientListener weiboClientListener) {
		mWeiboClientListener = weiboClientListener;
	}

	@Override
	public void authorize() {

		AuthHelper.auth(mContext, "");
	}

	@Override
	public void update(String status, String imagePath) {

		if (mUpdateWeiboTask != null && mUpdateWeiboTask.getStatus() != AsyncTask.Status.FINISHED) {
			mUpdateWeiboTask.cancel(false);
		}

		mUpdateWeiboTask = new UpdateWeiboTask();
		mUpdateWeiboTask.execute(status, imagePath);
	}

	@Override
	public boolean isAuthorized() {
		boolean binded = !TextUtils.isEmpty(mAccessToken) & !TextUtils.isEmpty(mOpenId);
		mPreference.putBoolean(UserPreference.KEY_WEIBO_TECENT_BIND, binded);
		return binded;
	}

	@Override
	public void authorizeCallBack(int requestCode, int resultCode, Intent data) {
		Log.i("weibo_tentence", String.format("result code:%d", requestCode));
		if (resultCode == OAuthV2AuthorizeWebView.RESULT_CODE) {
			OAuthV2 oAuth = (OAuthV2) data.getExtras().getSerializable("oauth");

			if (oAuth != null) {
				this.mTencentoAuthV2 = oAuth;
				mAccessToken = oAuth.getAccessToken();
				mOpenId = oAuth.getOpenid();
				mPreference.putString(UserPreference.KEY_TECENT_WEIBO_ACCESS_TOKEN, mAccessToken);
				mPreference.putString(UserPreference.KEY_TECENT_WEIBO_ACCESS_OPENID, mOpenId);
				mPreference.putBoolean(UserPreference.KEY_WEIBO_TECENT_BIND, true);
			}

			if (mWeiboClientListener != null) {
				mWeiboClientListener.onAuthorSuccess();
			}
		} else {
			if (mWeiboClientListener != null) {
				mWeiboClientListener.onAuthorCancel();
			}
		}
	}

	@Override
	public int getClientId() {
		return SessionPreference.VALUE_WEIBO_CLIENT_ID_TENCENT;
	}

	private class UpdateWeiboTask extends AsyncTask<Object, Integer, String> {

		@Override
		protected String doInBackground(Object... params) {
			String status = (String) params[0];
			String imagePath = (String) params[1];

			String response = null;

			TAPI tAPI = null;
			try {
				tAPI = new TAPI(OAuthConstants.OAUTH_VERSION_2_A);
				if (TextUtils.isEmpty(imagePath)) {
					response = tAPI.add(mTencentoAuthV2, "json", status, "127.0.0.1");
				} else {
					response = tAPI.addPic(mTencentoAuthV2, "json", status, "127.0.0.1", imagePath);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			JSONTokener jsonParser = new JSONTokener(result);
			JSONObject mJsonObject = null;
			String ret = null;

			try {
				if (result != null) {
					mJsonObject = (JSONObject) jsonParser.nextValue();
					ret = mJsonObject.getString("ret");
					if (ret.equals("0")) {
						if (mWeiboClientListener != null) {
							mWeiboClientListener.onUpdateSuccess();
						}
					} else {
						if (mWeiboClientListener != null) {
							mWeiboClientListener.onUpdateError(-1);
						}
					}
				} else {
					if (mWeiboClientListener != null) {
						mWeiboClientListener.onUpdateError(-1);
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
