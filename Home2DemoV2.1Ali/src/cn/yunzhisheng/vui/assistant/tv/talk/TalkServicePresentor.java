/**
 * Copyright (c) 2012-2012 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : TalkServicePresentor.java
 * @ProjectName : V Plus 1.0
 * @PakageName : cn.yunzhisheng.ishuoshuo.talk
 * @Author : Dancindream
 * @CreateDate : 2012-5-22
 */
package cn.yunzhisheng.vui.assistant.tv.talk;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import cn.yunzhisheng.common.net.Network;
import cn.yunzhisheng.common.util.LogUtil;
import cn.yunzhisheng.vui.assistant.tv.talk.ITalkService;
import cn.yunzhisheng.vui.assistant.util.MessageManager;

public class TalkServicePresentor {
	public static final String TAG = "TalkServicePresentor";

	private Context mContext = null;
	private ITalkServicePresentorListener mServicePresentorListener = null;
	private ITalkService mTalkService = null;

	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			LogUtil.d(TAG, "onServiceConnected");
			mTalkService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			LogUtil.d(TAG, "onServiceConnected");
			mTalkService = ITalkService.Stub.asInterface(service);
			MessageManager.sendPrivateMessage(mContext, TalkService.TALK_EVENT_ON_INITDONE);
		}
	};

	private BroadcastReceiver mServiceReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			try {
				if (action.equals(TalkService.TALK_EVENT_ON_SESSION_PROTOCAL)) {
					Bundle extras = intent.getExtras();
					String result = extras.getString(TalkService.TALK_DATA_PROTOCAL);
					mServicePresentorListener.onSessionProtocal(result);
				} else if (action.equals(TalkService.TALK_EVENT_ON_INITDONE)) {
					mServicePresentorListener.onTalkInitDone();
				} else if (action.equals(TalkService.EVENT_TTS_BUFFER)) {
					mServicePresentorListener.onBuffer();
				} else if (action.equals(TalkService.EVENT_TTS_PLAY_BEGIN)) {
					mServicePresentorListener.onPlayBegin();
				} else if (action.equals(TalkService.EVENT_TTS_PLAY_END)) {
					mServicePresentorListener.onPlayEnd();
				} else if (Network.ACTION_CONNECTIVITY_CHANGE.equals(action)) {
					mServicePresentorListener.onConnectivityChanged();
				} else if (action.equals(TalkService.TALK_EVENT_ON_RECORDING_START)) {
					mServicePresentorListener.onTalkRecordingStart();
				} else if (action.equals(TalkService.TALK_EVENT_ON_START)) {
					mServicePresentorListener.onTalkStart();
				} else if (action.equals(TalkService.TALK_EVENT_ON_STOP)) {
					mServicePresentorListener.onTalkStop();
				} else if (action.equals(TalkService.TALK_EVENT_ON_CANCEL)) {
					mServicePresentorListener.onTalkCancel();
				} else if (action.equals(TalkService.MOBILE_CONTROL_CONNECTION)) {
					Bundle extras = intent.getExtras();
					String client = extras.getString(TalkService.MOBILE_CONTROL_CONNECT_CLIENT);
					mServicePresentorListener.onConnectClient(client);
				} else if (action.equals(TalkService.MOBILE_CONTROL_DISCONNECTION)) {
					Bundle extras = intent.getExtras();
					String client = extras.getString(TalkService.MOBILE_CONTROL_DISCONNECT_CLIENT);
					mServicePresentorListener.onDisconnectClient(client);
				} else if (action.equals(TalkService.TALK_EVENT_ON_UPDATE_VOLUME)){
					Bundle extras = intent.getExtras();
					int volume = extras.getInt(TalkService.TALK_DATA_VOLUME);
					mServicePresentorListener.onUpdateVolume(volume);
				}else if (action.equals(TalkService.TALK_EVENT_ON_UPDATE_MOVEUP)) {
					mServicePresentorListener.onMoveUp();
				}else if (action.equals(TalkService.TALK_EVENT_ON_UPDATE_MOVEDOWN)) {
					mServicePresentorListener.onMoveDown();
				} else if(action.equals(TalkService.TALK_EVENT_ON_ERROR_SINGLETALK)){
					Bundle extras = intent.getExtras();
					String host = extras.getString(TalkService.MOBILE_CONTROL_SINGLETALK_HOST);
					String talkStatus = extras.getString(TalkService.MOBILE_CONTROL_SINGLETALK_STATUS);
					mServicePresentorListener.onErrorSingleTalk(host, talkStatus);
				}
			} catch (Exception e) {
				LogUtil.printStackTrace(e);
			}
			
			try {
				abortBroadcast();
			} catch (Exception e) {
			} 
		}
	};

	public TalkServicePresentor(Context context, ITalkServicePresentorListener l) {
		mServicePresentorListener = l;
		mContext = context;

		Intent intent = new Intent(mContext, TalkService.class);
		mContext.startService(intent);
		mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);

		registReceiver();
	}

	private void registReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(TalkService.TALK_EVENT_ON_SESSION_PROTOCAL);
		filter.addAction(TalkService.TALK_EVENT_ON_INITDONE);
		filter.addAction(TalkService.EVENT_TTS_BUFFER);
		filter.addAction(TalkService.EVENT_TTS_PLAY_BEGIN);
		filter.addAction(TalkService.EVENT_TTS_PLAY_END);
		filter.addAction(Network.ACTION_CONNECTIVITY_CHANGE);
		filter.addAction(TalkService.TALK_EVENT_ON_RECORDING_START);
		filter.addAction(TalkService.TALK_EVENT_ON_START);
		filter.addAction(TalkService.TALK_EVENT_ON_STOP);
		filter.addAction(TalkService.TALK_EVENT_ON_CANCEL);
		filter.addAction(TalkService.TALK_EVENT_ON_UPDATE_VOLUME);
		filter.addAction(TalkService.MOBILE_CONTROL_CONNECTION);
		filter.addAction(TalkService.MOBILE_CONTROL_DISCONNECTION);
		
		filter.addAction(TalkService.TALK_EVENT_ON_UPDATE_MOVEUP);
		filter.addAction(TalkService.TALK_EVENT_ON_UPDATE_MOVEDOWN);
		filter.addAction(TalkService.TALK_EVENT_ON_ERROR_SINGLETALK);
		
		filter = MessageManager.registPrivateIntentFilter(filter, mContext);
		mContext.registerReceiver(mServiceReceiver, filter);
	}

	private void unregistReceiver() {
		try {
			mContext.unregisterReceiver(mServiceReceiver);
		} catch (Exception e) {
		}
	}

	public void startTalk() {
		LogUtil.d(TAG, "startTalk");
		try {
			if (mTalkService != null) {
				mTalkService.startTalk();
			}
		} catch (RemoteException e) {
			LogUtil.printStackTrace(e);
		}
	}

	public void stopTalk() {
		LogUtil.d(TAG, "stopTalk");
		try {
			if (mTalkService != null) {
				mTalkService.stopTalk();
			}
		} catch (RemoteException e) {
			LogUtil.printStackTrace(e);
		}
	}

	public void cancelTalk() {
		LogUtil.d(TAG, "cancelTalk");
		try {
			if (mTalkService != null) {
				mTalkService.cancelTalk();
			}
		} catch (RemoteException e) {
			LogUtil.printStackTrace(e);
		}
	}
	
	public void putCustomText(String text) {
		LogUtil.d(TAG, "putCustomText text:" + text);
		try {
			if (mTalkService != null) {
				mTalkService.putCustomText(text);
			}
		} catch (RemoteException e) {
			LogUtil.printStackTrace(e);
		}
	}
	
	public void setProtocal(String protocal) {
		LogUtil.d(TAG, "setProtocal protocal:" + protocal);
		try {
			if (mTalkService != null) {
				mTalkService.setProtocal(protocal);
			}
		} catch (RemoteException e) {
			LogUtil.printStackTrace(e);
		}
	}

	public void playTTS(String tts) {
		LogUtil.d(TAG, "playTTS");
		try {
			if (mTalkService != null) {
				mTalkService.playTTS(tts);
			}
		} catch (RemoteException e) {
			LogUtil.printStackTrace(e);
		}
	}

	public void stopTTS() {
		LogUtil.d(TAG, "stopTTS");
		try {
			if (mTalkService != null) {
				mTalkService.stopTTS();
			}
		} catch (RemoteException e) {
			LogUtil.printStackTrace(e);
		}
	}

	public void onPause() {
		LogUtil.d(TAG, "onPause");
		unregistReceiver();
	}

	public void onResume() {
		LogUtil.d(TAG, "onResume");
		registReceiver();
	}

	public void onDestroy() {
		try {
			mContext.unbindService(mServiceConnection);
		} catch (Exception e) {
			LogUtil.printStackTrace(e);
		}

		mContext = null;
	}
}
