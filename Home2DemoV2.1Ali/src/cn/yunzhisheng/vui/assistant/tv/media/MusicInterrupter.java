/**
 * Copyright (c) 2012-2012 Yunzhisheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : MusicInterrupter.java
 * @ProjectName : iShuoShuo2
 * @PakageName : cn.yunzhisheng.ishuoshuo.media
 * @Author : Brant
 * @CreateDate : 2012-12-28
 */
package cn.yunzhisheng.vui.assistant.tv.media;

import cn.yunzhisheng.vui.assistant.preference.UserPreference;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Brant
 * @CreateDate : 2012-12-28
 * @ModifiedBy : Brant
 * @ModifiedDate: 2012-12-28
 * @Modified:
 * 2012-12-28: 实现基本功能
 */
public class MusicInterrupter {
	public static final String TAG = "MusicInterrupter";
	//private Context mContext;
	private MusicPlaybackService mContext;
	
	private WifiManager mWifiManager;
	private WifiLock mWifiLock;
	private TelephonyManager mTelephonyManager;
	private PhoneStateListener mPhoneStateListener;
	private UserPreference mPreferenceAction;

	/*public MusicInterrupter(Context context) {
		mContext = context;
	}*/
	public MusicInterrupter(MusicPlaybackService context) {
        mContext = context;
        mPreferenceAction = new UserPreference(context);
    }

	public void start() {
		mTelephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
		mPhoneStateListener = new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				Log.d(TAG, "onCallStateChanged");
				if (state == TelephonyManager.CALL_STATE_RINGING) {
				    mContext.stop();
                    mPreferenceAction.putBoolean("IS_MUSIC_PLAYING", false);
                    Log.i(TAG, "onCallStateChanged is Ringing..stop Music.");
				}
			}
		};
		mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
		mWifiLock = mWifiManager.createWifiLock(TAG);
		mWifiLock.setReferenceCounted(false);
		mWifiLock.acquire();
	}

	public void stop() {
		mWifiLock.release();
		mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_NONE);
		mPhoneStateListener = null;
		mTelephonyManager = null;
		mWifiLock = null;
		mWifiManager = null;
	}

}
