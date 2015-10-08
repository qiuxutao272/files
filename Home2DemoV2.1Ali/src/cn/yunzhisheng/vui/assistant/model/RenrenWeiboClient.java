package cn.yunzhisheng.vui.assistant.model;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import cn.yunzhisheng.vui.assistant.preference.PrivatePreference;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.vui.assistant.preference.UserPreference;

import com.renren.api.connect.android.AsyncRenren;
import com.renren.api.connect.android.Renren;
import com.renren.api.connect.android.common.AbstractRequestListener;
import com.renren.api.connect.android.exception.RenrenAuthError;
import com.renren.api.connect.android.exception.RenrenError;
import com.renren.api.connect.android.photos.PhotoHelper;
import com.renren.api.connect.android.photos.PhotoUploadRequestParam;
import com.renren.api.connect.android.photos.PhotoUploadResponseBean;
import com.renren.api.connect.android.status.StatusSetRequestParam;
import com.renren.api.connect.android.status.StatusSetResponseBean;
import com.renren.api.connect.android.view.RenrenAuthListener;

public class RenrenWeiboClient extends BaseWeiboClient {
	private Renren mRenrenAuthor = null;

	private static final int MESSAGE_AUTHORIZE_COMPLETE = 0;
	private static final int MESSAGE_AUTHORIZE_CANCEL = 1;
	private static final int MESSAGE_AUTHORIZE_ERROR = 2;
	private static final int MESSAGE_UPDATE_SUCCESS = 3;
	private static final int MESSAGE_UPDATE_ERROR = 4;

	private Handler mMainThreadHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			
			case MESSAGE_AUTHORIZE_COMPLETE:
				mPreference.putBoolean(UserPreference.KEY_WEIBO_RENREN_BIND, true);
				if (mWeiboClientListener != null) {
					mWeiboClientListener.onAuthorSuccess();
					Activity activity = (Activity)mContext;
					activity.finish();
				}
				break;
			case MESSAGE_AUTHORIZE_CANCEL:
				if (mWeiboClientListener != null) {
					mWeiboClientListener.onAuthorCancel();
					Activity activity = (Activity)mContext;
					activity.finish();
				}
				break;
			case MESSAGE_AUTHORIZE_ERROR:
				if (mWeiboClientListener != null) {
					mWeiboClientListener.onAuthorError(-1);
					Activity activity = (Activity)mContext;
					activity.finish();
				}
				break;
			case MESSAGE_UPDATE_SUCCESS:
				if (mWeiboClientListener != null) {
					mWeiboClientListener.onUpdateSuccess();
				}
				break;
			case MESSAGE_UPDATE_ERROR:
				if (mWeiboClientListener != null) {
					mWeiboClientListener.onUpdateError(-1);
				}
				break;
			default:
				break;
			}

			super.handleMessage(msg);
		}
	};

	private RenrenAuthListener mRenrenAuthListener = new RenrenAuthListener() {
		@Override
		public void onComplete(Bundle values) {
			mMainThreadHandler.sendEmptyMessage(MESSAGE_AUTHORIZE_COMPLETE);
		}

		@Override
		public void onRenrenAuthError(RenrenAuthError renrenAuthError) {
			mMainThreadHandler.sendEmptyMessage(MESSAGE_AUTHORIZE_ERROR);
		}

		@Override
		public void onCancelLogin() {
			mMainThreadHandler.sendEmptyMessage(MESSAGE_AUTHORIZE_CANCEL);
		}

		@Override
		public void onCancelAuth(Bundle values) {
			mMainThreadHandler.sendEmptyMessage(MESSAGE_AUTHORIZE_CANCEL);
		}
	};

	private AbstractRequestListener<StatusSetResponseBean> mUpdateStatusListener = new AbstractRequestListener<StatusSetResponseBean>() {
		@Override
		public void onRenrenError(RenrenError renrenError) {
			mMainThreadHandler.sendEmptyMessage(MESSAGE_UPDATE_ERROR);
		}

		@Override
		public void onFault(Throwable fault) {
			mMainThreadHandler.sendEmptyMessage(MESSAGE_UPDATE_ERROR);
		}

		@Override
		public void onComplete(StatusSetResponseBean bean) {
			mMainThreadHandler.sendEmptyMessage(MESSAGE_UPDATE_SUCCESS);
		}

	};

	private AbstractRequestListener<PhotoUploadResponseBean> mUpdatePhotoListener = new AbstractRequestListener<PhotoUploadResponseBean>() {
		@Override
		public void onRenrenError(RenrenError renrenError) {
			mMainThreadHandler.sendEmptyMessage(MESSAGE_UPDATE_ERROR);
		}

		@Override
		public void onFault(Throwable fault) {
			mMainThreadHandler.sendEmptyMessage(MESSAGE_UPDATE_ERROR);
		}

		@Override
		public void onComplete(PhotoUploadResponseBean photoResponse) {
			mMainThreadHandler.sendEmptyMessage(MESSAGE_UPDATE_SUCCESS);
		}
	};

	public RenrenWeiboClient(Context context) {
		super(context);
	}

	@Override
	public void init() {
		mRenrenAuthor = new Renren(PrivatePreference.RENREN_API_KEY, PrivatePreference.RENREN_SECRET_KEY, PrivatePreference.RENREN_APP_ID, mContext);
	}

	@Override
	public void setListener(IWeiboClientListener weiboClientListener) {
		mWeiboClientListener = weiboClientListener;
	}

	@Override
	public void authorize() {
		mRenrenAuthor.authorize((Activity) mContext, mRenrenAuthListener);
	}

	@Override
	public void update(String status, String imagePath) {
		if (TextUtils.isEmpty(imagePath)) {
			StatusSetRequestParam statusSetRequestParam = new StatusSetRequestParam(status);

			AsyncRenren asyncRenren = new AsyncRenren(mRenrenAuthor);
			asyncRenren.publishStatus(statusSetRequestParam, mUpdateStatusListener, true);
		} else {
			PhotoUploadRequestParam photoParam = new PhotoUploadRequestParam();
			photoParam.setCaption(status);
			photoParam.setFile(new File(imagePath));

			final PhotoHelper photoHelper = new PhotoHelper(mRenrenAuthor);

			photoHelper.asyncUploadPhoto(photoParam, mUpdatePhotoListener);
		}
	}

	@Override
	public boolean isAuthorized() {
		boolean binded = mRenrenAuthor.isSessionKeyValid();
		mPreference.putBoolean(UserPreference.KEY_WEIBO_RENREN_BIND, binded);
		return binded;
	}

	@Override
	public void authorizeCallBack(int requestCode, int resultCode, Intent data) {

	}

	@Override
	public int getClientId() {
		return SessionPreference.VALUE_WEIBO_CLIENT_ID_RENREN;
	}
}
