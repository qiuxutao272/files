package cn.yunzhisheng.vui.assistant.tv.view;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.assistant.tv.media.Track;
import cn.yunzhisheng.vui.assistant.tv.view.ELinearLayout.OnItemClickListener;
import cn.yunzhisheng.vui.assistant.tv.view.ELinearLayout.ViewBinder;

public class MusicContentView extends LinearLayout implements ISessionView {
	private static final String TAG = "MusicContentView";
	private int mItemPaddingLeft, mItemPaddingRight, mItemPaddingTop,
			mItemPaddingBottom;
	private boolean mPlayStatus = false;
	private int mCurrentIndex = -1;
	private ProgressBar mProgressBar;
	private TextView mTextViewSong, mTextViewArtist, mTextViewProgress;
	private ELinearLayout mList;
	private LayoutInflater mInflater;
	private ScrollView mScrollLayout;
	private Resources res;

	public MusicContentView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setFocusable(true);
		res = getResources();
		mItemPaddingLeft = res
				.getDimensionPixelSize(Res.dimen.function_item_padding_left);
		mItemPaddingRight = res
				.getDimensionPixelSize(Res.dimen.function_item_padding_right);
		mItemPaddingTop = 0;
		mItemPaddingBottom = 0;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater.inflate(Res.layout.music_content_view, this, true);
		mProgressBar = (ProgressBar) findViewById(Res.id.progressBarMusic);
		mTextViewSong = (TextView) findViewById(Res.id.textViewMusicHeaderSong);
		mTextViewArtist = (TextView) findViewById(Res.id.textViewMusicHeaderArtist);
		mTextViewProgress = (TextView) findViewById(Res.id.textViewMusicProgress);
		mScrollLayout = (ScrollView) findViewById(Res.id.scroll_item);
		
		mList = (ELinearLayout) findViewById(android.R.id.list);
		mList.setDividerDrawable(Res.drawable.horizontal_divider);
		mList.setBindResource(Res.layout.list_item_music);
		mList.setViewBinder(new ViewBinder() {

			@Override
			public void bindViewData(int position, View view,
					Map<String, ?> data) {
				TextView tvSong = (TextView) view
						.findViewById(Res.id.textViewSong);
				tvSong.setText((position + 1) + "." + (String) data.get("Song"));
				TextView tvArtist = (TextView) view
						.findViewById(Res.id.textViewArtist);
				tvArtist.setText((String) data.get("Artist"));
				ImageView imgStatus = (ImageView) view
						.findViewById(Res.id.imageViewStatus);

				if (position == mCurrentIndex) {
					imgStatus.setVisibility(View.VISIBLE);
					imgStatus
							.setImageResource(mPlayStatus ? Res.drawable.ic_music_stop
									: Res.drawable.ic_music_play);
				} else {
					imgStatus.setVisibility(View.GONE);
				}
				if (position == mList.getItemCount() - 1) {
					view.setBackgroundResource(Res.drawable.list_item_mid_bg);
				} else {
					view.setBackgroundResource(Res.drawable.list_item_mid_bg);
				}
				view.setPadding(mItemPaddingLeft, mItemPaddingTop,
						mItemPaddingRight, mItemPaddingBottom);
				view.setTag(data.get("Tag"));
				view.setFocusable(true);
				view.setClickable(true);
			}
		});
	}

	public MusicContentView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MusicContentView(Context context) {
		this(context, null);
	}

	public int getCurrentIndex() {
		return mCurrentIndex;
	}

	public void setCurrentIndex(int index) {
		mCurrentIndex = index;
	}

	public void setPlayingInfo(String song, String artist) {
		mTextViewSong.setText(song);
		mTextViewArtist.setText(artist);
	}

	/**
	 * 设置当前播放进度03:45/05:00
	 * 
	 * @param progress
	 */
	public void setProgress(String progress) {
		mTextViewProgress.setText(progress);
	}

	/**
	 * 设置进度条的最大值
	 * 
	 * @param max
	 */
	public void setMaxProgress(int max) {
		mProgressBar.setMax(max);
	}

	/**
	 * 设置进度条进度
	 * 
	 * @param progress
	 */
	public void setProgress(int progress) {
		mProgressBar.setProgress(progress);
	}

	/**
	 * 设置进度条第二进度
	 * 
	 * @param progress
	 */
	public void setSecondaryProgress(int progress) {
		mProgressBar.setSecondaryProgress(progress);
	}

	public void setPlayList(List<Track> list) {
		List<HashMap<String, ?>> data = new ArrayList<HashMap<String, ?>>();
		for (int i = 0; i < list.size(); i++) {
			HashMap<String, Object> item = new HashMap<String, Object>(2);
			Track track = list.get(i);
			item.put("Song", track.getTitle());
			item.put("Artist", track.getArtist());
			data.add(item);
		}
		mList.setData(data);
	}

	public void updateList() {
		mList.notifyDataSetChanged();
	}

	public boolean isPlaying() {
		return mPlayStatus;
	}

	public void setPlayStatus(boolean status) {
		mPlayStatus = status;
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		mList.setOnItemClickListener(listener);
	}

	@Override
	public boolean isTemporary() {
		return true;
	}

	@Override
	public void requestSuperFocus() { 
		Log.d(TAG, "requestSuperFocus   mCurrentIndex : "+mCurrentIndex);
		
		if(mList.getItemCount() < 5){
			mScrollLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					res.getDimensionPixelSize(Res.dimen.function_item_height)*mList.getItemCount()));
		}
		
		
		if (mCurrentIndex != -1) {    
			mList.getItemViewAt(mCurrentIndex).requestFocus();
		}else if (mList.getItemCount() >= 0) { 
			mList.getItemViewAt(0).requestFocus();
		} 
		
		
	}

	/**
	 * @Description	: release
	 * @Author		: Dancindream
	 * @CreateDate	: 2013-12-19
	 * @see cn.yunzhisheng.vui.assistant.tv.view.ISessionView#release()
	 */
	@Override
	public void release() {
		
	}

}
