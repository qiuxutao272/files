package cn.yunzhisheng.vui.assistant.tv.session;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;
import cn.yunzhisheng.common.JsonTool;
import cn.yunzhisheng.common.net.Network;
import cn.yunzhisheng.common.util.LogUtil;
import cn.yunzhisheng.tts.offline.common.USCError;
import cn.yunzhisheng.vui.assistant.VoiceAssistant;
import cn.yunzhisheng.vui.assistant.model.KnowledgeMode;
import cn.yunzhisheng.vui.assistant.oem.RomControl;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.vui.assistant.tv.MainActivity;
import cn.yunzhisheng.vui.assistant.tv.WindowService;
import cn.yunzhisheng.vui.assistant.tv.media.PlayList;
import cn.yunzhisheng.vui.assistant.tv.media.PlayerEngine;
import cn.yunzhisheng.vui.assistant.tv.talk.ITalkServicePresentorListener;
import cn.yunzhisheng.vui.assistant.tv.talk.TalkServicePresentor;
import cn.yunzhisheng.vui.assistant.tv.view.SessionContainer;
import cn.yunzhisheng.vui.assistant.tv.view.VoiceMode;

/**
 * @Module : Session层核心类
 * @Comments : 根据协议进行Session生成和协议分发，该类为Session层的核心枢纽
 * @Author : Dancindream
 * @CreateDate : 2014-4-1
 * @ModifiedBy : Dancindream
 * @ModifiedDate: 2014-4-1
 * @Modified:
 * 2014-4-1: 实现基本功能
 */
@SuppressLint("HandlerLeak")
public class SessionManager {
	private static final String TAG = "SessionManager";

	public static final int TASK_DELAY_KEY_TTS_END = 100;
	public static final int TASK_DELAY_KEY_SESSION_END = 101;
	public static final int TASK_DELAY_VALUE_RESET_TIMER = 1;

	private static final String BACK_PROTOCAL = VoiceAssistant.BACK_PROTOCAL;

	private WindowService mContext = null;
	private SessionContainer mSessionContainer = null;
	private SessionContainer mSessionViewContainer = null;
	private boolean mNeedAutoStart = true;
	private TalkServicePresentor mTalkServicePresentor = null;
	private BaseSession mCurrentSession = null;
	private DelayTaskArray mDelayTaskArray = new DelayTaskArray();
	private PopupWindow mPopupWindow = null;
	private ISessionManagerListener mListener;

