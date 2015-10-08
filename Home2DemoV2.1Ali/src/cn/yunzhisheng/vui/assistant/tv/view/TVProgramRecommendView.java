/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : TVProgramRecommendView.java
 * @ProjectName : iShuoShuo2
 * @PakageName : cn.yunzhisheng.ishuoshuo.view
 * @Author : CavanShi
 * @CreateDate : 2013-3-11
 */
package cn.yunzhisheng.vui.assistant.tv.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.assistant.model.TVBroadcastsGroupItem;
import cn.yunzhisheng.vui.assistant.model.TVProgramItem;
import cn.yunzhisheng.vui.assistant.model.TVRecommendResult;
import cn.yunzhisheng.vui.assistant.tv.view.ELinearLayout.OnItemClickListener;
import cn.yunzhisheng.vui.assistant.tv.view.ELinearLayout.ViewBinder;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : CavanShi
 * @CreateDate : 2013-3-11
 * @ModifiedBy : CavanShi
 * @ModifiedDate: 2013-3-11
 * @Modified: 2013-3-11: 实现基本功能
 */
public class TVProgramRecommendView extends LinearLayout implements ISessionView {
	public static final String TAG = "TVProgramRecommendView";
	private static final SimpleDateFormat mSrcDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm", Locale.getDefault());
	private static final SimpleDateFormat mDateFormat = new SimpleDateFormat(
			"MM-dd HH:mm", Locale.getDefault());
	private int mExpandedPosition = -1;
	private LayoutInflater mInflater;
	private ELinearLayout mList;
	private int mItemPaddingLeft, mItemPaddingRight, mItemPaddingTop,
			mItemPaddingBottom;
	private Resources res;
	private ScrollView mScrollLayout;

	private TVRecommendResult mTVRecommendResult;

	public TVProgramRecommendView(Context context) {
		this(context, null, 0);
	}

	public TVProgramRecommendView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater.inflate(Res.layout.tv_recommend_content_view, this, true);
		res = getResources();
		mItemPaddingLeft = res
				.getDimensionPixelSize(Res.dimen.function_item_padding_left);
		mItemPaddingRight = res
				.getDimensionPixelSize(Res.dimen.function_item_padding_right);
		mItemPaddingTop = 0;
		mItemPaddingBottom = 0;
		init();
		setListener();
	}

	private void init() {
		mScrollLayout = (ScrollView) findViewById(Res.id.tv_scroll_item);
		mList = (ELinearLayout) findViewById(Res.id.tv_recom_programm);
		mList.setBindResource(Res.layout.tv_recommend_list_item);
		mList.setDividerDrawable(Res.drawable.horizontal_divider);
		mList.setViewBinder(new ViewBinder() {

			@Override
			public void bindViewData(int position, View view,
					Map<String, ?> data) {
				TextView recommTitle = (TextView) view
						.findViewById(Res.id.tv_recomm_title);
				recommTitle.setText((String) data.get("name"));
				TextView recommData = (TextView) view
						.findViewById(Res.id.tv_recomm_data);
				String s = "推荐指数: " + (String) data.get("score");
				recommData.setText(s);

				RatingBar ratingBar = (RatingBar) view
						.findViewById(Res.id.tv_recomm_ratingBarPoi);
				Integer num = Integer.valueOf((String) data.get("score"));
				ratingBar.setProgress(num);
				if (position == mList.getItemCount() - 1) {
					view.setBackgroundResource(Res.drawable.list_item_down_bg);
				} else {
					view.setBackgroundResource(Res.drawable.list_item_mid_bg);
				}
				view.setPadding(mItemPaddingLeft, mItemPaddingTop,
						mItemPaddingRight, mItemPaddingBottom);

				TextView noPrograms = (TextView) view
						.findViewById(Res.id.tv_recomm_detail_notice);
				ELinearLayout programDetailList = (ELinearLayout) view
						.findViewById(Res.id.tv_recom_list_item);

				programDetailList
						.setBindResource(Res.layout.tv_recommend_list_item_detail);
				programDetailList
						.setDividerDrawable(Res.drawable.horizontal_divider);
				programDetailList.setViewBinder(new ViewBinder() {
					@Override
					public void bindViewData(int position, View view,
							Map<String, ?> data) {
						TextView title = (TextView) view
								.findViewById(Res.id.recommend_item_detail_title);
						TextView channel = (TextView) view
								.findViewById(Res.id.recommend_item_detail_channel);
						TextView time = (TextView) view
								.findViewById(Res.id.recommend_item_detail_time);
						title.setText((String) data.get("title"));
						channel.setText((String) data.get("channel"));
						time.setText(formatDate((String) data.get("time")));
					}
				});

				ArrayList<TVProgramItem> programmes = mTVRecommendResult.broadcasts
						.get(position).programs;
				noPrograms.setVisibility(programmes.size() == 0 ? View.VISIBLE
						: View.GONE);
				ArrayList<HashMap<String, String>> datas = new ArrayList<HashMap<String, String>>();
				for (TVProgramItem program : programmes) {
					HashMap<String, String> item = new HashMap<String, String>();
					item.put("time", program.time);
					item.put("channel", program.channel);
					item.put("title", program.title);
					datas.add(item);
				}
				programDetailList.setData(datas);
			}
		});
	}

	public void setListener() {
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClicked(int position, Map<String, ?> data, View v) {
				if (position == mExpandedPosition) {
					View view = mList.getItemViewAt(position);
					View itemExpand = (LinearLayout) view
							.findViewById(Res.id.tv_recomm_expandview);
					itemExpand.setVisibility(View.GONE);
					mExpandedPosition = -1;
				} else {
					if (mExpandedPosition != -1) {
						View oldItem = mList.getItemViewAt(mExpandedPosition);
						View oldItemExpand = (LinearLayout) oldItem
								.findViewById(Res.id.tv_recomm_expandview);
						oldItemExpand.setVisibility(View.GONE);
					}
					View item = mList.getItemViewAt(position);
					View itemExpand = (LinearLayout) item
							.findViewById(Res.id.tv_recomm_expandview);
					itemExpand.setVisibility(View.VISIBLE);
					mExpandedPosition = position;
				}
			}
		});
	}

	public void setTVRecommendResult(TVRecommendResult datas) {
		mTVRecommendResult = datas;
		ArrayList<HashMap<String, String>> allResults = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < datas.broadcasts.size(); i++) {
			TVBroadcastsGroupItem tvRecomm = datas.broadcasts.get(i);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", tvRecomm.name);
			map.put("score", tvRecomm.score);
			allResults.add(map);
		}
		mList.setData(allResults);
	}

	public View getItemViewAt(int index) {
		if (index >= 0 && index < mList.getItemCount()) {
			return mList.getItemViewAt(index);
		}
		return null;
	}

	public Map<String, ?> getData(int position) {
		return mList.getData(position);
	}

	public void setExpandedPosition(int pos) {
		mExpandedPosition = pos;
	}

	public int getExpandedPosition() {
		return mExpandedPosition;
	}

	@Override
	public boolean isTemporary() {
		return false;
	}

	@Override
	public void requestSuperFocus() {
		if (mList.getItemCount() >= 0) {
			mList.getItemViewAt(0).requestFocus();
		}
		if(mList.getItemCount() < 5){
			mScrollLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					res.getDimensionPixelSize(Res.dimen.function_item_height)*mList.getItemCount()));
		}
	}

	private String formatDate(String datetime) {
		try {
			Date date = mSrcDateFormat.parse(datetime);
			return mDateFormat.format(date);
		} catch (ParseException e) {
			return datetime;
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
