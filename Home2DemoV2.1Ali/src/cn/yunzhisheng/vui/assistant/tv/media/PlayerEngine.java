/*
 * Copyright (C) 2009 Teleca Poland Sp. z o.o. <android@teleca.com>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.yunzhisheng.vui.assistant.tv.media;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import cn.yunzhisheng.vui.assistant.tv.media.Track;
import cn.yunzhisheng.vui.assistant.tv.media.PlayList.PlaylistPlaybackMode;
import cn.yunzhisheng.common.util.LogUtil;

public class PlayerEngine {
	private static final String TAG = "PlayerEngine";
	public static final int MUSIC_ERROR_URL_EMPTY = -2;

	private static final long FAIL_TIME_FRAME = 1000;
	private static final int ACCEPTABLE_FAIL_NUMBER = 2;
	private static final int MSG_UPDATE_PROGRESS = 10000;
	private static final int MSG_UPDATE_BUFFER_PROGRESS = 10001;

	private static PlayerEngine sInstance = null;
	private long mLastFailTime;
	private long mTimesFailed;

	private InternalMediaPlayer mCurrentMediaPlayer;
	private PlayList mPlaylist;

	private List<PlayerEngineListener> mListeners = new ArrayList<PlayerEngineListener>();
	private ReentrantReadWriteLock mListenerLock = new ReentrantReadWriteLock();
	private final Lock mListenerReadLock = mListenerLock.readLock();
	private final Lock mListenerWriteLock = mListenerLock.writeLock();

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			LogUtil.d(TAG, msg.toString());
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_UPDATE_PROGRESS:
				if (isPlaying()) {
					int seconds = mCurrentMediaPlayer.getCurrentPosition() / 1000;
					int duration = mCurrentMediaPlayer.getDuration() / 1000;
					try {
						mListenerReadLock.lock();
						for (PlayerEngineListener listener : mListeners) {
							listener.onTrackProgress(seconds, duration);
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						mListenerReadLock.unlock();
					}

					sendProgressMessage(1000);
				}
				break;
			case MSG_UPDATE_BUFFER_PROGRESS:
				try {
					mListenerReadLock.lock();
					for (PlayerEngineListener listener : mListeners) {
						listener.onTrackBuffering(msg.arg1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					mListenerReadLock.unlock();
				}
				break;
			}
		}
	};

	public synchronized static PlayerEngine getInstance() {
		if (sInstance == null) {
			sInstance = new PlayerEngine();
		}

		return sInstance;
	}

	private PlayerEngine() {
		mLastFailTime = 0;
		mTimesFailed = 0;
	}
	
	
	public Track getCurrentPlayTrack() {
		if(mCurrentMediaPlayer != null) {
			return mCurrentMediaPlayer.track;
		}
		Log.i("Test", "getCurrentPlayTrack the track is null");
		return null;
	}

	public void release() {
		cleanUp();
		try {
			mListenerWriteLock.lock();
			mListeners.clear();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mListenerWriteLock.unlock();
		}

	}

	public void registerListener(PlayerEngineListener remoteEngineListener) {
		try {
			mListenerWriteLock.lock();
			if (remoteEngineListener != null
					&& !mListeners.contains(remoteEngineListener)) {
				mListeners.add(remoteEngineListener);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mListenerWriteLock.unlock();
		}
	}

	public void unregisterListener(PlayerEngineListener remoteEngineListener) {
		try {
			mListenerWriteLock.lock();
			mListeners.remove(remoteEngineListener);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mListenerWriteLock.unlock();
		}
	}

	public void next() {
		if (mPlaylist != null) {
			mPlaylist.selectNext();
			play();
		}
	}

	public void openPlaylist(PlayList playlist) {
		if (!playlist.isEmpty()) {
			mPlaylist = playlist;
		} else {
			mPlaylist = null;
		}
	}

	public static void releaseIfPlaying() {
		if (sInstance != null) {
			sInstance.pause();
			sInstance.stop();
		}
	}

	public void pause() {
		if (mCurrentMediaPlayer == null) {
			return;
		}

		if (mCurrentMediaPlayer.preparing) {
			mCurrentMediaPlayer.playAfterPrepare = false;
			return;
		}

		if (mCurrentMediaPlayer.isPlaying()) {
			mCurrentMediaPlayer.pause();
			ArrayList<PlayerEngineListener> tempList = new ArrayList<PlayerEngineListener>(
					mListeners);

			for (PlayerEngineListener listener : tempList) {
				listener.onTrackPause();
			}
		}
	}

	public void play() {
		Log.d(TAG, "play");

		startSteaming();

		try {
			mListenerReadLock.lock();
			for (PlayerEngineListener listener : mListeners) {
				listener.onTrackStart();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mListenerReadLock.unlock();
		}
	}

	private void startSteaming() {
		if (mCurrentMediaPlayer == null) {
			buildMediaPlayer(mPlaylist.getSelectedTrack());
		} else {
			if (mCurrentMediaPlayer.preparing) {
				mCurrentMediaPlayer.playAfterPrepare = true;
				return;
			}

			if (mCurrentMediaPlayer.track != mPlaylist.getSelectedTrack()) {
				mCurrentMediaPlayer.stop();
				mCurrentMediaPlayer.release();
				mCurrentMediaPlayer = null;

				buildMediaPlayer(mPlaylist.getSelectedTrack());
			} else {
				mCurrentMediaPlayer.start();
			}
		}
	}

	public void prev() {
		if (mPlaylist != null) {
			mPlaylist.selectPrev();
			play();
		}
	}

	public void skipTo(int index) {
		mPlaylist.select(index);
		play();
	}

	public void stop() {
		LogUtil.d(TAG, "stop");
		cleanUp();
		try {
			mListenerReadLock.lock();
			for (PlayerEngineListener listener : mListeners) {
				listener.onTrackStop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mListenerReadLock.unlock();
		}
	}

	private void cleanUp() {
		Log.d(TAG, "cleanUp");
		if (mCurrentMediaPlayer != null) {
			try {
				mCurrentMediaPlayer.stop();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} finally {
				mCurrentMediaPlayer.release();
				mCurrentMediaPlayer = null;
			}
		}

	}

	private void buildMediaPlayer(Track track) {
		Log.d(TAG, "build");
		mCurrentMediaPlayer = new InternalMediaPlayer();
		String streamUrl = track.getUrl();

		try {
			mCurrentMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mCurrentMediaPlayer.setDataSource(streamUrl);
			mCurrentMediaPlayer.track = track;

			mCurrentMediaPlayer
					.setOnCompletionListener(new OnCompletionListener() {

						public void onCompletion(MediaPlayer mp) {
							if (!mPlaylist.isLastTrackOnList()
									|| mPlaylist.getPlaylistPlaybackMode() == PlaylistPlaybackMode.REPEAT
									|| mPlaylist.getPlaylistPlaybackMode() == PlaylistPlaybackMode.SHUFFLE_AND_REPEAT) {
								next();
							} else {
								stop();
							}
						}
					});
			mCurrentMediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				public void onPrepared(MediaPlayer mp) {
					Log.d(TAG, "onPrepared");
					mCurrentMediaPlayer.preparing = false;

					if (mCurrentMediaPlayer.playAfterPrepare) {
						mCurrentMediaPlayer.playAfterPrepare = false;

						if (mCurrentMediaPlayer.track != mPlaylist
								.getSelectedTrack()) {
							mCurrentMediaPlayer.stop();
							mCurrentMediaPlayer.release();
							mCurrentMediaPlayer = null;

							play();
						} else {
							mCurrentMediaPlayer.start();
						}
					}
				}
			});

			mCurrentMediaPlayer
					.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

						public void onBufferingUpdate(MediaPlayer mp,
								int percent) {
							sendBufferMessage(percent);
						}
					});

			mCurrentMediaPlayer.setOnErrorListener(new OnErrorListener() {

				public boolean onError(MediaPlayer mp, int what, int extra) {
					Log.w(TAG, "PlayerEngineImpl fail, what (" + what
							+ ") extra (" + extra + ")");

					if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
						for (PlayerEngineListener listener : mListeners) {
							listener.onTrackStreamError(what);
						}
						stop();
						return true;
					}

					if (what == -1) {
						long failTime = System.currentTimeMillis();
						if (failTime - mLastFailTime > FAIL_TIME_FRAME) {
							mTimesFailed = 1;
							mLastFailTime = failTime;
							Log.w(TAG, "PlayerEngineImpl " + mTimesFailed
									+ " fail within FAIL_TIME_FRAME");
						} else {
							mTimesFailed++;
							if (mTimesFailed > ACCEPTABLE_FAIL_NUMBER) {
								Log.w(TAG,
										"PlayerEngineImpl too many fails, aborting playback");
								for (PlayerEngineListener listener : mListeners) {
									listener.onTrackStreamError(what);
								}
								stop();
								return true;
							}
						}
					}
					return false;
				}
			});

			mCurrentMediaPlayer.preparing = true;
			mCurrentMediaPlayer.playAfterPrepare = true;
			mCurrentMediaPlayer.prepareAsync();
			Log.d(TAG, "preparing");

			try {
				mListenerReadLock.lock();
				for (PlayerEngineListener listener : mListeners) {
					listener.onTrackChanged(mPlaylist.getSelectedIndex(),
							mPlaylist.getSelectedTrack());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				mListenerReadLock.unlock();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public PlayList getPlaylist() {
		return mPlaylist;
	}

	public boolean isPlaying() {
		LogUtil.d(TAG, "isPlaying: current thread "
				+ Thread.currentThread().getId());

		if (mCurrentMediaPlayer == null) {
			return false;
		}

		if (mCurrentMediaPlayer.preparing) {
			return false;
		}

		return mCurrentMediaPlayer.isPlaying();
	}

	public void setPlaybackMode(PlaylistPlaybackMode aMode) {
		mPlaylist.setPlaylistPlaybackMode(aMode);
	}

	public PlaylistPlaybackMode getPlaybackMode() {
		return mPlaylist.getPlaylistPlaybackMode();
	}

	public void forward(int time) {
		mCurrentMediaPlayer.seekTo(mCurrentMediaPlayer.getCurrentPosition()
				+ time);
	}

	public void rewind(int time) {
		mCurrentMediaPlayer.seekTo(mCurrentMediaPlayer.getCurrentPosition()
				- time);
	}

	public void sendBufferMessage(int percent) {
		try {
			mListenerReadLock.lock();
			if (!mListeners.isEmpty()) {
				Message msg = mHandler
						.obtainMessage(MSG_UPDATE_BUFFER_PROGRESS);
				msg.arg1 = percent;
				mHandler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mListenerReadLock.unlock();
		}
	}

	public void sendProgressMessage(long dely) {
		try {
			mListenerReadLock.lock();
			if (!mListeners.isEmpty()) {
				mHandler.sendEmptyMessageDelayed(MSG_UPDATE_PROGRESS, dely);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			mListenerReadLock.unlock();
		}
	}

	private class InternalMediaPlayer extends MediaPlayer {
		public Track track;
		public boolean preparing = false;
		public boolean playAfterPrepare = true;

		@Override
		public void start() throws IllegalStateException {
			super.start();
			sendProgressMessage(200);
		}

	}
}
