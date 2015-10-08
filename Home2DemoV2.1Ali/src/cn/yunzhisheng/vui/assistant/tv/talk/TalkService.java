/**
 * Copyright (c) 2012-2012 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : TalkService.java
 * @ProjectName : V Plus 1.0
 * @PakageName : cn.yunzhisheng.ishuoshuo.talk
 * @Author : Dancindream
 * @CreateDate : 2012-5-22
 */
package cn.yunzhisheng.vui.assistant.tv.talk;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.KeyEvent;
import cn.yunzhisheng.common.net.Network;
import cn.yunzhisheng.common.util.ErrorUtil;
import cn.yunzhisheng.common.util.LogUtil;
import cn.yunzhisheng.tts.offline.basic.ITTSControl;
import cn.yunzhisheng.tts.offline.basic.TTSFactory;
import cn.yunzhisheng.tts.offline.basic.TTSPlayerListener;
import cn.yunzhisheng.tts.offline.common.USCError;
import cn.yunzhisheng.vui.assistant.IVoiceAssistantListener;
import cn.yunzhisheng.vui.assistant.VoiceAssistant;
import cn.yunzhisheng.vui.assistant.oem.RomSystemSetting;
import cn.yunzhisheng.vui.assistant.preference.PrivatePreference;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.vui.assistant.preference.SessionPreferenceOfIntent;
import cn.yunzhisheng.vui.assistant.preference.UserPreference;
import cn.yunzhisheng.vui.assistant.tv.MessageReceiver;
import cn.yunzhisheng.vui.assistant.tv.WindowService;
import cn.yunzhisheng.vui.assistant.util.KeyUtil;
import cn.yunzhisheng.vui.assistant.util.MessageManager;
import cn.yunzhisheng.vui.server.IServer3Listener;
import cn.yunzhisheng.vui.server.IServerOperate;
import cn.yunzhisheng.vui.util.ControlKey;
import cn.yunzhisheng.vui.util.ControlType;
import cn.yunzhisheng.vui.util.DeviceInfo;

@SuppressLint("HandlerLeak")
public class TalkService extends Service {
	public static final String TAG = "TalkService";
	public final static String EVENT_TTS_PLAY_BEGIN = "EVENT_TTS_PLAY_BEGIN";
	public final static String EVENT_TTS_BUFFER = "EVENT_TTS_BUFFER";
	public final static String EVENT_TTS_PLAY_END = "EVENT_TTS_PLAY_END";
	public final static String EVENT_TTS_CANCEL = "EVENT_TTS_CANCEL";

	public final static String MOBILE_CONTROL_CONNECTION = "MOBILE_CONTROL_CONNECTION";
	public final static String MOBILE_CONTROL_DISCONNECTION = "MOBILE_CONTROL_DISCONNECTION";
	public final static String MOBILE_CONTROL_CONNECT_CLIENT = "MOBILE_CONTROL_CONNECT_CLIENT";
	public final static String MOBILE_CONTROL_DISCONNECT_CLIENT = "MOBILE_CONTROL_DISCONNECT_CLIENT";
	public final static String MOBILE_CONTROL_SINGLETALK_HOST = "MOBILE_CONTROL_SINGLETALK_HOST";
	public final static String MOBILE_CONTROL_SINGLETALK_STATUS = "MOBILE_CONTROL_SINGLETALK_STATUS";

	public final static String TALK_EVENT_ON_INITDONE = "cn.yunzhisheng.intent.talk.onInitDone";
	public final static String TALK_EVENT_ON_RECORDING_START = "cn.yunzhisheng.intent.talk.onRecordingStart";
	public final static String TALK_EVENT_ON_START = "cn.yunzhisheng.intent.talk.onTalkStart";
	public final static String TALK_EVENT_ON_STOP = "cn.yunzhisheng.intent.talk.onTalkStop";
	public final static String TALK_EVENT_ON_CANCEL = "cn.yunzhisheng.intent.talk.onTalkCancel";
	public final static String TALK_EVENT_ON_SESSION_PROTOCAL = "cn.yunzhisheng.intent.talk.onSessionProtocal";
	public final static String TALK_EVENT_ON_UPDATE_VOLUME = "cn.yunzhisheng.intent.talk.onUpdateVolume";
	public final static String TALK_EVENT_ON_UPDATE_MOVEUP = "cn.yunzhisheng.intent.talk.onUpdateMoveUp";
	public final static String TALK_EVENT_ON_UPDATE_MOVEDOWN = "cn.yunzhisheng.intent.talk.onUpdateMoveDown";
	public final static String TALK_EVENT_ON_ERROR_SINGLETALK = "cn.yunzhisheng.intent.talk.onErrorSingleTalk";

	public final static String TALK_DATA_VOLUME = "cn.yunzhisheng.intent.talk.data.volume";

	public final static String TALK_DATA_PROTOCAL = "cn.yunzhisheng.intent.talk.data.protocal";

	public static final String PERMISSION_VOICE_COMMAND = "cn.yunzhisheng.permission.RECEIVE_VOICE_COMMAND";
	public static final String CATEGORY_VOICE_COMMAND = "cn.yunzhisheng.intent.category.VOICE_COMMAND";

	public static final String MSG_VIRTUAL_KEY = "cn.yunzhisheng.intent.virtualKey";
	public static final String KEY_CODE = "KEY_CODE";
	public static final String MSG_VIRTUAL_KEY_SERVER_ACTIVE = "MSG_VIRTUAL_KEY_SERVER_ACTIVE";

