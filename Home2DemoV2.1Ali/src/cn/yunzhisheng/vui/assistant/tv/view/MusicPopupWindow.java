/**
 * Copyright (c) 2012-2012 Yunzhisheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : MusicControlView.java
 * @ProjectName : iShuoShuo2
 * @PakageName : cn.yunzhisheng.vui.assistant.view
 * @Author : Brant
 * @CreateDate : 2012-12-27
 */
package cn.yunzhisheng.vui.assistant.tv.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import cn.yunzhisheng.common.util.LogUtil;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.assistant.tv.media.PlayList;
import cn.yunzhisheng.vui.assistant.tv.media.PlayerEngine;
import cn.yunzhisheng.vui.assistant.tv.media.Track;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Brant
 * @CreateDate : 2012-12-27
 * @ModifiedBy : Brant
 * @ModifiedDate: 2012-12-27
 * @Modified:
 * 2012-12-27: 实现基本功能
 */
public class MusicPopupWindow extends PopupWindow implements OnClickListener, OnItemClickListener{
	public static final String TAG = "MusicPopupWindow";

	private TextView mTextViewSong, mTextViewArtist, mTextViewProgress;
	private ProgressBar mProgressBarMusic;
	private ImageView mBtnShowMore, mBtnPlayPause, mBtnClose;
	private ListView mListMusic;
	private MusicListAdapter mAdapter;
	private ArrayList<HashMap<String, Object>> mData;
	private MusicPopWindowCallback mMainContextCallback;
	private AudioManager mAudioManager;

	View mLayoutView;
	private PlayList mMusicPlayList;
	private int mCurrentPlayedPosition;

	public void onUpdateTrackProgress(int seconds, int duration) {
		mTextViewProgress.setText(getFromatTimeLength(seconds) + "/" + getFromatTimeLength(duration));
		mProgressBarMusic.setMax(100);
		int p = (int) (100 * ((float) seconds / duration));
		mProgressBarMusic.setProgress(p);
	}

	public void onUpdateTrackChanged(int index, Track track) {
		mCurrentPlayedPosition = index;
		mMainContextCallback.setCurrentPlayedIndex(mCurrentPlayedPosition);
		mAdapter.setCurrentItem(index);
		mTextViewSong.setText(track.getTitle());
		mTextViewArtist.setText(track.getArtist());
		refreshMusicProgress();
	}

	private void refreshMusicProgress() {
		mProgressBarMusic.setProgress(0);
		mProgressBarMusic.setSecondaryProgress(0);
		mTextViewProgress.setText("--:--/--:--");
	}

	public void onUpdateTrackBuffering(int percent) {
		mProgressBarMusic.setMax(100);
		mProgressBarMusic.setSecondaryProgress(percent);
	}

	public void onUpdateTrackStreamError(int what) {
		onMusicPaused();
		Log.e(TAG, "music error what=" + what);
		if (what == PlayerEngine.MUSIC_ERROR_URL_EMPTY) {
			Toast.makeText(getContentView().getContext(), Res.string.music_url_empty, Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getContentView().getContext(), "该歌曲播放异常", Toast.LENGTH_SHORT).show();
		}
	}

	private MusicPopupWindowListener mListener;
	private Context mContext;
	private Resources res;

