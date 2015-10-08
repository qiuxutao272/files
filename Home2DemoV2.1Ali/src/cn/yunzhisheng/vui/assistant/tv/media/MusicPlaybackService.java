/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : MusicPlaybackService.java
 * @ProjectName : iShuoShuo2_work
 * @PakageName : cn.yunzhisheng.ishuoshuo.media
 * @Author : CavanShi
 * @CreateDate : 2013-3-27
 */
package cn.yunzhisheng.vui.assistant.tv.media;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import cn.yunzhisheng.vui.assistant.preference.UserPreference;
import cn.yunzhisheng.vui.assistant.util.MessageManager;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : CavanShi
 * @CreateDate : 2013-3-27
 * @ModifiedBy : CavanShi
 * @ModifiedDate: 2013-3-27
 * @Modified:
 * 2013-3-27: 实现基本功能
 */
public class MusicPlaybackService extends Service {
	public static final String TAG = "MusicPlaybackService";
	
	public static final String EVENT_MUSIC_ONBUFF = "EVENT_MUSIC_ONBUFF";
	public static final String EVENT_MUSIC_ONPLAY = "EVENT_MUSIC_ONPLAY";
	public static final String EVENT_MUSIC_ONPAUSE = "EVENT_MUSIC_ONPAUSE";
	public static final String EVENT_MUSIC_ONSTOP = "EVENT_MUSIC_ONSTOP";
	public static final String EVENT_MUSIC_ONSELECT = "EVENT_MUSIC_ONSELECT";
	public static final String EVENT_MUSIC_ONERROR = "EVENT_MUSIC_ONERROR";
	public static final String EVENT_MUSIC_ONUPDATE_BUFFER = "EVENT_MUSIC_ONUPDATE_BUFFER";
	public static final String EVENT_MUSIC_ONUPDATE_PROGRESS = "EVENT_MUSIC_ONUPDATE_PROGRESS";

	public static final String DATA_INT_ONSELECT_INDEX = "DATA_INT_ONSELECT_INDEX";
	public static final String DATA_INT_ONERROR_ERRORCODE = "DATA_INT_ONERROR_ERRORCODE";
	public static final String DATA_INT_ONUPDATE_BUFFER_PERCENT = "DATA_INT_ONUPDATE_BUFFER_PERCENT";
	public static final String DATA_INT_ONUPDATE_PROGRESS_TIME = "DATA_INT_ONUPDATE_PROGRESS_TIME";
	public static final String DATA_INT_ONUPDATE_PROGRESS_DURATION = "DATA_INT_ONUPDATE_PROGRESS_DURATION";
	
	private static final int MSG_ACTION_PLAY_OR_PAUSE = 0x01;
	private static final int MSG_ACTION_PREV = 0x02;
	private static final int MSG_ACTION_NEXT = 0x03;
	private static final int MSG_ACTION_SELECT = 0x04;
	private static final int MSG_ACTION_STOP = 0x05;
	private static final int MSG_ACTION_PAUSE = 0x06;

	public static final int IDLE = 0x01;
	public static final int BUFF = 0x02;
	public static final int PLAYING = 0x03;
	public static final int PAUSE = 0x04;
	public static final int STOP = 0x05;
	public static final int ERROR = 0x06;

	private int mServiceStatus = IDLE;
	private int mCurrentTime = 0;
	private int mCurrentBuffPercent = 0;
	private int mErrorCode = 0;

	private IBinder mLocalBinder = new MusicBinder();

	private MusicInterrupter mMusicInterrupter;
	private PlayerEngine mPlayEngine;

