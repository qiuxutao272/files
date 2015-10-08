/**
 * Copyright (c) 2012-2013 Yunzhisheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : WindowService.java
 * @ProjectName : letvAssistant
 * @PakageName : cn.yunzhisheng.voicetv
 * @Author : Brant
 * @CreateDate : 2013-5-13
 */
package cn.yunzhisheng.vui.assistant.tv;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.yunzhisheng.common.util.LogUtil;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.assistant.model.KnowledgeMode;
import cn.yunzhisheng.vui.assistant.preference.PrivatePreference;
import cn.yunzhisheng.vui.assistant.preference.SessionPreferenceOfIntent;
import cn.yunzhisheng.vui.assistant.preference.UserPreference;
import cn.yunzhisheng.vui.assistant.tv.media.MusicPlaybackService;
import cn.yunzhisheng.vui.assistant.tv.media.MusicPlaybackService.MusicBinder;
import cn.yunzhisheng.vui.assistant.tv.media.PlayList;
import cn.yunzhisheng.vui.assistant.tv.media.PlayerEngine;
import cn.yunzhisheng.vui.assistant.tv.media.Track;
import cn.yunzhisheng.vui.assistant.tv.session.SessionManager;
import cn.yunzhisheng.vui.assistant.tv.session.SessionManager.ISessionManagerListener;
import cn.yunzhisheng.vui.assistant.tv.view.MusicPopupWindow;
import cn.yunzhisheng.vui.assistant.tv.view.MusicPopupWindow.MusicPopWindowCallback;
import cn.yunzhisheng.vui.assistant.tv.view.MusicPopupWindow.MusicPopupWindowListener;
import cn.yunzhisheng.vui.assistant.tv.view.SessionContainer;
import cn.yunzhisheng.vui.assistant.tv.view.TVSearchContentView;
import cn.yunzhisheng.vui.assistant.tv.view.VoiceMode;
import cn.yunzhisheng.vui.assistant.tv.view.WindowPopupDialog;
import cn.yunzhisheng.vui.assistant.util.MessageManager;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Brant
 * @CreateDate : 2013-5-13
 * @ModifiedBy : Brant
 * @ModifiedDate: 2013-5-13
 * @Modified: 2013-5-13: 实现基本功能
 */
@SuppressLint({ "NewApi", "Wakelock", "CutPasteId" })
public class WindowService extends Service implements OnKeyListener, OnDismissListener {
	private static final String TAG = "WindowService";
	private static final int AUTO_FINISH_DELAY = 5000;

	public static final String MSG_VIRTUAL_KEY_BACK = "MSG_VIRTUAL_KEY_BACK";
	public static final String MSG_VIRTUAL_KEY_CENTER = "MSG_VIRTUAL_KEY_ENTER";
	public static final String MSG_VIRTUAL_KEY_LEFT = "MSG_VIRTUAL_KEY_LEFT";
	public static final String MSG_VIRTUAL_KEY_RIGHT = "MSG_VIRTUAL_KEY_RIGHT";
	public static final String MSG_VIRTUAL_KEY_UP = "MSG_VIRTUAL_KEY_UP";
	public static final String MSG_VIRTUAL_KEY_DOWN = "MSG_VIRTUAL_KEY_DOWN";

	public static final String BUNDLE_DATA_FROM_MOBILE_CONTROL = "BUNDLE_DATA_FROM_MOBILE_CONTROL";

	private final static int MSG_VOICE_BUTTON_START = 0x1;
	private final static int MSG_VOICE_BUTTON_STOP = 0x2;
	private final static int MSG_AUTO_FINISH = 0x3;
	private final static int TIME_VOICE_BUTTON_START = 0;
	private final static int TIME_VOICE_BUTTON_STOP = 300;

	private final static int ADD_FLOAT_WINDOW = 5;
	private final static int REMOVE_FLOAT_WINDOW = 6;

	private final static int MIC_STATUS = 7;

	private Timer mTimer;
	private View mViewNetWorkStatus;
	private SessionContainer sessionContainer;
	private WindowPopupDialog mWindowPopupDialog;
	private TextView mTextViewNetWorkChangeContent;
	private UserPreference mUserPreference;
	private WindowManager mWindowManager;
	private View mFloatView = null;
	private LayoutInflater mInflater;
	private boolean floatWindowIsShowing = false;

	private LinearLayout mMainFloat, mTalkToast, linearLayoutMainView, mFloatMusic;

	private ImageView mVoiceBg;
	private ImageView mVoiceRed;
	private ImageView mVoiceLevel;
	private ImageView mVoiceRing;
	private ImageView mRatoteImage;
	private ImageView mWaitRatoteImage;
	private Animation operatingAnim;
	private Animation music_operatingAnim;
	private Animation wait_operatingAnim;
	private LinearInterpolator lin;
//	private Animation mRecognitionScaleAnim;
//	private Animation mRecordRedScaleInAnim;
//	private Animation mRecordRedScaleOutAnim;
//	private Animation mRecordRightInAnim;
//	private Animation mRecordRightOutAnim;
//	private Animation mRecordAlphaInAnim;
//	private Animation mRecordAlphaOutAnim;
//	private Animation mRecordTxtDownInAnim;
	private LinearLayout voice_start;
	private int mVolume;

	private TextView mTextResul, mTextRecord, mTextWaiting;

	private PlayList mPlayList;
	private int mPlayIndexChange = -1;
	private boolean mPlayOnError;
	private boolean mServiceBinderFlag;
	private TextView mTextViewSong, mTextViewMusicHeaderArtist, mTextViewProgress;
	private MusicPlaybackService mMusicService;
	private MusicPopupWindow mMusicPopupWindow;
	private ImageView mMusicScale;
	private ImageView mMusicPlay;
	private ImageView mMusicPause;
	public List<String> mConnectClienList = new ArrayList<String>();

	public int mConnectClienNum = 0;

	public static WindowService windowService;


	LinearLayout container = null;

	private Button talkingButton;
	private Button nottalkingButton;
	private boolean isMusicShowing = false;
	private boolean isMusicPlaying = false;

	private boolean isMicListener = true;

	public static WindowService getService() {
		return windowService;
	}

	public int getConnectClient() {
		return mConnectClienList.size();
	}