	/**
	 * @Description : TODO 提供主线程的方法调用
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-1
	 */
	private Handler mSessionManagerHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			LogUtil.d(TAG, "handleMessage:" + msg);
			switch (msg.what) {
			// TODO 开始语音识别流程
			case SessionPreference.MESSAGE_START_TALK:
				startTalk();
				break;
			// TODO 结束语音录音
			case SessionPreference.MESSAGE_STOP_TALK:
				stopTalk();
				break;
			// TODO 取消语音识别流程
			case SessionPreference.MESSAGE_CANCEL_TALK:
				cancelTalk();
				break;
			// TODO 取消Session操作流程
			case SessionPreference.MESSAGE_SESSION_CANCEL:
				if (mCurrentSession != null) {
					mCurrentSession.release();
					mCurrentSession = null;
				}
				int value = mDelayTaskArray.pop(TASK_DELAY_KEY_SESSION_END);
				if (value != 0) {
					handleDelayTask(value);
				}
				break;
			// TODO Session操作流程已完成
			case SessionPreference.MESSAGE_SESSION_DONE:
				mSessionViewContainer.clearTemporaryViews();
				if (mCurrentSession != null) {
					mCurrentSession.release();
					mCurrentSession = null;
				}
				value = mDelayTaskArray.pop(TASK_DELAY_KEY_SESSION_END);
				if (value != 0) {
					handleDelayTask(value);
				}
				break;
			// TODO 开始音乐播放（通知最上层处理）
			case SessionPreference.MESSAGE_REQUEST_MUSIC_START:
				if (mListener != null) {
					if (mCurrentSession instanceof MusicShowSession) {
						mListener.onMusicSearchDone(((MusicShowSession) mCurrentSession).getPlayList());
					}
				}
				break;
			// TODO 播报文字
			case SessionPreference.MESSAGE_REQUEST_PLAY_TTS:
				//------------------xuhua
//				mTalkServicePresentor.playTTS((String) msg.obj);
				break;
			// TODO 取消播报文字
			case SessionPreference.MESSAGE_REQUEST_CANCEL_TTS:
				//--------------------xuhua
//				mTalkServicePresentor.stopTTS();
				break;
			// TODO 直接跳转到新的Session
			case SessionPreference.MESSAGE_REQUEST_RESET_TIMER:
				if (mListener != null) {
					mListener.onResetTimer();
				}
				break;
			case SessionPreference.MESSAGE_REQUEST_CANCEL_TIMER:
				if (mListener != null) {
					mListener.onCancelTimer();
				}
				break;
			case SessionPreference.MESSAGE_NEW_PROTOCAL:
				String strProtocolString = (String) msg.obj;
				createSession(strProtocolString);
				break;
			// TODO VAD方式自动结束录音
			case SessionPreference.MESSAGE_TASK_DELY:
				mDelayTaskArray.put(msg.arg1, msg.arg2);
				break;
			// TODO 添加答句文字（语音魔方回答的文字）
			case SessionPreference.MESSAGE_ADD_ANSWER_TEXT:
				String text = (String) msg.obj;
				mSessionViewContainer.addAnswerView(text);
				break;
			// TODO 添加回答的View（语音魔方需要展现的View）
			case SessionPreference.MESSAGE_ADD_ANSWER_VIEW:
				View view = (View) msg.obj;
				mSessionViewContainer.addSessionView(view, false);
				break;
			// TODO 添加问句文字（用户说的话）
			case SessionPreference.MESSAGE_ADD_QUESTION_TEXT:
				mSessionViewContainer.addQustionView((String) msg.obj);
				break;
			// TODO 返还给语音魔方的协议（用于业务流程跳转）
			case SessionPreference.MESSAGE_UI_OPERATE_PROTOCAL:
				String protocal = (String) msg.obj;
				if (mTalkServicePresentor != null) {
					mTalkServicePresentor.setProtocal(protocal);
				}
				break;
			case SessionPreference.MESSAGE_HIDE_WINDOW:
				mContext.dismiss();
				break;
			case SessionPreference.MESSAGE_MOREFUNCTION_PROTOCAL:
				break;
			}
		}

	};

	public void hidePopup() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}

	/**
	 * @Description : TODO TalkServicePresentor回调
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-1
	 */
	private ITalkServicePresentorListener mTalkServicePresentorListener = new ITalkServicePresentorListener() {

		@Override
		public void onTalkStop() {
			SessionManager.this.onTalkStop();
		}

		@Override
		public void onTalkStart() {
			SessionManager.this.onTalkStart();
		}

		@Override
		public void onSessionProtocal(String protocal) {
			SessionManager.this.onSessionProtocal(protocal);
		}

		@Override
		public void onTalkInitDone() {
			SessionManager.this.onTalkInitDone();
		}

		@Override
		public void onTalkCancel() {
			SessionManager.this.onTalkCancel();
		}

		@Override
		public void onBuffer() {
			SessionManager.this.onBuffer();
		}

		@Override
		public void onPlayBegin() {
			SessionManager.this.onPlayBegin();
		}

		@Override
		public void onPlayEnd() {
			SessionManager.this.onPlayEnd();
		}

		@Override
		public void onConnectClient(String client) {
			SessionManager.this.onConnectClient(client);
		}

		@Override
		public void onDisconnectClient(String client) {
			SessionManager.this.onDisconnectClient(client);
		}

		@Override
		public void onTalkRecordingStart() {
			SessionManager.this.onTalkRecordingStart();
		}

		@Override
		public void onConnectivityChanged() {
			SessionManager.this.onConnectivityChanged();
		}

		@Override
		public void onUpdateVolume(int volume) {
			SessionManager.this.onUpdateVolume(volume);
		}

		@Override
		public void onCancel() {
			SessionManager.this.onPlayCancel();
		}

		@Override
		public void onMoveUp() {
			SessionManager.this.onMoveUp();
		}

		@Override
		public void onMoveDown() {
			SessionManager.this.onMoveDown();
		}

		@Override
		public void onError(USCError arg0) {

		}

		@Override
		public void onInitFinish() {

		}

		@Override
		public void onErrorSingleTalk(String host, String talkStatus) {
			SessionManager.this.onErrorSingleTalk(host, talkStatus);
		}

		@Override
		public void onTtsData(byte[] arg0) {
			
		}
	};

	public SessionManager(WindowService context, SessionContainer sessionContainer) {
		mContext = context;
		mSessionContainer = sessionContainer;
		init();

	}

	private void init() {
		mSessionViewContainer = (SessionContainer) mSessionContainer;
		mTalkServicePresentor = new TalkServicePresentor(mContext, mTalkServicePresentorListener);
	}

	public void setSessionListener(ISessionManagerListener listener) {
		mListener = listener;
	}

	/**
	 * @Description : TODO 主动启动语音识别流程
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-1
	 */
	public void startTalk() {
		LogUtil.d(TAG, "startTalk");
		mTalkServicePresentor.startTalk();
	}

	/**
	 * @Description : TODO 主动停止录音
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-1
	 */
	public void stopTalk() {
		LogUtil.d(TAG, "stopTalk");
		mTalkServicePresentor.stopTalk();
	}

	public void startByControlButton() {
		LogUtil.d(TAG, "startByControlButton");
		startTalk();
	}

	public void stopByControlButton() {
		LogUtil.d(TAG, "stopByControlButton");
		stopTalk();
	}

	/**
	 * @Description : TODO 主动取消识别流程
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-1
	 */
	public void cancelTalk() {
		LogUtil.d(TAG, "cancelTalk");
		mTalkServicePresentor.cancelTalk();
	}

	/**
	 * @Description : TODO 等待识别结果流程
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-1
	 */
	private void waitForRecognitionResult() {
		LogUtil.d(TAG, "waitForRecognitionResult");
		mContext.updateVoiceView(VoiceMode.MODE_RECOGNISING);
	}

	public void resetTalk(VoiceMode mode) {
	}

	public void onPause() {
		LogUtil.d(TAG, "onPause");
		hidePopup();
		PlayerEngine.getInstance().pause();
		mTalkServicePresentor.stopTTS();
		resetTalk(VoiceMode.MODE_DEFAULT);

		mTalkServicePresentor.onPause();
	}

	public void stopTTS() {
		mTalkServicePresentor.stopTTS();
		if (mCurrentSession != null) {
			mCurrentSession = null;
		}
	}

	public boolean getPopupWindowIsShowing() {
		if (mPopupWindow != null) {
			return mPopupWindow.isShowing();
		} else {
			return false;
		}
	}

	public void onResume() {
		LogUtil.d(TAG, "onResume");
		mTalkServicePresentor.onResume();
		mNeedAutoStart = true;
	}

	public void onDestroy() {
		LogUtil.d(TAG, "onDestroy");
		if (mCurrentSession != null) {
			mCurrentSession = null;
			if (mTalkServicePresentor != null) {
				mTalkServicePresentor.setProtocal(BACK_PROTOCAL);
			}
		}
		mSessionViewContainer.removeAllSessionViews();
		mDelayTaskArray.clear();
		mTalkServicePresentor.onDestroy();
	}

	public boolean onBackPressed() {
		LogUtil.d(TAG, "onBackPressed");
		boolean result = false;
		mTalkServicePresentor.stopTTS();
		if (mCurrentSession != null) {
			result = true;
		}

		return result;
	}

	public void onSessionProtocal(String protocal) {
		mContext.showMainContent();
		MainActivity.mVoiceKeyFlag = false;
		createSession(protocal);
	}

	/**
	 * @Description : TODO 当TalkService初始化完成的回调
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-1
	 */
	private void onTalkInitDone() {
		LogUtil.d(TAG, "onTalkServiceInitDone mNeedAutoStart=" + mNeedAutoStart);
		Log.d("SHOW_TIME", "SessionManager:TALK SERVICE INIT DONE");

		if (mNeedAutoStart) {
			mNeedAutoStart = false;
		}
	}

	private void onConnectivityChanged() {
		LogUtil.d(TAG, "onConnectivityChanged");
		boolean hasNetwork = Network.hasNetWorkConnect();

		if (mListener != null) {
			mListener.onConnectivityChanged(hasNetwork);
		}
	}

	public void onBuffer() {

	}

	public void onPlayBegin() {

	}

	public void onPlayEnd() {
		LogUtil.d(TAG, "onPlayEnd");
		//int value = mDelayTaskArray.pop(TASK_DELAY_KEY_TTS_END);
		if (mListener != null) {
			//handleDelayTask(TASK_DELAY_VALUE_RESET_TIMER);
		}
	}

	private void handleDelayTask(int value) {
		switch (value) {
		case TASK_DELAY_VALUE_RESET_TIMER:
			if (mListener != null) {
				LogUtil.d(TAG, "onResetTimer");
				mListener.onResetTimer();
			}
			break;
		}
	}

	/**
	 * @Description : TODO 核心方法，根据协议生成Session对象，并处理协议分发
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-1
	 * @param protocal
	 */
	private void createSession(String protocal) {
		LogUtil.d(TAG, "createSession:" + protocal);
		JSONObject obj = JsonTool.parseToJSONObject(protocal);
		BaseSession base = null;
		if (obj != null) {
			String sessionStatus = JsonTool.getJsonValue(obj, SessionPreference.KEY_DOMAIN, "");
			// TODO 如果是一个新领域流程的开始，是否需要将历史记录清理（参数控制）
			if (SessionPreference.VALUE_SESSION_BENGIN.equals(sessionStatus)) {
				mSessionViewContainer.removeAllSessionViews();
			}

			String type = JsonTool.getJsonValue(obj, SessionPreference.KEY_TYPE, "");
			// TODO 根据type进行实例创建
			
			if (SessionPreference.VALUE_TYPE_INPUT_CONTENT_WEIBO.equals(type)) {
				if (mCurrentSession != null && mCurrentSession instanceof WeiBoInputShowSession) {
					base = mCurrentSession;
				} else {
					base = new WeiBoInputShowSession(mContext, mSessionManagerHandler);
				}
			} else if (SessionPreference.DOMAIN_FULLVOICE_SHOW.equals(type)) {
				base = new FullVoiceShowSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_TYPE_WEIBO_OK.equals(type)) {
				base = new WeiBoShowSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_TYPE_WAITING.equals(type)) {
				if (mCurrentSession != null && mCurrentSession instanceof WeiBoInputShowSession) {
					base = mCurrentSession;
				} else {
					JSONObject mDataObject = JsonTool.getJSONObject(obj, SessionPreference.KEY_DATA);
					if (mDataObject != null) {
						String waitTitle = JsonTool.getJsonValue(mDataObject, SessionPreference.KEY_ANSWER, "");
						mContext.showWaiting(waitTitle);
						base = new WaitingSession(mContext, mSessionManagerHandler);
					}

				}
			} else if (SessionPreference.VALUE_TYPE_WEATHER_SHOW.equals(type)) {
				base = new WeatherShowSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_TYPE_WEB_SHOW.equals(type)) {
				base = new WebShowSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_TYPE_TRANSLATION_SHOW.equals(type)) {
				base = new TranslationShowSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_TYPE_STOCK_SHOW.equals(type)) {
				base = new StockShowSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_TYPE_MUSIC_SHOW.equals(type)) {
				base = new MusicShowSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_TYPE_PROG_RECOMMEND.equals(type)) {
				base = new ProgRecommendSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_TYPE_PROG_SEARCH_RESULT.equals(type)) {
				base = new ProgSearchResultSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_TYPE_CHANNEL_PROG_LIST.equals(type)) {
				base = new ChannelProgListSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_TYPE_APP_LAUNCH.equals(type)) {
				base = new AppLaunchSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_TYPE_APP_UNINSTALL.equals(type)) {
				base = new AppUninstallSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_TYPE_SETTING.equals(type)) {
				base = new SettingSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_TYPE_TALK_SHOW.equals(type)
						|| SessionPreference.VALUE_TYPE_INPUT_CONTACT.equals(type)) {
				base = new TalkShowSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_TYPE_ERROR_SHOW.equals(type)) {
				base = new ErrorShowSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_TYPE_UI_HANDLE_SHOW.equals(type)) {
				base = new VideoShowSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_TYPE_APP_EXIT.equals(type)) {
				base = new AppExitSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_UI_PROTOCAL_SHOW.equals(type)) {
				String originType = null;
				try {
					originType = obj.getString(SessionPreference.KEY_ORIGIN_TYPE);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (SessionPreference.DOMAIN_VIDEO.equals(originType)) {
					base = new VideoShowSession(mContext, mSessionManagerHandler);
				} else if (SessionPreference.DOMAIN_LOCAL.equals(originType)) {
					base = new HelpShowSession(mContext, mSessionManagerHandler);
				} else {
					base = new UnsupportShowSession(mContext, mSessionManagerHandler);
				}
			} else if (SessionPreference.VALUE_TYPE_CHANNEL_SWITCH_SHOW.equals(type)) {
				base = new ChannelSwitchSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_TYPE_SHOP_SHOW.equals(type)) {
				base = new ShopShowSession(mContext, mSessionManagerHandler);
			} else if (SessionPreference.VALUE_TYPE_HELP_SHOW.equals(type)) {
				base = new HelpShowSession(mContext, mSessionManagerHandler);
			} 
		}

		if (base != null) {
			mSessionViewContainer.clearTemporaryViews();
			mCurrentSession = base;
			mCurrentSession.putProtocol(obj);
		} else {
			JSONObject dataObj = BaseSession.getJSONObject(obj, SessionPreference.KEY_DATA);
			String message = BaseSession.getJsonValue(dataObj, SessionPreference.KEY_QUESTION);
			String ttString = null;
			if (TextUtils.isEmpty(message)) {
				mSessionViewContainer.clearTemporaryViews();
				ttString = KnowledgeMode.getKnowledgeAnswer(mContext, KnowledgeMode.KNOWLEDGE_STAGE_NO_INPUT);
			} else {
				mSessionViewContainer.addQustionView(message);
				ttString = KnowledgeMode.getRecognitionNoResultAnswer(mContext, message);
			}
			addAnswerText(ttString);
			playTTS(ttString);
		}
	}

	private void addAnswerText(String text) {
		Message msg = new Message();
		msg.what = SessionPreference.MESSAGE_ADD_ANSWER_TEXT;
		msg.obj = text;
		mSessionManagerHandler.sendMessage(msg);
	}

	private void playTTS(String text) {
		Message tts = mSessionManagerHandler.obtainMessage(SessionPreference.MESSAGE_REQUEST_PLAY_TTS);
		tts.obj = text;
		mSessionManagerHandler.sendMessage(tts);
	}

	public void onFunctionClick() {
		onBackPressed();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (mCurrentSession != null) {
			mCurrentSession.onActivityResult(requestCode, resultCode, data);
		}
	}

	public static interface ISessionManagerListener {
		void onConnectivityChanged(boolean hasNetwork);

		void onMusicSearchDone(PlayList playList);

		void onResetTimer();

		void onCancelTimer();

		void onConnectClient(String client);

		void onDisconnectClient(String client);
	}

	private static class DelayTaskArray extends SparseIntArray {
		public int pop(int key) {
			int value = get(key);
			delete(key);
			return value;
		}
	}

	private void onTalkStart() {
		LogUtil.d(TAG, "onTalkStart");
		mContext.showFloat();
		RomControl.enterControl(mContext, RomControl.START_ASSISTANT);
		mContext.updateVoiceView(VoiceMode.MODE_RECORDINGINIT);
		mTalkServicePresentor.stopTTS();
	}

	public void onTalkRecordingStart() {
		LogUtil.d(TAG, "onTalkRecordingStart");
		mContext.updateVoiceView(VoiceMode.MODE_RECORDING);
	}

	private void onUpdateVolume(int volume) {
		mContext.updateVolume(volume);
	}

	private void onTalkStop() {
		LogUtil.d(TAG, "onTalkStop");
		waitForRecognitionResult();
	}

	private void onTalkCancel() {
		LogUtil.d(TAG, "onTalkCancel");
		mContext.updateVoiceView(VoiceMode.MODE_DEFAULT);
		mContext.dismiss();
		MainActivity.mVoiceKeyFlag = false;
		resetTalk(VoiceMode.MODE_DEFAULT);
	}

	private void onPlayCancel() {

	}

	public void onConnectClient(String client) {
		if (mListener != null) {
			mListener.onConnectClient(client);
		}
	}

	public void onDisconnectClient(String client) {
		if (mListener != null) {
			mListener.onDisconnectClient(client);
		}
	}

	private void onMoveUp() {
		LogUtil.d(TAG, "onMoveUp");
		mContext.onMoveUp();
	}

	private void onMoveDown() {
		LogUtil.d(TAG, "onMoveDown");
		mContext.onMoveDown();
	}

	private void onErrorSingleTalk(String host, String talkStatus) {
		LogUtil.d(TAG, "onErrorCanNotTalk");
		mContext.onErrorSingleTalk(host, talkStatus);
	}
}