	private UserPreference mPreferenceAction;
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_ACTION_PLAY_OR_PAUSE:
				mPlayEngine.play();
				break;
			case MSG_ACTION_PAUSE:
				mPlayEngine.pause();
				break;
			case MSG_ACTION_PREV:
				mPlayEngine.prev();
				break;
			case MSG_ACTION_NEXT:
				mPlayEngine.next();
				break;
			case MSG_ACTION_SELECT:
				int position = (Integer) (msg.obj);
		        if (position == mPlayEngine.getPlaylist().getSelectedIndex()) {
		            return;
		        }
		        mPlayEngine.skipTo(position);
				break;
			case MSG_ACTION_STOP:
				if (mPlayEngine != null) {
					mPlayEngine.stop();
				}
				break;
			}
		};
	};

	public void onCreate() {
		mPlayEngine =PlayerEngine.getInstance();  
		Log.i("Test", "MusicPlaybackService onCreate() isPlaying:" + mPlayEngine.isPlaying());
		if (mMusicInterrupter == null) {
			mMusicInterrupter = new MusicInterrupter(MusicPlaybackService.this);
		}
		mMusicInterrupter.start();
		mPlayEngine.registerListener(mPlayerEngineListener);
		mPreferenceAction = new UserPreference(this);
	}

	private PlayerEngineListener mPlayerEngineListener = new PlayerEngineListener() {
		@Override
		public void onTrackStreamError(int what) {
			mErrorCode = what;
			mServiceStatus = ERROR;
			mPreferenceAction.putBoolean("IS_MUSIC_PLAYING", false);
			Bundle extras = new Bundle();
			extras.putInt(DATA_INT_ONERROR_ERRORCODE, what);
			showMessage(EVENT_MUSIC_ONERROR, extras);
		}

		@Override
		public void onTrackStop() {
			mServiceStatus = STOP;
			showMessage(EVENT_MUSIC_ONSTOP);
		}

		@Override
		public void onTrackStart() {
			mServiceStatus = PLAYING;
			showMessage(EVENT_MUSIC_ONPLAY);
		}

		@Override
		public void onTrackPause() {
			mServiceStatus = PAUSE;
			showMessage(EVENT_MUSIC_ONPAUSE);
		}

		@Override
		public void onTrackChanged(int index, Track track) {
			mServiceStatus = BUFF;
			Bundle extras = new Bundle();
			extras.putInt(DATA_INT_ONSELECT_INDEX, index);
			showMessage(EVENT_MUSIC_ONSELECT, extras);
			showMessage(EVENT_MUSIC_ONBUFF);
		}

		@Override
		public void onTrackBuffering(int percent) {
			mCurrentBuffPercent = percent;
			Bundle extras = new Bundle();
			extras.putInt(DATA_INT_ONUPDATE_BUFFER_PERCENT, percent);
			if(isPlaying() == true){
				showMessage(EVENT_MUSIC_ONUPDATE_BUFFER, extras);
			}
		}

		@Override
		public void onTrackProgress(int seconds, int duration) {
			mCurrentTime = seconds;
			Bundle extras = new Bundle();
			extras.putInt(DATA_INT_ONUPDATE_PROGRESS_TIME, seconds);
			extras.putInt(DATA_INT_ONUPDATE_PROGRESS_DURATION, duration);
			if(isPlaying() == true){
				showMessage(EVENT_MUSIC_ONUPDATE_PROGRESS, extras);
			}
		}
	};

	public boolean isPlaying() {
		if (mPlayEngine != null) {
			return mPlayEngine.isPlaying();
		}
		return false;
	}

	public int getMusicServiceStatus() {
		return mServiceStatus;
	}

	public int getCurrentTime() {
		return mCurrentTime;
	}

	public int getCurrentIndex() {
		return mPlayEngine.getPlaylist().getSelectedIndex();
	}

	public Track getCurrentTrack() {
		return mPlayEngine.getCurrentPlayTrack();
	}

	public int getCurrentTrackDuration() {
		if (mPlayEngine.getCurrentPlayTrack() != null) {
			return mPlayEngine.getCurrentPlayTrack().getDuration();
		}
		return 0;
	}

	public int getCurrentBuffPrecent() {
		return mCurrentBuffPercent;
	}

	public int getErrorCode() {
		return mErrorCode;
	}

	public void prev() {
		mHandler.sendEmptyMessage(MSG_ACTION_PREV);
	}

	public void play() {
		mHandler.sendEmptyMessage(MSG_ACTION_PLAY_OR_PAUSE);
	}
	
	public void pause() {
		mHandler.sendEmptyMessage(MSG_ACTION_PAUSE);
	}

	public void stop() {
		mHandler.sendEmptyMessage(MSG_ACTION_STOP);
	}

	public void next() {
		mHandler.sendEmptyMessage(MSG_ACTION_NEXT);
	}

	public void onDestory() {
		Log.i(TAG, "MediaPlaybackService..onDestory");

		if (mPlayEngine != null) {
			mPlayEngine.unregisterListener(mPlayerEngineListener);
			mPlayEngine = null;
		}

		mMusicInterrupter.stop();
		mMusicInterrupter = null;
		//hideNotification();
		stopSelf();
	}

	public void startPlay() {
		if (mMusicInterrupter == null) {
			mMusicInterrupter = new MusicInterrupter(MusicPlaybackService.this);
		}
		mMusicInterrupter.start();
		
		if (mPlayEngine == null) {
			mPlayEngine = PlayerEngine.getInstance();
		}
		mPlayEngine.registerListener(mPlayerEngineListener);

		if (mPlayEngine.getPlaylist() != null && mPlayEngine.getPlaylist().size() > 0) {
			int index = mPlayEngine.getPlaylist().getSelectedIndex();
			mPlayEngine.getPlaylist().select(index);
			mPlayEngine.play();
		} else {
		}
	}

	public void setChooseItemToPlay(int position) {
		Message msg = new Message();
		msg.what = MSG_ACTION_SELECT;
		msg.obj = position;
		mHandler.sendMessage(msg);
	}

	public Track getCurrentPlayTrack() {
		return mPlayEngine.getCurrentPlayTrack();
	}

	@Override
	public IBinder onBind(Intent intent) {
//		LogUtil.iLog("Test", "service onBinder");

		if (mPlayEngine == null) {
			mPlayEngine = PlayerEngine.getInstance();
		}
		Log.i("Test", "MusicPlaybackService onCreate() isPlaying:" + mPlayEngine.isPlaying());
		if (mMusicInterrupter == null) {
			mMusicInterrupter = new MusicInterrupter(MusicPlaybackService.this);
		}
		mMusicInterrupter.start();
		mPlayEngine.registerListener(mPlayerEngineListener);

		return mLocalBinder;
	}

	public boolean onUnbind(Intent intent) {
//		LogUtil.iLog("Test", "service onUnbind");
		return true;
	}

	public int onStartCommond(Intent intent, int flags, int startId) {
//		LogUtil.iLog("Test", "onStartCommond");
		return START_STICKY;
	}

	public void onRebind(Intent intent) {
//		LogUtil.iLog("Test", "service onRebind");
	}

	public void setPlayList(PlayList list) {
		if (mPlayEngine != null) {
			mPlayEngine.openPlaylist(list);
		}
	}

	public PlayList getPlayList() {
		if (mPlayEngine != null) {
			return mPlayEngine.getPlaylist();
		}
		return null;
	}

	public class MusicBinder extends Binder {
		public MusicPlaybackService getService() {
			return MusicPlaybackService.this;
		}
	}

	private void showMessage(String message, Bundle extras) {
		MessageManager.sendPrivateMessage(getApplicationContext(), message, extras);
	}

	private void showMessage(String message) {
		MessageManager.sendPrivateMessage(getApplicationContext(), message);
	}
}