	private SessionManager mSessionManager = null;
	private ISessionManagerListener mSessionManagerListener = new ISessionManagerListener() {

		@Override
		public void onConnectivityChanged(boolean hasNetwork) {
			updateNetWorkStatus(hasNetwork);
		}

		@Override
		public void onResetTimer() {
//			LogUtil.d(TAG, "onResetTimer");
			//resetTimer();
		}

		@Override
		public void onCancelTimer() {
//			LogUtil.d(TAG, "onCancelTimer");
			//cancelTimer();
		}

		@Override
		public void onConnectClient(String client) {
			Log.d(TAG, "onConnectClient client = " + client + " musicShow :　" + isMusicShowing + " musicPlay : "
						+ isMusicPlaying);
			synchronized (mConnectClienList) {
				if (!mConnectClienList.contains(client)) {
					mConnectClienList.add(client);
				}
			}

			showMessage(SessionPreferenceOfIntent.INTENT_CONNECT_CLIENT);

			if (isMusicShowing) {
				showMessage(SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_START);
				if (isMusicPlaying) {
					showMessage(SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_PLAY);
				} else {
					showMessage(SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_PAUSE);
				}
			}

		}

		private void showMessage(String message) {
			Intent intent = new Intent(message);
			LogUtil.d(TAG, "message = " + message);
			sendBroadcast(intent);
		}

		@Override
		public void onDisconnectClient(String client) {
			Log.d(TAG, "onDisconnectClient client = " + client);
			if (mConnectClienList != null && mConnectClienList.size() > 0) {
				synchronized (mConnectClienList) {
					if (mConnectClienList.contains(client)) {
						mConnectClienList.remove(client);
					}
				}
			}

			showMessage(SessionPreferenceOfIntent.INTENT_DISCONNECT_CLIENT);
		}

		@Override
		public void onMusicSearchDone(PlayList playList) {
			mPlayList = playList;
			mPlayIndexChange = 0;
			mPlayOnError = false;
			if (mMusicPopupWindow == null) {
				mMusicPopupWindow = new MusicPopupWindow(WindowService.this);
				mMusicPopupWindow.setAnimationStyle(Res.style.MusicPoPAnimation);
				mMusicPopupWindow.setListener(mMusicPopupWindowListener);
				mMusicPopupWindow.setMusicPopWindowCallback(mMusicPopWinCallback);
			}
			if (mMusicService == null) {
				Intent musicIntent = new Intent(WindowService.this, MusicPlaybackService.class);
				bindService(musicIntent, mMusicConnection, Context.BIND_AUTO_CREATE);
				mServiceBinderFlag = true;
				startService(musicIntent);
			} else {
				mMusicService.setPlayList(mPlayList);
				mMusicService.startPlay();
			}
			mFloatMusic.setVisibility(View.INVISIBLE);
			mMusicPopupWindow.updateMusicList(mPlayList);
			int[] location = new int[2];
			sessionContainer.getLocationOnScreen(location);
			mMusicPopupWindow.showAtLocation(sessionContainer, Gravity.NO_GRAVITY, location[0], 0);
			mMusicPopupWindow.setFocusable(true);

			isMusicShowing = true;
			showMessage(SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_START);
		}
	};

	private MusicPopupWindowListener mMusicPopupWindowListener = new MusicPopupWindowListener() {

		@Override
		public void onShow() {
			LogUtil.d(TAG, "onShow");
		}

		@Override
		public void onDismiss() {
			mFloatMusic.setFocusable(false);
			sessionContainer.requestSuperFocus();
			if (!mPlayOnError) {
				if (mMusicService != null) {
					mFloatMusic.setVisibility(View.VISIBLE);
					if (mMusicPopupWindow != null) {
						mMusicPopupWindow.onMusicStatus(mMusicService.isPlaying());
					}
					if (mMusicService.isPlaying()) {
						mMusicPause.setVisibility(View.GONE);
						mMusicScale.setVisibility(View.VISIBLE);
//						mMusicScale.startAnimation(music_operatingAnim);
						mMusicPlay.setVisibility(View.VISIBLE);
					} else {
						mMusicScale.clearAnimation();
						mMusicScale.setVisibility(View.GONE);
						mMusicPlay.setVisibility(View.GONE);
						mMusicPause.setVisibility(View.VISIBLE);
					}
				}
			} else {
				mMusicScale.setVisibility(View.GONE);
				mMusicScale.clearAnimation();
				mMusicPlay.setVisibility(View.GONE);
				mMusicPause.setVisibility(View.VISIBLE);
				mPlayOnError = false;
			}
		}
	};

	private MusicPopWindowCallback mMusicPopWinCallback = new MusicPopWindowCallback() {
		@Override
		public void setCurrentPlayedIndex(int pos) {
			mPlayIndexChange = pos;
			Track track = mPlayList.getTrack(mPlayIndexChange);
			updateMusicStatus(track.getTitle() + " - " + track.getArtist());
			if (mMusicService != null) {
				mMusicService.setChooseItemToPlay(mPlayIndexChange);
			}
		}

		@Override
		public void prev() {
			if (mMusicService != null) {
				mMusicService.prev();
			}
		}

		@Override
		public void play() {
			if (mMusicService != null) {
				if (mMusicService.isPlaying()) {
					mMusicService.pause();
				} else {
					mMusicService.play();
				}
			}
		}

		@Override
		public void pause() {
			if (mMusicService != null) {
				mMusicService.play();
			}
		}

		@Override
		public void stop() {
			if (mMusicService != null) {
				mMusicService.stop();
			}
		}

	};

	private void updateMusicStatus(String song) {
		String getString = song;
		String splitStr = " - ";
		String str[] = getString.split(splitStr);
		if (str != null && str.length >= 2) {
			mTextViewSong.setText(str[0]);
			mTextViewMusicHeaderArtist.setText(str[1]);
		} else {
			mTextViewSong.setText(str[0]);
		}
	}