	public MusicPopupWindow(Context context) {
		super(context);
		mContext = context;
		res = mContext.getResources();
		setWidth(res.getDimensionPixelSize(Res.dimen.pop_music_width));
		setHeight(LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(new BitmapDrawable());

		mLayoutView = View.inflate(context, Res.layout.music_pop_window, null);
		mLayoutView.setFocusable(true);
		setContentView(mLayoutView);
		findViews();
		setListener();
		setFocusable(true);
		setOutsideTouchable(true);
		mData = new ArrayList<HashMap<String, Object>>();
		mAdapter = new MusicListAdapter(context, mData);
		mListMusic.setAdapter(mAdapter);
		if (Build.VERSION.SDK_INT < 14) {
			mLayoutView.setFocusableInTouchMode(true);
			mLayoutView.setOnKeyListener(new PopWindowKeyListener());
			mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		}
	}

	private void findViews() {
		View contentView = getContentView();

		contentView.setFocusable(true);
		contentView.setFocusableInTouchMode(true);
		contentView.setOnKeyListener((new PopWindowKeyListener()));
		
		mTextViewSong = (TextView) contentView.findViewById(Res.id.textViewSong);
		mTextViewArtist = (TextView) contentView.findViewById(Res.id.textViewArtist);
		mTextViewProgress = (TextView) contentView.findViewById(Res.id.textViewMusicProgress);
		mProgressBarMusic = (ProgressBar) contentView.findViewById(Res.id.progressBarMusic);

		mBtnShowMore = (ImageView) contentView.findViewById(Res.id.btnMusicShowMore);
		mBtnPlayPause = (ImageView) contentView.findViewById(Res.id.btnMusicPlayPause);
		mBtnClose = (ImageView) contentView.findViewById(Res.id.btnMusicClose);
		mListMusic = (ListView) contentView.findViewById(android.R.id.list);
		mListMusic.setFocusable(true);
		mListMusic.setFocusableInTouchMode(true);
		/*mBtnShowMore.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					openMusicList();
				}else{
					closeMusicList();
				}
			}
		});*/
	}

	private void setListener() {
		mListMusic.setOnItemClickListener(this);
		mBtnShowMore.setOnClickListener(this);
		mBtnClose.setOnClickListener(this);
		mBtnPlayPause.setOnClickListener(this);
	}

	public void updateMusicList(PlayList list) {
		mMusicPlayList = list;
		mData.clear();
		if (mMusicPlayList == null) {
			mData.clear();
		} else {
			List<Track> trackList = mMusicPlayList.getAllTracks();
			for (int i = 0; i < trackList.size(); i++) {
				Track track = trackList.get(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("Song", (i + 1) + "." + track.getTitle());
				map.put("Artist", track.getArtist());
				mData.add(map);
			}

			int index = mMusicPlayList.getSelectedIndex();
			Track track = mMusicPlayList.getSelectedTrack();
			if (mAdapter != null) {
				mAdapter.setCurrentItem(index);
			}
			if (mTextViewSong != null) {
				mTextViewSong.setText(track.getTitle());
			}
			if (mTextViewArtist != null) {
				mTextViewArtist.setText(track.getArtist());
			}
		}
		mAdapter.notifyDataSetChanged();
		
		ViewGroup.LayoutParams params = mListMusic.getLayoutParams();
		if(mMusicPlayList != null && mMusicPlayList.size()<5) {
			params.height = (res.getDimensionPixelSize(Res.dimen.function_item_height)+1)*mMusicPlayList.size();
		}else{
			params.height = (res.getDimensionPixelSize(Res.dimen.function_item_height)+1)*5;
		}
		mListMusic.setLayoutParams(params);
	}

	public void setListener(MusicPopupWindowListener listener) {
		mListener = listener;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case Res.id.btnMusicShowMore:
			if(mListMusic.getVisibility() == View.GONE){
				openMusicList();
			}else{
				closeMusicList();
			}
			
			break;
		case Res.id.btnMusicPlayPause:
			if (mMainContextCallback != null) {
				mMainContextCallback.play();
			}
			break;
		case Res.id.btnMusicClose:
			mMainContextCallback.stop();
			break;
		}
	}

	@Override
	public void showAsDropDown(View anchor) {
		super.showAsDropDown(anchor);
		closeMusicList();
		if (mListener != null) {
			mListener.onShow();
		}
	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		mBtnShowMore.requestFocus();
		if (mListener != null) {
			mListener.onShow();
		}
		
		/*ViewGroup.LayoutParams params = mListMusic.getLayoutParams();
		if(mMusicPlayList != null && mMusicPlayList.size()<5) {
			params.height = (res.getDimensionPixelSize(Res.dimen.function_item_height)+1)*mMusicPlayList.size();
		}
		mListMusic.setLayoutParams(params);*/
	}

	@Override
	public void dismiss() {
		super.dismiss();
		if (mListener != null) {
			mListener.onDismiss();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (mMainContextCallback != null) {
			mMainContextCallback.setCurrentPlayedIndex(position);
		}
		closeMusicList();
		mBtnShowMore.requestFocus();
	}

	private String getFromatTimeLength(int timeInSeconds) {
		int minutes = timeInSeconds / 60;
		int seconds = timeInSeconds % 60;
		return String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
	}

	public void onMusicBuff() {
		mBtnPlayPause.setImageResource(Res.drawable.ic_music_stop);
		mTextViewProgress.setText("音乐缓冲中...");
	}
	
	public void onMusicStatus(boolean isPlaying) {
		if(isPlaying){
			mBtnPlayPause.setImageResource(Res.drawable.ic_music_play);
		}else{
			mBtnPlayPause.setImageResource(Res.drawable.ic_music_stop);
		}
	}

	public void onMusicPlayed() {
		mBtnPlayPause.setImageResource(Res.drawable.ic_music_play);
		//mBtnPlayPause.setText("暂停");
	}

	public void onMusicPaused() {
		//mBtnPlayPause.setText("开始");
		mBtnPlayPause.setImageResource(Res.drawable.ic_music_stop);
	}

	private void openMusicList() {
		mListMusic.setFocusable(true);
		mListMusic.setVisibility(View.VISIBLE);
	}

	private void closeMusicList() {
		mListMusic.setFocusable(false);
		mListMusic.setVisibility(View.GONE);
	}

	private static class MusicListAdapter extends SimpleAdapter {

		private int mBackGroundFocus, mBackGroundNormal;
		private int mCurrentItem = -1;

		public MusicListAdapter(Context context, List<? extends Map<String, ?>> data) {
			super(context, data, Res.layout.list_item_music, new String[] { "Song", "Artist" }, new int[] {
				Res.id.textViewSong, Res.id.textViewArtist });
			//Resources res = context.getResources();
			mBackGroundFocus = Res.drawable.view_focused_bk;
			mBackGroundNormal = android.R.color.transparent;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			TextView textViewSong = (TextView) view.findViewById(Res.id.textViewSong);
			TextView textViewArtist = (TextView) view.findViewById(Res.id.textViewArtist);
			if (position == mCurrentItem) {
				//textViewSong.getPaint().setFakeBoldText(true);
				//textViewArtist.getPaint().setFakeBoldText(true);
				//view.setBackgroundResource(mBackGroundFocus);
				//textViewSong.setTextColor(mColorFocus);
				//textViewArtist.setTextColor(mColorFocus);
			} else {
				//textViewSong.getPaint().setFakeBoldText(false);
				//textViewArtist.getPaint().setFakeBoldText(false);
				//view.setBackgroundResource(mBackGroundNormal);
				//textViewSong.setTextColor(mColorNormal);
				//textViewArtist.setTextColor(mColorNormal);
			}
			return view;
		}

		public void setCurrentItem(int position) {
			if (mCurrentItem != position) {
				mCurrentItem = position;
				notifyDataSetChanged();
			}
		}

	}

	public void release() {
		mAudioManager = null;
	}

	public class PopWindowKeyListener implements View.OnKeyListener {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (mAudioManager == null) {
				mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
			}
			int volume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			int maxVol = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			if (event.getAction() == KeyEvent.ACTION_DOWN) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_VOLUME_DOWN:
					int volDown = volume-- > 0 ? volume : 0;
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volDown, 1);
					break;
				case KeyEvent.KEYCODE_VOLUME_UP:
					int volUp = volume++ < maxVol ? volume : maxVol;
					mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volUp, 1);
					break;
				}
			} else {
			}

			return true;
		}
	}

	public static interface MusicPopupWindowListener {
		public void onShow();

		public void onDismiss();
	}

	public interface MusicPopWindowCallback {
		public void setCurrentPlayedIndex(int index);

		public void play();

		public void prev();
		
		public void pause();
		
		public void stop();
	};

	public void setMusicPopWindowCallback(MusicPopWindowCallback callback) {
		mMainContextCallback = callback;
	}
}
