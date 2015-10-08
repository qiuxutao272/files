/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName	: MusicShowSession.java
 * @ProjectName	: vui_assistant
 * @PakageName	: cn.yunzhisheng.ishuoshuo.session
 * @Author		: Dancindream
 * @CreateDate	: 2013-9-3
 */
package cn.yunzhisheng.vui.assistant.tv.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.vui.assistant.tv.media.PlayList;
import cn.yunzhisheng.vui.assistant.tv.media.PlayerEngine;
import cn.yunzhisheng.vui.assistant.tv.media.PlayerEngineListener;
import cn.yunzhisheng.vui.assistant.tv.media.Track;
import cn.yunzhisheng.vui.assistant.tv.view.MusicContentView;
import cn.yunzhisheng.vui.assistant.tv.view.ELinearLayout.OnItemClickListener;

/**
 * @Module		: 隶属模块名
 * @Comments	: 描述
 * @Author		: Dancindream
 * @CreateDate	: 2013-9-3
 * @ModifiedBy	: Dancindream
 * @ModifiedDate: 2013-9-3
 * @Modified: 
 * 2013-9-3: 实现基本功能
 */
public class MusicShowSession extends BaseSession {
	public static final String TAG = "MusicShowSession";
	private PlayList mPlayList = null;
	private MusicContentView mMusicContentView = null;
	private List<Track> list = null;
	private PlayerEngine mEngine;
	/**
	 * @Author		: Dancindream
	 * @CreateDate	: 2013-9-3
	 * @param context
	 * @param sessionManagerHandler
	 */
	
	private PlayerEngineListener mPlayerEngineListener = new PlayerEngineListener() {

		@Override
		public void onTrackStreamError(int what) {
			Log.e(TAG, "music error what=" + what);
			mMusicContentView.setPlayStatus(false);
			//sendResetTimerMessage();
		}

		@Override
		public void onTrackStop() {
			mMusicContentView.setPlayStatus(false);
			mMusicContentView.updateList();
			//sendResetTimerMessage();
		}

		@Override
		public void onTrackStart() {
			mMusicContentView.setPlayStatus(true);
			mMusicContentView.updateList();
			//sendCancelTimerMessage();
		}

		@Override
		public void onTrackProgress(int seconds, int duration) {
			mMusicContentView.setProgress(getFromatTimeLength(seconds) + "/"
					+ getFromatTimeLength(duration));
			int p = (int) (100 * ((float) seconds / duration));
			mMusicContentView.setProgress(p);
		}

		@Override
		public void onTrackPause() {
			mMusicContentView.setPlayStatus(false);
			mMusicContentView.updateList();
			//sendResetTimerMessage();
		}

		@Override
		public void onTrackChanged(int index, Track track) {
			mMusicContentView.setCurrentIndex(index);
			mMusicContentView.setPlayingInfo(track.getTitle(),
					track.getArtist());
			mMusicContentView.setProgress(0);
			mMusicContentView.setSecondaryProgress(0);
			mMusicContentView.setProgress("--:--/--:--");
			mMusicContentView.updateList();
		}

		@Override
		public void onTrackBuffering(int percent) {
			mMusicContentView.setSecondaryProgress(percent);
		}
	};
	
	
	public MusicShowSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
	}
	
	public void putProtocol(JSONObject jsonProtocol) {
		super.putProtocol(jsonProtocol);
		
		JSONObject resultObject = getJSONObject(mDataObject, "result");
		
		JSONArray musicArray = getJsonArray(resultObject, "musicData");
		if (musicArray != null) {
			mPlayList = new PlayList();
			list = new ArrayList<Track>();
			for (int i = 0; i < musicArray.length(); i ++) {
				JSONObject item = getJSONObject(musicArray, i);
				if (item != null) {
					String title = getJsonValue(item, "title", "");
					String artist = getJsonValue(item, "artist", "");
					String album = getJsonValue(item, "album", "");
					String duration = getJsonValue(item, "duration", "0");
					String image = getJsonValue(item, "imageUrl", "");
					String link = getJsonValue(item, "url", "");
					
					Track track = new Track();
					track.setTitle(title);
					track.setArtist(artist);
					track.setImgUrl(image);
					track.setAlbum(album);
					track.setDuration(Integer.parseInt(duration));
					track.setUrl(link);
					mPlayList.addTrack(track);
					list.add(track);
				}
				
			}
		}

		mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_REQUEST_MUSIC_START);
		
		addQuestionViewText(mQuestion);
		addAnswerViewText(mAnswer);
		playTTS(mAnswer);
		
		
		
		//onTaskFinished();
//		mMusicContentView = new MusicContentView(mContext);
//		mMusicContentView.setPlayList(list);
//		addAnswerView(mMusicContentView); 
	}
	
	
	private static String getFromatTimeLength(int timeInSeconds) {
		int minutes = timeInSeconds / 60;
		int seconds = timeInSeconds % 60;
		return String.format("%02d", minutes) + ":"
				+ String.format("%02d", seconds);
	}
	
	public void onTaskFinished() {
		android.util.Log.e("ttt", "onTaskFinished");
		if (mPlayList != null && !mPlayList.isEmpty()) {
			cancelTTS();
			mMusicContentView = new MusicContentView(mContext);
			mMusicContentView.setMaxProgress(100);
			mMusicContentView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClicked(int position, Map<String, ?> data,
						View v) {
					if (position == mEngine.getPlaylist().getSelectedIndex()) {
						if (mEngine.isPlaying()) {
							mEngine.pause();
						} else {
							mEngine.play();
						}
					} else {
						mEngine.getPlaylist().select(position);
						mEngine.play();
					}
				}
			});
			mMusicContentView.setPlayList(mPlayList.getAllTracks()); 
			
			addAnswerView(mMusicContentView);
			
			mEngine = PlayerEngine.getInstance();
			mEngine.registerListener(mPlayerEngineListener);
			mEngine.openPlaylist(mPlayList);
			mEngine.getPlaylist().select(0);
			mEngine.play();
		} else {
			String ttsString = "很抱歉，没有搜索到相关歌曲"; 
			addAnswerViewText(ttsString);
			 
			playTTS(ttsString);
			sendDelayTaskTTSEndMessage();
		}

//		mSessionManagerHandler
//				.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_DONE);
	}
	
	
	
	public PlayList getPlayList() {
		return mPlayList;
	}
}