	private ServiceConnection mMusicConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MusicBinder binder = (MusicBinder) service;
			mMusicService = binder.getService();
			boolean isPlaying = mMusicService.isPlaying();
			if (isPlaying) {
				mPlayList = mMusicService.getPlayList();
				if (mPlayList != null) {
					mMusicService.setPlayList(mPlayList);
				}

				Track track = mMusicService.getCurrentPlayTrack();
				if (track != null) {
					mTextViewSong.setText(track.getTitle());
				}
			} else {
				if (mPlayList != null) {
					mMusicService.setPlayList(mPlayList);
				}
				mMusicService.startPlay();
			}

		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mMusicService = null;
		}
	};

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtil.d(TAG, intent.toString());
			String action = intent.getAction();
			if (MSG_VIRTUAL_KEY_BACK.equals(action)) {
				if (!mSessionManager.getPopupWindowIsShowing()
					|| (mMusicPopupWindow != null && mMusicPopupWindow.isShowing())) {
					onKey(null, KeyEvent.KEYCODE_BACK, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
					onKey(null, KeyEvent.KEYCODE_BACK, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
				} else {
					mSessionManager.hidePopup();
				}
			} else if (MSG_VIRTUAL_KEY_CENTER.equals(action)) {
				if (!mSessionManager.getPopupWindowIsShowing()) {
					onKey(null, KeyEvent.KEYCODE_DPAD_CENTER, new KeyEvent(
						KeyEvent.ACTION_DOWN,
						KeyEvent.KEYCODE_DPAD_CENTER));
					onKey(null, KeyEvent.KEYCODE_DPAD_CENTER, new KeyEvent(
						KeyEvent.ACTION_UP,
						KeyEvent.KEYCODE_DPAD_CENTER));

				}
			} else if (MSG_VIRTUAL_KEY_LEFT.equals(action)) {
				if (!mSessionManager.getPopupWindowIsShowing()) {
					onKey(null, KeyEvent.KEYCODE_DPAD_LEFT, new KeyEvent(
						KeyEvent.ACTION_DOWN,
						KeyEvent.KEYCODE_DPAD_LEFT));
					onKey(
						null,
						KeyEvent.KEYCODE_DPAD_LEFT,
						new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_LEFT));
				}
			} else if (MSG_VIRTUAL_KEY_RIGHT.equals(action)) {
				if (!mSessionManager.getPopupWindowIsShowing()) {
					onKey(null, KeyEvent.KEYCODE_DPAD_RIGHT, new KeyEvent(
						KeyEvent.ACTION_DOWN,
						KeyEvent.KEYCODE_DPAD_RIGHT));
					onKey(null, KeyEvent.KEYCODE_DPAD_RIGHT, new KeyEvent(
						KeyEvent.ACTION_UP,
						KeyEvent.KEYCODE_DPAD_RIGHT));
				}
			} else if (MSG_VIRTUAL_KEY_UP.equals(action)) {
				if (!mSessionManager.getPopupWindowIsShowing()) {
					onKey(null, KeyEvent.KEYCODE_DPAD_UP, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_UP));
					onKey(null, KeyEvent.KEYCODE_DPAD_UP, new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_UP));

				}
			} else if (MSG_VIRTUAL_KEY_DOWN.equals(action)) {
				if (!mSessionManager.getPopupWindowIsShowing()) {
					onKey(null, KeyEvent.KEYCODE_DPAD_DOWN, new KeyEvent(
						KeyEvent.ACTION_DOWN,
						KeyEvent.KEYCODE_DPAD_DOWN));
					onKey(
						null,
						KeyEvent.KEYCODE_DPAD_DOWN,
						new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DPAD_DOWN));
				}
			} else if (MusicPlaybackService.EVENT_MUSIC_ONBUFF.equals(action)) {
				// 音乐缓冲
				LogUtil.d(TAG, "Music buff...");
				mTextViewProgress.setText("--:--/--:--");
				// mIsMusicPlaying = true;
				if (mMusicPopupWindow != null) {
					mMusicPopupWindow.onMusicBuff();
				}
			} else if (MusicPlaybackService.EVENT_MUSIC_ONPLAY.equals(action)) {
				// 音乐播放
				LogUtil.d(TAG, "Music play");
				isMusicPlaying = true;
				mSessionManager.stopTTS();
				// mIsMusicPlaying = true;
				if (mMusicPopupWindow != null) {
					mMusicPopupWindow.onMusicPlayed();
				}
				Track track = mPlayList.getTrack(mPlayIndexChange);
				updateMusicStatus(track.getTitle() + " - " + track.getArtist());
				showMessage(SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_PLAY);
			} else if (MusicPlaybackService.EVENT_MUSIC_ONPAUSE.equals(action)) {
				// 音乐暂停
				LogUtil.d(TAG, "Music pause or stop");
				isMusicPlaying = false;
				// mIsMusicPlaying = false;
				if (mMusicPopupWindow != null) {
					mMusicPopupWindow.onMusicPaused();
				}

				showMessage(SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_PAUSE);
			} else if (MusicPlaybackService.EVENT_MUSIC_ONSTOP.equals(action)) {
				// 音乐停止
				LogUtil.d(TAG, "Music stop");
				isMusicShowing = false;
				// mIsMusicPlaying = false;
				if (mServiceBinderFlag) {
					unbindService(mMusicConnection);
					mServiceBinderFlag = false;
					mMusicService = null;
				}

				if (mMusicPopupWindow != null) {
					mMusicPopupWindow.dismiss();
					mMusicPopupWindow.release();
					mMusicPopupWindow = null;
				}

				showMessage(SessionPreferenceOfIntent.INTENT_MUSIC_TO_CONTROL_STOP);
			} else if (MusicPlaybackService.EVENT_MUSIC_ONSELECT.equals(action)) {
				// 选择某项音乐
				Bundle extras = intent.getExtras();
				int index = extras.getInt(MusicPlaybackService.DATA_INT_ONSELECT_INDEX);
				LogUtil.d(TAG, "Music select index:" + index);
				mPlayIndexChange = index;
				if (mPlayList != null && mPlayList.size() > 0) {
					Track track = mPlayList.getTrack(mPlayIndexChange);

					if (track != null) {
						updateMusicStatus("音乐缓冲中...");
						if (mMusicPopupWindow != null) {
							mMusicPopupWindow.onMusicPaused();
							mMusicPopupWindow.onUpdateTrackChanged(index, track);
						}
					}
				}
			} else if (MusicPlaybackService.EVENT_MUSIC_ONERROR.equals(action)) {
				// 音乐异常
				mMusicPause.setVisibility(View.VISIBLE);
				mMusicScale.setVisibility(View.GONE);
				mMusicScale.clearAnimation();
				mMusicPlay.setVisibility(View.GONE);

				Bundle extras = intent.getExtras();
				int error = extras.getInt(MusicPlaybackService.DATA_INT_ONERROR_ERRORCODE);
				LogUtil.d(TAG, "Music error code:" + error);

				if (mMusicPopupWindow != null) {
					mMusicPopupWindow.onUpdateTrackStreamError(error);
				}
			} else if (MusicPlaybackService.EVENT_MUSIC_ONUPDATE_BUFFER.equals(action)) {
				// 音乐缓冲进度
				Bundle extras = intent.getExtras();
				int percent = extras.getInt(MusicPlaybackService.DATA_INT_ONUPDATE_BUFFER_PERCENT);
				LogUtil.d(TAG, "Music buffer percent:" + percent);

				if (mMusicPopupWindow != null) {
					mMusicPopupWindow.onUpdateTrackBuffering(percent);
				}
			} else if (MusicPlaybackService.EVENT_MUSIC_ONUPDATE_PROGRESS.equals(action)) {
				// 音乐播放进度
				Bundle extras = intent.getExtras();
				int time = extras.getInt(MusicPlaybackService.DATA_INT_ONUPDATE_PROGRESS_TIME);
				int duration = extras.getInt(MusicPlaybackService.DATA_INT_ONUPDATE_PROGRESS_DURATION);

				if (mMusicPopupWindow != null) {
					mMusicPopupWindow.onUpdateTrackProgress(time, duration);
				}

				mTextViewProgress.setText(getFromatTimeLength(time) + "/" + getFromatTimeLength(duration));

				if (mPlayIndexChange == -1) {
					// 当前音乐控件需要重新初始化
					if (mMusicService == null) {
						Intent musicIntent = new Intent(WindowService.this, MusicPlaybackService.class);
						bindService(musicIntent, mMusicConnection, Context.BIND_AUTO_CREATE);
						mServiceBinderFlag = true;
						startService(musicIntent);
					}
				}
			} else if (SessionPreferenceOfIntent.INTENT_MUSIC_FROM_CONTROL_PLAY.equals(action)) {
				LogUtil.d(TAG, "INTENT_MUSIC_PLAY");
				if (mMusicService != null) {
					mMusicService.play();
				}
			} else if (SessionPreferenceOfIntent.INTENT_MUSIC_FROM_CONTROL_PAUSE.equals(action)) {
				LogUtil.d(TAG, "INTENT_MUSIC_PAUSE");
				if (mMusicService != null) {
					mMusicService.pause();
				}
			} else if (SessionPreferenceOfIntent.INTENT_MUSIC_FROM_CONTROL_STOP.equals(action)) {
				LogUtil.d(TAG, "INTENT_MUSIC_STOP");
				if (mMusicService != null) {
					mMusicService.stop();
				}
			} else if (SessionPreferenceOfIntent.INTENT_MUSIC_FROM_CONTROL_PREV.equals(action)) {
				LogUtil.d(TAG, "INTENT_MUSIC_PREV");
				if (mMusicService != null) {
					mMusicService.prev();
				}
			} else if (SessionPreferenceOfIntent.INTENT_MUSIC_FROM_CONTROL_NEXT.equals(action)) {
				LogUtil.d(TAG, "INTENT_MUSIC_NEXT");
				if (mMusicService != null) {
					mMusicService.next();
				}
			}

			try {
				abortBroadcast();
			} catch (Exception e) {
			} 
		}
	};

	private String getFromatTimeLength(int timeInSeconds) {
		int minutes = timeInSeconds / 60;
		int seconds = timeInSeconds % 60;
		return String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			int action = msg.what;
			switch (action) {
			case MSG_VOICE_BUTTON_START:
				mSessionManager.startByControlButton();
				//show();
				break;
			case MSG_VOICE_BUTTON_STOP:
				//resetTimer();
				mSessionManager.stopByControlButton();
				break;
			case MSG_AUTO_FINISH:
				LogUtil.d(TAG, "Auto stop service time out!");
				// stopSelf();
				dismiss();
				break;
			case ADD_FLOAT_WINDOW:
				addFloatWindow();
				break;
			case REMOVE_FLOAT_WINDOW:
				removeFloatView();
				break;
			case MIC_STATUS:
				break;
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		LogUtil.d(TAG, "onCreate");
		super.onCreate();
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		mUserPreference = new UserPreference(this);
		mWindowPopupDialog = new WindowPopupDialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		mWindowPopupDialog.setOnKeyListener(this);
		mWindowPopupDialog.setOnDismissListener(this);
		mWindowPopupDialog.setContentView(Res.layout.activity_main);
		mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		findViews();
		mSessionManager = new SessionManager(this, sessionContainer);
		mSessionManager.setSessionListener(mSessionManagerListener);
		registReceiver();
		mSessionManager.onResume();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		windowService = this;

		if (intent != null) {
			String action = intent.getAction();
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				if (bundle.getBoolean(BUNDLE_DATA_FROM_MOBILE_CONTROL)) {
					if (mWindowPopupDialog.isShowing()) {
						dismiss();
					} else {
						show();
					}
				} else {
					for (String key : MessageReceiver.ACTION_START_TALK_ARRAY) {
						if (key.equals(action)) {
							mHandler.removeMessages(MSG_VOICE_BUTTON_STOP);
							mHandler.sendEmptyMessageDelayed(MSG_VOICE_BUTTON_START, TIME_VOICE_BUTTON_START);
							break;
						}
					}
					for (String key : MessageReceiver.ACTION_STOP_TALK_ARRAY) {
						if (key.equals(action)) {
							mHandler.sendEmptyMessageDelayed(MSG_VOICE_BUTTON_STOP, TIME_VOICE_BUTTON_STOP);
							//resetTimer();
							break;
						}
					}
				}
			} else {
				for (String key : MessageReceiver.ACTION_START_TALK_ARRAY) {
					if (key.equals(action)) {
						mHandler.removeMessages(MSG_VOICE_BUTTON_STOP);
						mHandler.sendEmptyMessageDelayed(MSG_VOICE_BUTTON_START, TIME_VOICE_BUTTON_START);
						break;
					}
				}
				for (String key : MessageReceiver.ACTION_STOP_TALK_ARRAY) {
					if (key.equals(action)) {
						mHandler.sendEmptyMessageDelayed(MSG_VOICE_BUTTON_STOP, TIME_VOICE_BUTTON_STOP);
						//resetTimer();
						break;
					}
				}
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	public void showFloat() {
		LogUtil.d(TAG, "showFloat Start");
		cancelTimer();
		if (mMusicService != null && mMusicPopupWindow != null && mMusicPopupWindow.isShowing()) {
			mMusicPopupWindow.dismiss();
		}
		mFloatMusic.setVisibility(View.INVISIBLE);
		sessionContainer.setVisibility(View.GONE);
		mTalkToast.setVisibility(View.INVISIBLE);
		mMainFloat.setVisibility(View.VISIBLE);

		show();
	}

	public void showMainContent() {
		if (mMusicService != null && mMusicPopupWindow != null && mMusicPopupWindow.isShowing()) {
			mMusicPopupWindow.dismiss();
		}
		if (mMusicService != null) {
			mFloatMusic.setVisibility(View.VISIBLE);
			if (mMusicPopupWindow != null) {
				mMusicPopupWindow.onMusicStatus(mMusicService.isPlaying());
			}
			if (mMusicService.isPlaying()) {
				mMusicPause.setVisibility(View.GONE);
				mMusicScale.setVisibility(View.VISIBLE);
//				mMusicScale.startAnimation(music_operatingAnim);
				mMusicPlay.setVisibility(View.VISIBLE);
			} else {
				mMusicScale.clearAnimation();
				mMusicScale.setVisibility(View.GONE);
				mMusicPlay.setVisibility(View.GONE);
				mMusicPause.setVisibility(View.VISIBLE);
			}
		}
//		------------xuhua------------------
//		setRatoteImageGone();
		mMainFloat.setVisibility(View.GONE);
		sessionContainer.setVisibility(View.VISIBLE);
//		sessionContainer.startAnimation(mRecordTxtDownInAnim);

		mTalkToast.setVisibility(View.VISIBLE);
		if (!mWindowPopupDialog.isShowing()) {
			mWindowPopupDialog.show();
		}
		sessionContainer.requestSuperFocus();
		resetTimer();
	}
	
	private final Handler handler = new Handler();  
	Runnable runnable=new Runnable(){
		@Override
		public void run() {
			updateVolume((int)PrivatePreference.mRecordingVoiceVolumn);
			handler.postDelayed(this, 20);
		}
	};
	
	public void show() {
		
		if (!mWindowPopupDialog.isShowing()) {
//			linearLayoutMainView.startAnimation(mRecordRightInAnim);
			mWindowPopupDialog.show();
		}
		
		handler.postDelayed(runnable, 100);
		
		if (mMusicService != null) {
			mFloatMusic.setVisibility(View.VISIBLE);
			if (mMusicPopupWindow != null) {
				mMusicPopupWindow.onMusicStatus(mMusicService.isPlaying());
			}
			if (mMusicService.isPlaying()) {
				mMusicPause.setVisibility(View.GONE);
				mMusicScale.setVisibility(View.VISIBLE);
//				mMusicScale.startAnimation(music_operatingAnim);
				mMusicPlay.setVisibility(View.VISIBLE);
			} else {
				mMusicScale.clearAnimation();
				mMusicScale.setVisibility(View.GONE);
				mMusicPlay.setVisibility(View.GONE);
				mMusicPause.setVisibility(View.VISIBLE);
			}
		}
		
		LogUtil.d(TAG, "showFloat End");
	}

	public void dismiss() {
		LogUtil.d(TAG, "dismiss");
		if (mMusicPopupWindow != null && mMusicPopupWindow.isShowing()) {
			mMusicPopupWindow.dismiss();
		} else {
//			linearLayoutMainView.startAnimation(mRecordRightOutAnim);
			mWindowPopupDialog.dismiss();
		}
	}

	public void runOnThread(Runnable r) {
		if (r != null) {
			mHandler.post(r);
		}
	}

	private void findViews() {
		mWindowPopupDialog.setContentView(Res.layout.activity_main);
		mViewNetWorkStatus = mWindowPopupDialog.findViewById(Res.id.linearLayoutNetWorkStatus);
		mTextViewNetWorkChangeContent = (TextView) mViewNetWorkStatus.findViewById(Res.id.textViewNetWorkSatusContent);

		mFloatMusic = (LinearLayout) mWindowPopupDialog.findViewById(Res.id.float_music);
		mFloatMusic.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mFloatMusic.setVisibility(View.INVISIBLE);
				LogUtil.d(TAG, "onClick");
				if (mMusicPopupWindow != null) {
					int[] location = new int[2];
					sessionContainer.getLocationOnScreen(location);
					mMusicPopupWindow.showAtLocation(sessionContainer, Gravity.NO_GRAVITY, location[0], 0);
					mMusicPopupWindow.setFocusable(true);
				}
			}
		});

		mFloatMusic.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				LogUtil.d(TAG, "onFocusChange hasFocus = " + hasFocus);
				mFloatMusic.setVisibility(View.INVISIBLE);
				if (hasFocus) {
					LogUtil.d(TAG, "onFocusChange");
					if (mMusicPopupWindow != null) {
						int[] location = new int[2];
						sessionContainer.getLocationOnScreen(location);
						mMusicPopupWindow.showAtLocation(sessionContainer, Gravity.NO_GRAVITY, location[0], 0);
						mMusicPopupWindow.setFocusable(true);
					}
				}
			}
		});

		mTextViewSong = (TextView) mWindowPopupDialog.findViewById(Res.id.textViewMusicHeaderSong);
		mTextViewMusicHeaderArtist = (TextView) mWindowPopupDialog.findViewById(Res.id.textViewMusicHeaderArtist);
		mTextViewProgress = (TextView) mWindowPopupDialog.findViewById(Res.id.textViewMusicProgress);
		linearLayoutMainView = (LinearLayout) mWindowPopupDialog.findViewById(Res.id.linearLayoutMainView);
		sessionContainer = (SessionContainer) mWindowPopupDialog.findViewById(Res.id.sessionContainerTalk);
		sessionContainer.setFocusable(true);
		mMainFloat = (LinearLayout) mWindowPopupDialog.findViewById(Res.id.main_float);
		mTalkToast = (LinearLayout) mWindowPopupDialog.findViewById(Res.id.talk_toast);
		voice_start = (LinearLayout) mWindowPopupDialog.findViewById(Res.id.voice_start);

		mTextResul = (TextView) mWindowPopupDialog.findViewById(Res.id.text_result);
		mTextRecord = (TextView) mWindowPopupDialog.findViewById(Res.id.text_record);
		mTextWaiting = (TextView) mWindowPopupDialog.findViewById(Res.id.txt_toast);

		mVoiceBg = (ImageView) mWindowPopupDialog.findViewById(Res.id.voice_bg);
		mVoiceLevel = (ImageView) mWindowPopupDialog.findViewById(Res.id.voice_level);
		mVoiceRing = (ImageView) mWindowPopupDialog.findViewById(Res.id.voice_ring);
		mRatoteImage = (ImageView) mWindowPopupDialog.findViewById(Res.id.rotate_animation);
		mWaitRatoteImage = (ImageView) mWindowPopupDialog.findViewById(Res.id.wait_rotate_animation);
		mVoiceRed = (ImageView) mWindowPopupDialog.findViewById(Res.id.voice_red);

		mMusicScale = (ImageView) mWindowPopupDialog.findViewById(Res.id.ic_music_scale);
		mMusicPlay = (ImageView) mWindowPopupDialog.findViewById(Res.id.ic_music_play);
		mMusicPause = (ImageView) mWindowPopupDialog.findViewById(Res.id.ic_music_pause);

		lin = new LinearInterpolator();
		operatingAnim = AnimationUtils.loadAnimation(this, Res.anim.rotate_sacnner);
		operatingAnim.setInterpolator(lin);