	public final static String NET_MODE_ALL = "ALL";
	public final static String NET_MODE_WIFI_ONLY = "WIFI_ONLY";
	public final static String NET_MODE_ONLINE_ONLY = "ONLINE_ONLY";
	public final static String NET_MODE_OFFLINE_ONLY = "OFFLINE_ONLY";

	public static String VOICE_TV_SERVER_IP = "";

	public static boolean isTalking = false;

	private VoiceAssistant mVoiceAssistant = null;
	private static IServerOperate mServerOperate = null;
	private ITTSControl mTTSPlayer = null;

	private static ArrayList<String> mOnlineSupportList = null;
	private static ArrayList<String> mOfflineSupportList = null;

	private static boolean mKeyServiceExist = true;

	private TalkService mTalkService = null;

	private Looper mServiceCompileLooper;

	private UserPreference mUserPreference;

	private String mServerVersionCode = "";

	private boolean hasNewVersion = false;

	/**
	 * @Description : 广播的实现
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			LogUtil.d(TAG, "action:  " + action);
			if (Network.CONNECTIVITY_CHANGE_ACTION.equals(action)) {
				// 网络变化的时候会发送通知
				Network.checkNetworkConnected(context);
			} else if (MSG_VIRTUAL_KEY_SERVER_ACTIVE.equals(action)) {
				// 说明接受虚拟按键的服务存在
				LogUtil.e(TAG, "KEY_VIRTUAL_KEY_SERVER_ACTIVE....alive");
				mUserPreference.putBoolean(UserPreference.KEY_VIRTUAL_KEY_SERVER_ACTIVE, true);
			} else if (SessionPreferenceOfIntent.INTENT_SETTVNAME.equals(action)) {
				if (mServerOperate != null) {
					mServerOperate.setName(mUserPreference.getString(
						UserPreference.TV_FRIENDLY_NAME_KEY,
						UserPreference.TV_FRIENDLY_NAME_DEFAULT));
				}
				sendSetTVNameSuccessToControl();
			} else if (SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_STOP.equals(action)) {
				sendMusicStartToControl(SessionPreference.MUSIC_STATUS_STOP, 0, 0);
			} else if (SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_START.equals(action)) {
				sendMusicStartToControl(SessionPreference.MUSIC_STATUS_START, 0, 0);
			} else if (SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_PAUSE.equals(action)) {
				sendMusicStartToControl(SessionPreference.MUSIC_STATUS_PAUSE, 0, 0);
			} else if (SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_PLAY.equals(action)) {
				sendMusicStartToControl(SessionPreference.MUSIC_STATUS_PLAY, 0, 0);
			} else if (SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_BUFFER.equals(action)) {
				int percent = intent.getExtras().getInt(SessionPreferenceOfIntent.KEY_MUSIC_BUFFER_PERCENT);
				// sendMusicStartToControl(SessionPreference.MUSIC_STATUS_BUFFER,percent,0);
			} else if (SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_PROGRESS.equals(action)) {
				int duration = intent.getExtras().getInt(SessionPreferenceOfIntent.KEY_MUSIC_PROGRESS_DURATION);
				int time = intent.getExtras().getInt(SessionPreferenceOfIntent.KEY_MUSIC_PROGRESS_TIME);
				// sendMusicStartToControl(SessionPreference.MUSIC_STATUS_PROGRESS,time,duration);
			}

			try {
				abortBroadcast();
			} catch (Exception e) {
			} 
		}
	};

	/**
	 * @Description : TTS的监听回调
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private TTSPlayerListener mListener = new TTSPlayerListener() {

		/**
		 * @Description : TODO TTS播放结束
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.tts.offline.TTSPlayerListener#onPlayEnd()
		 */
		@Override
		public void onPlayEnd() {
			showMessage(EVENT_TTS_PLAY_END);
		}

		/**
		 * @Description : TODO TTS开始播放
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.tts.offline.TTSPlayerListener#onPlayBegin()
		 */
		@Override
		public void onPlayBegin() {
			showMessage(EVENT_TTS_PLAY_BEGIN);
		}

		/**
		 * @Description : TODO TTS开始缓存
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.tts.offline.TTSPlayerListener#onBuffer()
		 */
		@Override
		public void onBuffer() {
			showMessage(EVENT_TTS_BUFFER);
		}

		/**
		 * @Description : TODO TTS已取消
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.tts.offline.TTSPlayerListener#onCancel()
		 */
		@Override
		public void onCancel() {
			showMessage(EVENT_TTS_CANCEL);
		}

		/**
		 * @Description : TODO TTS出现异常
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.tts.offline.TTSPlayerListener#onError(cn.yunzhisheng.tts.offline.common.USCError)
		 */
		@Override
		public void onError(USCError arg0) {

		}

		/**
		 * @Description : TODO TTS初始化完成
		 * @Author : Dancindream
		 * @CreateDate : 2014-5-10
		 * @see cn.yunzhisheng.tts.offline.TTSPlayerListener#onInitFinish()
		 */
		@Override
		public void onInitFinish() {

		}