//		music_operatingAnim = AnimationUtils.loadAnimation(this, Res.anim.rotate_sacnner);
//		music_operatingAnim.setInterpolator(lin);

//		wait_operatingAnim = AnimationUtils.loadAnimation(this, Res.anim.rotate_sacnner);
//		wait_operatingAnim.setInterpolator(lin);

//		mRecognitionScaleAnim = AnimationUtils.loadAnimation(this, Res.anim.global_voice_scale_in);
//		mRecognitionScaleAnim.setAnimationListener(animationListener);

//		mRecordRightInAnim = AnimationUtils.loadAnimation(this, Res.anim.global_voice_right_in);
//
//		mRecordRightOutAnim = AnimationUtils.loadAnimation(this, Res.anim.global_voice_right_out);
//
//		mRecordAlphaInAnim = AnimationUtils.loadAnimation(this, Res.anim.global_voice_alpha_in);
//
//		mRecordAlphaOutAnim = AnimationUtils.loadAnimation(this, Res.anim.global_voice_alpha_out);
//
//		mRecordTxtDownInAnim = AnimationUtils.loadAnimation(this, Res.anim.global_voice_down_in);
//
//		mRecordRedScaleInAnim = AnimationUtils.loadAnimation(this, Res.anim.global_red_scale_in);
//		mRecordRedScaleInAnim.setAnimationListener(animationListener);
//
//		mRecordRedScaleOutAnim = AnimationUtils.loadAnimation(this, Res.anim.global_red_scale_out);
//		mRecordRedScaleOutAnim.setAnimationListener(animationListener);
		container = (LinearLayout) mWindowPopupDialog.findViewById(Res.id.talk_single);
	}

	private AnimationListener animationListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