		@Override
		public void onTtsData(byte[] arg0) {
			
		}
	};

	/**
	 * @Description : TalkService服务API
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private final ITalkService.Stub mBinder = new ITalkService.Stub() {

		/**
		 * @Description : TODO 开始录音流程
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.vui.assistant.tv.talk.ITalkService#startTalk()
		 */
		@Override
		public void startTalk() throws RemoteException {
			mTalkService.startTalk();
		}

		/**
		 * @Description : TODO 结束录音
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.vui.assistant.tv.talk.ITalkService#stopTalk()
		 */
		@Override
		public void stopTalk() throws RemoteException {
			mTalkService.stopTalk();
		}

		/**
		 * @Description : TODO 取消整个流程
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.vui.assistant.tv.talk.ITalkService#cancelTalk()
		 */
		@Override
		public void cancelTalk() throws RemoteException {
			mTalkService.cancelTalk();
		}

		/**
		 * @Description : TODO 使用自定义文字替代语音输入
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.vui.assistant.tv.talk.ITalkService#putCustomText(java.lang.String)
		 */
		@Override
		public void putCustomText(String text) throws RemoteException {
			mTalkService.putCustomText(text);
		}

		/**
		 * @Description : TODO 设置语音魔方协议，辅助魔方跳过一些流程
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.vui.assistant.tv.talk.ITalkService#setProtocal(java.lang.String)
		 */
		@Override
		public void setProtocal(String protocal) throws RemoteException {
			mTalkService.setProtocal(protocal);
		}

		/**
		 * @Description : TODO 播放TTS
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.vui.assistant.tv.talk.ITalkService#playTTS(java.lang.String)
		 */
		@Override
		public void playTTS(String tts) throws RemoteException {
			TalkService.this.playTTS(tts);
		}

		/**
		 * @Description : TODO 结束播放TTS
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.vui.assistant.tv.talk.ITalkService#stopTTS()
		 */
		@Override
		public void stopTTS() throws RemoteException {
			TalkService.this.stopTTS();
		}
	};

	/**
	 * @Description : TODO 语音魔方引擎主监听
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private IVoiceAssistantListener mVoiceAssistantListener = new IVoiceAssistantListener() {

		/**
		 * @Description : TODO 录音音量回调
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.vui.assistant.IVoiceAssistantListener#onVolumeUpdate(int)
		 */
		@Override
		public void onVolumeUpdate(int arg0) {
			PrivatePreference.mRecordingVoiceVolumn = (float) arg0;
			//mTalkService.onUpdateVolume(arg0);
		}

		/**
		 * @Description : TODO 流程取消后的回调
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.vui.assistant.IVoiceAssistantListener#onCancel()
		 */
		@Override
		public void onCancel() {
			mTalkService.onTalkCancel();
		}

		/**
		 * @Description : TODO 引擎初始化完成的回调，此时可以开始语音识别
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.vui.assistant.IVoiceAssistantListener#onInitDone()
		 */
		@Override
		public void onInitDone() {
			if (mVoiceAssistant != null) {
				// TODO 获取在线支持的领域列表
				mOnlineSupportList = mVoiceAssistant.getSupportList(true);
				// TODO 获取离线支持的领域列表
				mOfflineSupportList = mVoiceAssistant.getSupportList(false);
			}
			showMessage(TALK_EVENT_ON_INITDONE);
		}

		/**
		 * @Description : 【可忽略】返回语义协议数据
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.vui.assistant.IVoiceAssistantListener#onProtocal(java.lang.String)
		 */
		@Override
		public void onProtocal(String arg0) {
		}

		/**
		 * @Description : TODO 【主要】返回语音魔方协议数据
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.vui.assistant.IVoiceAssistantListener#onSessionProtocal(java.lang.String)
		 */
		@Override
		public void onSessionProtocal(String arg0) {
			mTalkService.onSessionProtocal(arg0);
		}

		/**
		 * @Description : TODO 流程开始的回调
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.vui.assistant.IVoiceAssistantListener#onStart()
		 */
		@Override
		public void onStart() {
			LogUtil.d(TAG, "VoiceAssistant_onStart");
			isTalking = true;
			mTalkService.onTalkStart();
		}

		/**
		 * @Description : TODO 录音结束的回调
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.vui.assistant.IVoiceAssistantListener#onStop()
		 */
		@Override
		public void onStop() {
			LogUtil.d(TAG, "VoiceAssistant_onStop");
			isTalking = false;
			mTalkService.onTalkStop();
		}

		/**
		 * @Description : TODO 麦克风真正开始录音的回调
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.vui.assistant.IVoiceAssistantListener#onRecordingStart()
		 */
		@Override
		public void onRecordingStart() {
			LogUtil.d(TAG, "VoiceAssistant_onRecordingStart");
			mTalkService.onRecordingStart();
		}

		/**
		 * @Description : TODO 个性化数据编译完成，此时开始个性化数据开始生效
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-22
		 * @see cn.yunzhisheng.vui.assistant.IVoiceAssistantListener#onDataDone()
		 */
		@Override
		public void onDataDone() {

		}

		/**
		 * @Description : 【可忽略】返回语音识别的结果
		 * @Author : Dancindream
		 * @CreateDate : 2014-4-23
		 * @see cn.yunzhisheng.vui.assistant.IVoiceAssistantListener#onResult(java.lang.String)
		 */
		@Override
		public void onResult(String arg0) {

		}

		@Override
		public void onRecordingStop() {
			LogUtil.d(TAG, "VoiceAssistant_onRecordingStop");
			
		}
	};

	private void startKeyService() {
		if (mKeyServiceExist) {
			Intent vritualKey = new Intent(MessageReceiver.ACTION_VIRTUAL_KEY_SERVER);
			startService(vritualKey);
		}
	}

	/**
	 * @Description : TODO 魔方插件：3.0版本服务器监听  new
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private IServer3Listener mServer3Listener = new IServer3Listener() {

		/**
		 * @Description	: TODO 接收到遙控器发送的退出界面的指令
		 * @Author		: Dancindream
		 * @CreateDate	: 2014-6-18
		 * @see cn.yunzhisheng.vui.server.IServer3Listener#onStopMachine()
		 */
		@Override
		public void onStopMachine() {
			mTalkService.exitAssistant();
		}

		/**
		 * @Description	: TODO 接收到遥控器发送的显示界面的指令
		 * @Author		: Dancindream
		 * @CreateDate	: 2014-6-18
		 * @see cn.yunzhisheng.vui.server.IServer3Listener#onStartMachine()
		 */
		@Override
		public void onStartMachine() {
			mTalkService.startAssistant();
		}

		/**
		 * @Description	: TODO 遥控器断开
		 * @Author		: Dancindream
		 * @CreateDate	: 2014-6-18
		 * @see cn.yunzhisheng.vui.server.IServer3Listener#onDisconnectClient(cn.yunzhisheng.vui.util.DeviceInfo)
		 */
		@Override
		public void onDisconnectClient(DeviceInfo deviceInfo) {
			mTalkService.onDisconnectClient(deviceInfo.getHost());
		}

		/**
		 * @Description	: TODO 遥控器触发标准按键
		 * @Author		: Dancindream
		 * @CreateDate	: 2014-6-18
		 * @see cn.yunzhisheng.vui.server.IServer3Listener#onControlDone(cn.yunzhisheng.vui.util.ControlKey)
		 */
		@Override
		public void onControlDone(ControlKey arg0) {
			if (arg0.equals(ControlKey.Back)) {
				if (mUserPreference.getBoolean(UserPreference.KEY_VIRTUAL_KEY_SERVER_ACTIVE, false)) {
					sendVirtualKeyMessage(KeyEvent.KEYCODE_BACK);
				} else {
					startKeyService();
					mTalkService.onVirtureKeyBack();
				}
			} else if (arg0.equals(ControlKey.Center)) {
				if (mUserPreference.getBoolean(UserPreference.KEY_VIRTUAL_KEY_SERVER_ACTIVE, false)) {
					sendVirtualKeyMessage(KeyEvent.KEYCODE_DPAD_CENTER);
				} else {
					startKeyService();
					mTalkService.onVirtureKeyCenter();
				}
			} else if (arg0.equals(ControlKey.Down)) {
				if (mUserPreference.getBoolean(UserPreference.KEY_VIRTUAL_KEY_SERVER_ACTIVE, false)) {
					sendVirtualKeyMessage(KeyEvent.KEYCODE_DPAD_DOWN);
				} else {
					startKeyService();
					mTalkService.onVirtureKeyDown();
				}
			} else if (arg0.equals(ControlKey.Left)) {
				if (mUserPreference.getBoolean(UserPreference.KEY_VIRTUAL_KEY_SERVER_ACTIVE, false)) {
					sendVirtualKeyMessage(KeyEvent.KEYCODE_DPAD_LEFT);
				} else {
					startKeyService();
					mTalkService.onVirtureKeyLeft();
				}
			} else if (arg0.equals(ControlKey.Right)) {
				if (mUserPreference.getBoolean(UserPreference.KEY_VIRTUAL_KEY_SERVER_ACTIVE, false)) {
					sendVirtualKeyMessage(KeyEvent.KEYCODE_DPAD_RIGHT);
				} else {
					startKeyService();
					mTalkService.onVirtureKeyRight();
				}
			} else if (arg0.equals(ControlKey.Up)) {
				if (mUserPreference.getBoolean(UserPreference.KEY_VIRTUAL_KEY_SERVER_ACTIVE, false)) {
					sendVirtualKeyMessage(KeyEvent.KEYCODE_DPAD_UP);
				} else {
					startKeyService();
					mTalkService.onVirtureKeyUp();
				}
			} else if (arg0.equals(ControlKey.Home)) {
				if (mUserPreference.getBoolean(UserPreference.KEY_VIRTUAL_KEY_SERVER_ACTIVE, false)) {
					sendVirtualKeyMessage(KeyEvent.KEYCODE_HOME);
				} else {
					startKeyService();
					mTalkService.onVirtureKeyUp();
				}
			} else if (arg0.equals(ControlKey.Menu)) {
				if (mUserPreference.getBoolean(UserPreference.KEY_VIRTUAL_KEY_SERVER_ACTIVE, false)) {
					sendVirtualKeyMessage(KeyEvent.KEYCODE_MENU);
				} else {
					startKeyService();
					mTalkService.onVirtureKeyUp();
				}
			} 
		}

		/**
		 * @Description	: TODO 遥控器接入回调
		 * @Author		: Dancindream
		 * @CreateDate	: 2014-6-18
		 * @see cn.yunzhisheng.vui.server.IServer3Listener#onConnectClient(cn.yunzhisheng.vui.util.DeviceInfo)
		 */
		@Override
		public void onConnectClient(DeviceInfo deviceInfo) {
			mTalkService.onConnectClient(deviceInfo.getHost());
		}

		/**
		 * @Description	: TODO 接收自定义数据；此函数中所有数据，均为遥控器发送过来的自定义通讯协议数据
		 * @Author		: Dancindream
		 * @CreateDate	: 2014-6-18
		 * @see cn.yunzhisheng.vui.server.IServer3Listener#onReceivedCustomDataFromControl(cn.yunzhisheng.vui.util.DeviceInfo, java.lang.String)
		 */
		@Override
		public void onReceivedCustomDataFromControl(DeviceInfo deviceInfo, String data) {
			LogUtil.d(TAG, "onReceivedCustomDataFromControl host = " + deviceInfo.getHost() + ";data = " + data
							+ ";hasNewVersion: " + hasNewVersion);
			if (data.equals("onMoveUp")) {
				showMessage(TALK_EVENT_ON_UPDATE_MOVEUP);
			} else if (data.equals("onMoveDown")) {
				showMessage(TALK_EVENT_ON_UPDATE_MOVEDOWN);
			} else if (data.contains(SessionPreference.ACTION_WEATHER_CITY)) {
				LogUtil.d(TAG, "set locationCity:" + data);
				String city[] = data.split(":");
				if (city != null && city.length == 2) {
					cn.yunzhisheng.preference.PrivatePreference.setValue("default_city", city[1]);
				}
			} else if (data.contains(SessionPreference.ACTION_SET_TV_NAME)) {
				LogUtil.d(TAG, "set tvName:" + data);
				String tvName[] = data.split(":");
				if (tvName != null && tvName.length == 2) {
					if (mUserPreference != null) {
						mUserPreference.putString(UserPreference.TV_FRIENDLY_NAME_KEY, tvName[1]);
						if (mServerOperate != null) {
							mServerOperate.setName(mUserPreference.getString(
								UserPreference.TV_FRIENDLY_NAME_KEY,
								UserPreference.TV_FRIENDLY_NAME_DEFAULT));
							sendSetTVNameSuccessToControl();
						}
						showMessage(SessionPreferenceOfIntent.INTENT_SETTVNAME);
					}
				}
			} else if (data.contains(SessionPreference.ACTION_MUSIC_PLAY)) {
				LogUtil.d(TAG, "歌曲播放");
				showMessage(SessionPreferenceOfIntent.INTENT_MUSIC_FROM_CONTROL_PLAY);
			} else if (data.contains(SessionPreference.ACTION_MUSIC_PAUSE)) {
				LogUtil.d(TAG, "歌曲暂停");
				showMessage(SessionPreferenceOfIntent.INTENT_MUSIC_FROM_CONTROL_PAUSE);
			} else if (data.contains(SessionPreference.ACTION_MUSIC_STOP)) {

				showMessage(SessionPreferenceOfIntent.INTENT_MUSIC_FROM_CONTROL_STOP);
			} else if (data.contains(SessionPreference.ACTION_MUSIC_PREV)) {
				LogUtil.d(TAG, "上一曲");
				showMessage(SessionPreferenceOfIntent.INTENT_MUSIC_FROM_CONTROL_PREV);
			} else if (data.contains(SessionPreference.ACTION_MUSIC_NEXT)) {
				LogUtil.d(TAG, "下一曲");
				showMessage(SessionPreferenceOfIntent.INTENT_MUSIC_FROM_CONTROL_NEXT);
			}
		}

		/**
		 * @Description	: TODO 遥控器发送切换频道的指令
		 * @Author		: Dancindream
		 * @CreateDate	: 2014-6-18
		 * @see cn.yunzhisheng.vui.server.IServer3Listener#onChannelDone(java.lang.String, java.lang.String)
		 */
		@Override
		public void onChannelDone(String action, String arg1) {

		}

		/**
		 * @Description	: TODO 遥控器发送调节音量的指令
		 * @Author		: Dancindream
		 * @CreateDate	: 2014-6-18
		 * @see cn.yunzhisheng.vui.server.IServer3Listener#onVolumeDone(java.lang.String, java.lang.String)
		 */
		@Override
		public void onVolumeDone(String action, String arg1) {
			LogUtil.d(TAG, "onVolumeDone   action: " + action + "volume : " + arg1);
			if (ControlType.ACTION_INCREASE.equals(action)) {
				// 音量增
				int currentVolume = RomSystemSetting.RaiseOrLowerVolume(TalkService.this, true, Integer.parseInt(arg1));
				mUserPreference.putInt(UserPreference.KEY_VOLUME, currentVolume);
			} else if (ControlType.ACTION_DECREASE.equals(action)) {
				// 音量减
				int currentVolume = RomSystemSetting
					.RaiseOrLowerVolume(TalkService.this, false, Integer.parseInt(arg1));
				mUserPreference.putInt(UserPreference.KEY_VOLUME, currentVolume);
			} else if (ControlType.ACTION_MAX.equals(action)) {
				// 设置最大音量
				int currentVolume = RomSystemSetting.setMaxVolume(TalkService.this);
				mUserPreference.putInt(UserPreference.KEY_VOLUME, currentVolume);
			} else if (ControlType.ACTION_MIN.equals(action)) {
				// 设置最小音量
				int currentVolume = RomSystemSetting.setMinVolume(TalkService.this);
				mUserPreference.putInt(UserPreference.KEY_VOLUME, currentVolume);
			} else if (ControlType.ACTION_RESET.equals(action)) {
				// 重置音量
				int currentVolume = -1;
				int lastVolumeValue = mUserPreference.getInt(UserPreference.KEY_VOLUME, -1);
				if (lastVolumeValue != -1) {
					currentVolume = RomSystemSetting.setVolume(TalkService.this, lastVolumeValue);
				}
				mUserPreference.putInt(UserPreference.KEY_VOLUME, currentVolume);
			} else if (ControlType.ACTION_SET.equals(action)) {
				// 设置音量到某个值
				int currentVolume = RomSystemSetting.setVolume(TalkService.this, Integer.parseInt(arg1));
				mUserPreference.putInt(UserPreference.KEY_VOLUME, currentVolume);
			}
		}

		/**
		 * @Description	: TODO 服务器异常的回调
		 * @Author		: Dancindream
		 * @CreateDate	: 2014-6-18
		 * @see cn.yunzhisheng.vui.server.IServer3Listener#onError(cn.yunzhisheng.common.util.ErrorUtil)
		 */
		@Override
		public void onError(ErrorUtil errorUtil) {
			LogUtil.d(TAG, "onError code:" + errorUtil.code + ";message:" + errorUtil.message);
		}

		/**
		 * @Description	: TODO 受限回调，当一个遥控器在使用语音时，其他遥控器将会受限
		 * @Author		: Dancindream
		 * @CreateDate	: 2014-6-18
		 * @see cn.yunzhisheng.vui.server.IServer3Listener#onSingleTalk(java.lang.String, java.lang.String)
		 */

		@Override
		public void onSingleTalk(DeviceInfo arg0, String arg1) {
			Bundle extras = new Bundle();
			extras.putString(MOBILE_CONTROL_SINGLETALK_HOST, arg0.host);
			extras.putString(MOBILE_CONTROL_SINGLETALK_STATUS, arg1);
			showMessage(TALK_EVENT_ON_ERROR_SINGLETALK, extras);
		}

	};

	/**
	 * @Description : 【可忽略】发送命令给VirtualKey应用，以便能够从底层模拟系统上、下、左、右、中等按键操作
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 * @param keyCode
	 */
	private void sendVirtualKeyMessage(int keyCode) {
		LogUtil.d(TAG, "sendVirtualKeyMessage   " + keyCode);
		Intent intent = new Intent();
		intent.setAction(MSG_VIRTUAL_KEY);
		intent.putExtra(KEY_CODE, keyCode);
		sendBroadcast(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtil.d(TAG, "onCreate");

		mKeyServiceExist = KeyUtil.hasAppClient(this);
		initVersionCode();

		mTalkService = this;
		mUserPreference = new UserPreference(this);
		registReceiver();

		// TODO 创建语音魔方实例
		mVoiceAssistant = new VoiceAssistant(this);
		// TODO 设置主监听
		mVoiceAssistant.setListener(mVoiceAssistantListener);
		// TODO 让语音魔方开始初始化
		mVoiceAssistant.init();
		// TODO 可通过下面方法设置当前所在城市，一些领域对城市信息有需求，会优先使用该数据为默认当前城市
		// cn.yunzhisheng.preference.PrivatePreference.setValue("default_city",
		// Util.getCurrentCity(this));
		// TODO 是否启用VAD停止；true-开启；false-关闭；关闭后，语音识别将无法根据
		mVoiceAssistant.setVadEnable(false);
		// TODO TTS初始化
		mTTSPlayer = TTSFactory.createTTSControl(this, "");
		// TODO 设置TTS回调
		mTTSPlayer.setTTSListener(mListener);
		// TODO 设置TTS类型，这里都是2，表示离线TTS
		mTTSPlayer.setType(2);
		// TODO 设置TTS声音类型，推荐使用STREAM_MUSIC
		mTTSPlayer.setStreamType(AudioManager.STREAM_MUSIC);
		// TODO 初始化TTS的模型数据
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
				String folderFiles = getFilesDir().getAbsolutePath() + File.separator;
				mTTSPlayer.initTTSEngine(TalkService.this, folderFiles);
//			}
//		}).start();
		// TODO 【重点】获取语音魔方的服务器插件
		mServerOperate = (IServerOperate) mVoiceAssistant.getOperate("OPERATE_SERVER");

		if (mServerOperate != null) {
			// TODO 设置服务器回调
			mServerOperate.setServerListener(mServer3Listener);

			mServerOperate.setName(mUserPreference.getString(
				UserPreference.TV_FRIENDLY_NAME_KEY,
				UserPreference.TV_FRIENDLY_NAME_DEFAULT));
			mServerOperate.startServer();
		}
	}

	private void registReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Network.CONNECTIVITY_CHANGE_ACTION);
		filter.addAction(MSG_VIRTUAL_KEY_SERVER_ACTIVE);
		filter.addAction(SessionPreferenceOfIntent.INTENT_SETTVNAME);

		filter.addAction(SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_STOP);
		filter.addAction(SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_PLAY);
		filter.addAction(SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_START);
		filter.addAction(SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_PAUSE);
		filter.addAction(SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_BUFFER);
		filter.addAction(SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_PROGRESS);

		filter.addAction(SessionPreferenceOfIntent.INTENT_SETTVNAME);
		filter = MessageManager.registPrivateIntentFilter(filter, getApplicationContext());
		registerReceiver(mBroadcastReceiver, filter);
	}

	private void unregistReceiver() {
		try {
			unregisterReceiver(mBroadcastReceiver);
		} catch (Exception e) {
		}
	}

	/**
	 * @Description : TODO 启动语音魔方流程
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private void startTalk() {
		LogUtil.d(TAG, "startTalk");
		mVoiceAssistant.start();

	}

	/**
	 * @Description : TODO 结束录音
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private void stopTalk() {
		mVoiceAssistant.stop();
	}

	/**
	 * @Description : TODO 取消整个流程
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private void cancelTalk() {
		mVoiceAssistant.cancel();
	}

	/**
	 * @Description : TODO 使用自定义文字替换本次语音输入
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 * @param text
	 */
	private void putCustomText(String text) {
		mVoiceAssistant.putCustomText(text);
	}

	/**
	 * @Description : TODO 设置语音魔方协议，以便跳过一些流程
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 * @param protocal
	 */
	private void setProtocal(String protocal) {
		LogUtil.d(TAG, "setProtocal protocal:" + protocal);
		mVoiceAssistant.setProtocal(protocal);
	}

	/**
	 * @Description : TODO 语音魔方流程启动时的处理
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private void onTalkStart() {
		LogUtil.d(TAG, "onTalkStart");
		showMessage(TALK_EVENT_ON_START);
	}

	/**
	 * @Description : TODO 语音模仿停止录音时的处理
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private void onTalkStop() {
		LogUtil.d(TAG, "onTalkStop");
		showMessage(TALK_EVENT_ON_STOP);
	}

	/**
	 * @Description : TODO 语音魔方录音真正开始时的处理
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private void onRecordingStart() {
		LogUtil.d(TAG, "onRecordingStart");
		showMessage(TALK_EVENT_ON_RECORDING_START);
	}

	/**
	 * @Description : TODO 录音音量更新时的处理
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 * @param volume
	 */
	private void onUpdateVolume(int volume) {
		Bundle extras = new Bundle();
		extras.putInt(TALK_DATA_VOLUME, volume);
		showMessage(TALK_EVENT_ON_UPDATE_VOLUME, extras);
	}

	/**
	 * @Description : TODO 流程取消时的处理
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private void onTalkCancel() {
		LogUtil.d(TAG, "onTalkCancel");
		showMessage(TALK_EVENT_ON_CANCEL);
	}

	/**
	 * @Description : TODO 【主要】魔方协议回调时的处理，此处为SessionManager的促发点
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 * @param protocal
	 */
	private void onSessionProtocal(String protocal) {
		LogUtil.d(TAG, "onSessionProtocal:" + protocal);
		Bundle extras = new Bundle();
		extras.putString(TALK_DATA_PROTOCAL, protocal);
		showMessage(TALK_EVENT_ON_SESSION_PROTOCAL, extras);
	}

	/**
	 * @Description : TODO 播报TTS
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 * @param tts
	 */
	private void playTTS(String tts) {
		LogUtil.d(TAG, "playTTS tts=" + tts);
		if (mTTSPlayer == null) {
			LogUtil.e(TAG, "TTSPlayer instance is null!");
		} else if (!TextUtils.isEmpty(tts)) {
			tts = tts.toLowerCase(Locale.getDefault());
			mTTSPlayer.play(tts);
		}
	}

	/**
	 * @Description : TODO 停止TTS
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private void stopTTS() {
		LogUtil.d(TAG, "stopTTS");
		if (mTTSPlayer != null) {
			mTTSPlayer.stop();
		}
	}

	/**
	 * @Description : TODO 当控制器接入之后的处理
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 * @param client
	 */
	private void onConnectClient(String client) {
		LogUtil.d(TAG, "onConnectClient client:" + client);
		Bundle extras = new Bundle();
		extras.putString(MOBILE_CONTROL_CONNECT_CLIENT, client);
		showMessage(MOBILE_CONTROL_CONNECTION, extras);
	}

	/**
	 * @Description : TODO 当控制器断开之后的处理
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 * @param client
	 */
	public void onDisconnectClient(String client) {
		LogUtil.d(TAG, "onDisconnectClient client:" + client);
		Bundle extras = new Bundle();
		extras.putString(MOBILE_CONTROL_DISCONNECT_CLIENT, client);
		showMessage(MOBILE_CONTROL_DISCONNECTION, extras);
	}

	private void sendUpdateErrorToControl(ErrorUtil error) {
		LogUtil.d(TAG, "sendUpdateErrorToControl:");
		if (mServerOperate != null) {
			JSONObject obj = new JSONObject();
			try {
				obj.put(SessionPreference.KEY_TYPE, SessionPreference.TYPE_VERSION_INFO);
				obj.put(SessionPreference.KEY_HAS_NEW, hasNewVersion);
				obj.put(SessionPreference.KEY_OLD_CODE, mServerVersionCode);
				mServerOperate.sendCustomDataToControl(obj.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void initVersionCode() {
		PackageManager manager = getPackageManager();
		try {
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			String vName = info.versionName;
			LogUtil.d(TAG, "initVersionCode : " + vName);
			mServerVersionCode = vName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @Description	: 给手机端发送一个设置电视名成功数据包
	 * @Author		: Dancindream
	 * @CreateDate	: 2014-6-18
	 */
	private void sendSetTVNameSuccessToControl() {
		if (mServerOperate != null) {
			JSONObject obj = new JSONObject();
			try {
				obj.put(SessionPreference.KEY_TYPE, SessionPreference.KEY_SETTVNAME_SUCCESS);
				obj.put(SessionPreference.KEY_NAME, mUserPreference.getString(
					UserPreference.TV_FRIENDLY_NAME_KEY,
					UserPreference.TV_FRIENDLY_NAME_DEFAULT));
				LogUtil.d(TAG, "sendSetTVNameSuccessToControl : " + obj.toString());
				mServerOperate.sendCustomDataToControl(obj.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 音乐状态 发送消息给手机端
	 */
	private void sendMusicStartToControl(int status, int percent, int duration) {
		if (mServerOperate != null) {
			JSONObject obj = new JSONObject();
			try {
				switch (status) {
				case SessionPreference.MUSIC_STATUS_STOP:
					obj.put(SessionPreference.KEY_TYPE, SessionPreference.KEY_SEND_MUSIC_STATUS);
					obj.put(SessionPreference.KEY_NAME, status);
					mServerOperate.sendCustomDataToControl(obj.toString());
					LogUtil.d(TAG, "sendMusicStartToControl obj : " + obj.toString());
					break;
				case SessionPreference.MUSIC_STATUS_START:
					obj.put(SessionPreference.KEY_TYPE, SessionPreference.KEY_SEND_MUSIC_STATUS);
					obj.put(SessionPreference.KEY_NAME, status);
					mServerOperate.sendCustomDataToControl(obj.toString());
					LogUtil.d(TAG, "sendMusicStartToControl obj : " + obj.toString());
					break;
				case SessionPreference.MUSIC_STATUS_PAUSE:
					obj.put(SessionPreference.KEY_TYPE, SessionPreference.KEY_SEND_MUSIC_STATUS);
					obj.put(SessionPreference.KEY_NAME, status);
					mServerOperate.sendCustomDataToControl(obj.toString());
					LogUtil.d(TAG, "sendMusicStartToControl obj : " + obj.toString());
					break;
				case SessionPreference.MUSIC_STATUS_BUFFER:
					obj.put(SessionPreference.KEY_TYPE, SessionPreference.KEY_SEND_MUSIC_STATUS);
					obj.put(SessionPreference.KEY_NAME, status);
					obj.put(SessionPreferenceOfIntent.KEY_MUSIC_BUFFER_PERCENT, percent);
					mServerOperate.sendCustomDataToControl(obj.toString());
					LogUtil.d(TAG, "sendMusicStartToControl obj : " + obj.toString());
					break;
				case SessionPreference.MUSIC_STATUS_PROGRESS:
					obj.put(SessionPreference.KEY_TYPE, SessionPreference.KEY_SEND_MUSIC_STATUS);
					obj.put(SessionPreference.KEY_NAME, status);
					obj.put(SessionPreferenceOfIntent.KEY_MUSIC_PROGRESS_DURATION, duration);
					obj.put(SessionPreferenceOfIntent.KEY_MUSIC_PROGRESS_TIME, percent);
					mServerOperate.sendCustomDataToControl(obj.toString());
					LogUtil.d(TAG, "sendMusicStartToControl obj : " + obj.toString());
					break;
				case SessionPreference.MUSIC_STATUS_PLAY:
					obj.put(SessionPreference.KEY_TYPE, SessionPreference.KEY_SEND_MUSIC_STATUS);
					obj.put(SessionPreference.KEY_NAME, status);
					mServerOperate.sendCustomDataToControl(obj.toString());
					LogUtil.d(TAG, "sendMusicStartToControl obj : " + obj.toString());
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Description : TODO 启动界面
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private void startAssistant() {
		LogUtil.d(TAG, "startAssistant");
		String message = "";
		if (MessageReceiver.ACTION_START_TALK_ARRAY != null && MessageReceiver.ACTION_START_TALK_ARRAY.length > 0) {
			message = MessageReceiver.ACTION_START_TALK_ARRAY[0];
		}
		if (message != null && !message.equals("")) {
			Bundle extras = new Bundle();
			extras.putBoolean(WindowService.BUNDLE_DATA_FROM_MOBILE_CONTROL, true);
			showMessage(message, extras);
		}
	}

	/**
	 * @Description : TODO 关闭界面
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private void exitAssistant() {
		LogUtil.d(TAG, "exitAssistant");
		showMessage(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
	}

	/**
	 * @Description : TODO 模拟Back键
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private void onVirtureKeyBack() {
		LogUtil.d(TAG, "onVirtureKeyBack");
		showMessage(WindowService.MSG_VIRTUAL_KEY_BACK);
	}

	/**
	 * @Description : TODO 模拟Center键
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private void onVirtureKeyCenter() {
		LogUtil.d(TAG, "onVirtureKeyCenter");
		showMessage(WindowService.MSG_VIRTUAL_KEY_CENTER);
	}

	/**
	 * @Description : TODO 模拟Left键
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private void onVirtureKeyLeft() {
		LogUtil.d(TAG, "onVirtureKeyLeft");
		showMessage(WindowService.MSG_VIRTUAL_KEY_LEFT);
	}

	/**
	 * @Description : TODO 模拟Right键
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private void onVirtureKeyRight() {
		LogUtil.d(TAG, "onVirtureKeyRight");
		showMessage(WindowService.MSG_VIRTUAL_KEY_RIGHT);
	}

	/**
	 * @Description : TODO 模拟Up键
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private void onVirtureKeyUp() {
		LogUtil.d(TAG, "onVirtureKeyUp");
		showMessage(WindowService.MSG_VIRTUAL_KEY_UP);
	}

	/**
	 * @Description : TODO 模拟Down键
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	private void onVirtureKeyDown() {
		LogUtil.d(TAG, "onVirtureKeyDown");
		showMessage(WindowService.MSG_VIRTUAL_KEY_DOWN);
	}

	/**
	 * @Description : TODO 提供给APP获取在线和离线的支持领域列表
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 * @param hasNetWork
	 * @return
	 */
	public static ArrayList<String> getSupportList(boolean hasNetWork) {
		if (hasNetWork) {
			return mOnlineSupportList;
		} else {
			return mOfflineSupportList;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtil.d(TAG, "onStartCommand:" + intent);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		LogUtil.d(TAG, "onDestroy");
		mServiceCompileLooper.quit();
		mServerOperate.stopServer();
		mVoiceAssistant.release();
		mServerOperate = null;
		mVoiceAssistant = null;
		mTalkService = null;
		mTTSPlayer.releaseTTSEngine();
		mTTSPlayer = null;
		unregistReceiver();
		super.onDestroy();
	}

	private void showMessage(String message, Bundle extras) {
		MessageManager.sendPrivateMessage(getApplicationContext(), message, extras);
	}

	private void showMessage(String message) {
		MessageManager.sendPrivateMessage(getApplicationContext(), message);
	}
}