//			if (animation.equals(mRecognitionScaleAnim)) {
//				mVoiceRed.setVisibility(View.VISIBLE);
//				mVoiceRed.startAnimation(mRecordRedScaleInAnim);
//			} else if (animation.equals(mRecordRedScaleInAnim)) {
//				mVoiceRed.setVisibility(View.GONE);
//				mVoiceBg.setVisibility(View.VISIBLE);
//				mVoiceBg.setImageResource(Res.drawable.ic_voice_speaking);
//			} else if (animation.equals(mRecordRedScaleOutAnim)) {
//				mVoiceRed.setVisibility(View.GONE);
//			}
		}
	};

	private void registReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(MSG_VIRTUAL_KEY_BACK);
		filter.addAction(MSG_VIRTUAL_KEY_CENTER);
		filter.addAction(MSG_VIRTUAL_KEY_LEFT);
		filter.addAction(MSG_VIRTUAL_KEY_RIGHT);
		filter.addAction(MSG_VIRTUAL_KEY_UP);
		filter.addAction(MSG_VIRTUAL_KEY_DOWN);

		filter.addAction(MusicPlaybackService.EVENT_MUSIC_ONBUFF);
		filter.addAction(MusicPlaybackService.EVENT_MUSIC_ONPLAY);
		filter.addAction(MusicPlaybackService.EVENT_MUSIC_ONPAUSE);
		filter.addAction(MusicPlaybackService.EVENT_MUSIC_ONSTOP);
		filter.addAction(MusicPlaybackService.EVENT_MUSIC_ONSELECT);
		filter.addAction(MusicPlaybackService.EVENT_MUSIC_ONERROR);
		filter.addAction(MusicPlaybackService.EVENT_MUSIC_ONUPDATE_BUFFER);
		filter.addAction(MusicPlaybackService.EVENT_MUSIC_ONUPDATE_PROGRESS);

		filter.addAction(SessionPreferenceOfIntent.INTENT_MUSIC_FROM_CONTROL_PLAY);
		filter.addAction(SessionPreferenceOfIntent.INTENT_MUSIC_FROM_CONTROL_PAUSE);
		filter.addAction(SessionPreferenceOfIntent.INTENT_MUSIC_FROM_CONTROL_STOP);
		filter.addAction(SessionPreferenceOfIntent.INTENT_MUSIC_FROM_CONTROL_PREV);
		filter.addAction(SessionPreferenceOfIntent.INTENT_MUSIC_FROM_CONTROL_NEXT);

		filter = MessageManager.registPrivateIntentFilter(filter, getApplicationContext());
		registerReceiver(mBroadcastReceiver, filter);
	}

	private void unregistReceiver() {
		try {
			unregisterReceiver(mBroadcastReceiver);
		} catch (Exception e) {
		}
	}

	private void cancelTimer() {
		LogUtil.d(TAG, "cancelTimer");
		mHandler.removeMessages(MSG_AUTO_FINISH);
	}

	private void resetTimer() {
		LogUtil.d(TAG, "resetTimer");
		cancelTimer();
		mHandler.sendEmptyMessageDelayed(MSG_AUTO_FINISH, AUTO_FINISH_DELAY);
	}

	private void updateNetWorkStatus(boolean hasNetwork) {
		LogUtil.d(TAG, "updateNetWorkStatus:hasNetwork " + hasNetwork);
		if (hasNetwork) {
			mViewNetWorkStatus.setVisibility(View.GONE);
		} else {
			mViewNetWorkStatus.setVisibility(View.GONE);
//			mTextViewNetWorkChangeContent.setText("当前网络连接异常，部分功能将无法使用~");
		}
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		// 音乐播放中，不重置Timer
		/*if (canResetTimer()) {
			resetTimer();
		}*/

		View focusView = null;
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			LogUtil.d(TAG, "onKey:event " + event);
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				mSessionManager.stopTTS();
				dismiss();
				return true;
			case KeyEvent.KEYCODE_LEFT_BRACKET:
			case KeyEvent.KEYCODE_DPAD_LEFT:
				focusView = mWindowPopupDialog.getCurrentFocus();
				LogUtil.d(TAG, "onKey:focusView " + focusView);
				if (focusView != null) {
					View nextView = focusView.focusSearch(View.FOCUS_LEFT);
					if (nextView != null) {
						nextView.requestFocus();
						return true;
					} else {
						MessageManager.sendPrivateMessage(getApplicationContext(), TVSearchContentView.MSG_KEY_LEFT);
						return true;
					}
				}
				break;
			case KeyEvent.KEYCODE_RIGHT_BRACKET:
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				focusView = mWindowPopupDialog.getCurrentFocus();
				if (focusView != null) {
					View nextView = focusView.focusSearch(View.FOCUS_RIGHT);
					if (nextView != null) {
						nextView.requestFocus();
						return true;
					} else {
						MessageManager.sendPrivateMessage(getApplicationContext(), TVSearchContentView.MSG_KEY_RIGHT);
						return true;
					}
				}
				break;
			case KeyEvent.KEYCODE_DPAD_UP:
				focusView = mWindowPopupDialog.getCurrentFocus();
				if (focusView != null) {
					View nView = (View) focusView.getParent();
					View nextView = focusView.focusSearch(View.FOCUS_UP);
					LogUtil.d(TAG, "KEYCODE_DPAD_UP:" + nextView + "=====ViewParent: " + nView);
					if (nextView != null) {
						nextView.requestFocus();
						return true;
					} else if (mMusicService != null) {
						mFloatMusic.setFocusable(true);
						mFloatMusic.setFocusableInTouchMode(true);
						mFloatMusic.requestFocus();
						return true;
					}
				} else if (mMusicService != null) {
					mFloatMusic.setFocusable(true);
					mFloatMusic.setFocusableInTouchMode(true);
					mFloatMusic.requestFocus();
					return true;
				}
				break;
			case KeyEvent.KEYCODE_DPAD_DOWN:
				focusView = mWindowPopupDialog.getCurrentFocus();
				if (focusView != null) {
					View nextView = focusView.focusSearch(View.FOCUS_DOWN);
					if (nextView != null) {
						nextView.requestFocus();
						return true;
					} else {
						if (mMusicPopupWindow != null && mMusicPopupWindow.isShowing()) {
							mMusicPopupWindow.dismiss();
						}
					}
				}
				break;
			case KeyEvent.KEYCODE_DPAD_CENTER:
				focusView = mWindowPopupDialog.getCurrentFocus();
				if (focusView != null) {
					focusView.callOnClick();
					return true;
				}
				break;
			}

			if (keyCode == mUserPreference.getInt(UserPreference.VOICE_KEY, -1)) {
				if (!MainActivity.mVoiceKeyFlag) {
					MainActivity.mVoiceKeyFlag = true;
					mHandler.removeMessages(MSG_VOICE_BUTTON_STOP);
					mHandler.sendEmptyMessageDelayed(MSG_VOICE_BUTTON_START, TIME_VOICE_BUTTON_START);
				}
			}
		}
		return false;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		LogUtil.d(TAG, "onDismiss");
		//mSessionManager.stopTTS();
	}

	private boolean canResetTimer() {
		if (PlayerEngine.getInstance().isPlaying()) {
			return false;
		}
		return true;
	}

	private void addFloatWindow() {
		LogUtil.d(TAG, "addFloatWindow:   ");
		floatWindowIsShowing = true;

		mFloatView = mInflater.inflate(Res.layout.float_window, null);

		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		params.format = PixelFormat.RGBA_8888;
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		params.width = LayoutParams.MATCH_PARENT;
		params.height = 100;
		params.gravity = Gravity.CENTER | Gravity.BOTTOM;
		params.x = 0;
		params.y = 0;
		mWindowManager.addView(mFloatView, params);

	}

	private void removeFloatView() {
		if (mFloatView != null) {
			if (floatWindowIsShowing && mFloatView.getParent() == null) {
				return;
			}
			LogUtil.d(TAG, "removeFloatView:   " + floatWindowIsShowing);
			mWindowManager.removeViewImmediate(mFloatView);
			floatWindowIsShowing = false;
			mFloatView = null;
		}
	}

	public void updateVoiceView(VoiceMode mode) {
		Log.d(TAG, "global record sound update view " + mode);
		switch (mode) {
		case MODE_RECORDING:
			setRatoteImageGone();
//			voice_start.startAnimation(mRecognitionScaleAnim);
			mTextResul.setText(Res.string.voice_release);
			String mTextRecordString = KnowledgeMode.getKnowledgeHelpAnswer(this, KnowledgeMode.KNOWLEDGE_FLOAT);
			mTextRecord.setText(mTextRecordString);
			LogUtil.d(TAG, "mTextRecord == " + mTextRecordString);
			mTextRecord.setVisibility(View.INVISIBLE);
			mRatoteImage.setVisibility(View.VISIBLE);
			mRatoteImage.startAnimation(operatingAnim);
//			mTextRecord.startAnimation(mRecordTxtDownInAnim);
			mVoiceRing.setVisibility(View.INVISIBLE);
			linearLayoutMainView.setVisibility(View.VISIBLE);
			mVoiceBg.setVisibility(View.VISIBLE);
			mVoiceBg.setImageResource(Res.drawable.ic_voice_default_current);
			//mVoiceLevel.setVisibility(View.INVISIBLE);
			break;
		case MODE_RECOGNISING:
			setRatoteImageGone();
			//handler.removeCallbacks(runnable);
			mTextResul.setText(Res.string.voice_recognising);
//			mVoiceRed.setVisibility(View.VISIBLE);
//			mVoiceRed.startAnimation(mRecordRedScaleOutAnim);
//			mTextRecord.startAnimation(mRecordAlphaOutAnim);
			mTextRecord.setVisibility(View.INVISIBLE);
			mRatoteImage.setVisibility(View.GONE);
			//mRatoteImage.startAnimation(operatingAnim);
			mVoiceBg.setVisibility(View.INVISIBLE);
			//红色---xuhua
			linearLayoutMainView.setVisibility(View.GONE);
			mVoiceBg.setImageResource(Res.drawable.ic_voice_default_current);
			mVoiceRing.setVisibility(View.GONE);
			mVoiceLevel.setVisibility(View.GONE);
			break;
		case MODE_UNRECOGNISED:
			mVoiceRing.setVisibility(View.INVISIBLE);
			mVoiceBg.setImageResource(Res.drawable.ic_voice_default);
			setRatoteImageGone();
			mVoiceLevel.setVisibility(View.GONE);
			break;
		case MODE_NO_VOICE:
			mVoiceRing.setVisibility(View.INVISIBLE);
			mVoiceBg.setImageResource(Res.drawable.ic_voice_default);
			mVoiceLevel.setVisibility(View.GONE);
			break;
		case MODE_RECORDINGINIT:
			setRatoteImageGone();
			if (mMusicService != null) {
				mMusicService.pause();
			}
			mTextResul.setText(Res.string.voice_recording_init);
//			mTextResul.startAnimation(mRecordAlphaInAnim);
			mTextRecord.setVisibility(View.INVISIBLE);
			mVoiceRing.setVisibility(View.INVISIBLE);
			mVoiceBg.setVisibility(View.VISIBLE);
			//绿色---xuhua
//			linearLayoutMainView.setBackgroundColor(Color.GREEN);
			mVoiceBg.setImageResource(Res.drawable.ic_voice_default);
//			mVoiceBg.startAnimation(operatingAnim);
			mVoiceLevel.setVisibility(View.GONE);
			break;
		case MODE_UNAVILIABLE:
			break;
		case MODE_DEFAULT:
			setRatoteImageGone();
			mTextResul.setText(Res.string.voice_release);
			mVoiceRing.setVisibility(View.INVISIBLE);
			mVoiceBg.setVisibility(View.VISIBLE);
			mVoiceBg.setImageResource(Res.drawable.ic_voice_speakable);
			mVoiceLevel.setVisibility(View.GONE);
			break;
		case MODE_PROTOCAL:
			mVoiceRing.setVisibility(View.INVISIBLE);
			mVoiceBg.setVisibility(View.VISIBLE);
			mVoiceBg.setImageResource(Res.drawable.ic_voice_prasing);
			mRatoteImage.clearAnimation();
			mRatoteImage.setVisibility(View.GONE);
			mVoiceLevel.setVisibility(View.GONE);
			break;
		case MODE_RECORDINGSHORT:
			voice_start.clearAnimation();
			mVoiceRing.setVisibility(View.INVISIBLE);
			mVoiceBg.setVisibility(View.INVISIBLE);
			mRatoteImage.clearAnimation();
			mRatoteImage.setVisibility(View.GONE);
			mVoiceLevel.setVisibility(View.GONE);
			break;
		}
	}

	private void setRatoteImageGone() {
		sessionContainer.clearAnimation();
		voice_start.clearAnimation();
		mRatoteImage.clearAnimation();
//		mWaitRatoteImage.clearAnimation();
		mVoiceBg.clearAnimation();
		mVoiceRed.clearAnimation();
		mTextResul.clearAnimation();
		mTextRecord.clearAnimation();
//		mWaitRatoteImage.setVisibility(View.INVISIBLE);
		//-----------------------------
//		linearLayoutMainView.setVisibility(View.GONE);
		mRatoteImage.setVisibility(View.GONE);
		mVoiceBg.setVisibility(View.GONE);
		mVoiceRed.setVisibility(View.GONE);
		mTextWaiting.setText(Res.string.voice_waiting);
	}

	public void showWaiting(String waitingTitle) {
//		mWaitRatoteImage.setVisibility(View.VISIBLE);
//		mWaitRatoteImage.startAnimation(wait_operatingAnim);
		mTextWaiting.setText(waitingTitle);
	}

	public void updateVolume(int volume) {
		mVolume = volume;
		if (mVolume < 10) {
			setVoiceLevel(1);
		} else if (mVolume < 20) {
			setVoiceLevel(2);
		} else if (mVolume < 30) {
			setVoiceLevel(3);
		} else if (mVolume < 40) {
			setVoiceLevel(4);
		} else if (mVolume < 50) {
			setVoiceLevel(5);
		} else if (mVolume < 60) {
			setVoiceLevel(6);
		} else if (mVolume < 70) {
			setVoiceLevel(7);
		} else if (mVolume < 80) {
			setVoiceLevel(8);
		} else if (mVolume < 90) {
			setVoiceLevel(9);
		} else {
			setVoiceLevel(10);
		}
	}

	public void onMoveUp() {
		LogUtil.d(TAG, "onMoveUp");
		mTextResul.setText(Res.string.voice_moveup);
	}

	public void onMoveDown() {
		LogUtil.d(TAG, "onMoveDown");
		mTextResul.setText(Res.string.voice_release);
	}

	public void onErrorSingleTalk(String host, String talkStatus) {
		LogUtil.d(TAG, "onErrorSingleTalk host:" + host + ";talkStatus:" + talkStatus + ";container.getChildCount() :"
						+ container.getChildCount());
		if (talkStatus.equals("PROTOCAL_START_TALK_OLD_VERSION") || talkStatus.equals("PROTOCAL_START_TALK")) {
			nottalkingButton = new Button(this);
			nottalkingButton.setBackgroundResource(Res.drawable.talk_disable);
			nottalkingButton.setTag(host);
		} else if (talkStatus.equals("PROTOCAL_STOP_RECORDING") || talkStatus.equals("PROTOCAL_CANCEL_TALK")) {
			if (container != null && container.findViewWithTag(host) != null) {
				// container.removeView(container.findViewWithTag(host));
			}
		}
	}

	public void setVoiceLevel(int level) {
		mVoiceLevel.getDrawable().setLevel(level);
	}

	@Override
	public void onDestroy() {
		LogUtil.d(TAG, "onDestroy");
		super.onDestroy();
		isMicListener = false;
		mSessionManager.onPause();
		mWindowPopupDialog.setOnKeyListener(null);
		mWindowPopupDialog.dismiss();
		unregistReceiver();
		mSessionManagerListener = null;
		mSessionManager.onDestroy();
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		mHandler.removeMessages(MSG_VOICE_BUTTON_START);
		mHandler.removeMessages(MSG_VOICE_BUTTON_STOP);
		mHandler.removeMessages(MSG_AUTO_FINISH);
		mHandler.removeMessages(ADD_FLOAT_WINDOW);
		mHandler.removeMessages(REMOVE_FLOAT_WINDOW);
		mHandler = null;
	}

	private void showMessage(String message) {
		MessageManager.sendPrivateMessage(getApplicationContext(), message);
	}
}
