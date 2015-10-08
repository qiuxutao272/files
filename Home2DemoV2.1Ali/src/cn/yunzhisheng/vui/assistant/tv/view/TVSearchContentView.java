/**
 * Copyright (c) 2012-2013 Yunzhisheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : TVStationContentView.java
 * @ProjectName : iShuoShuo2
 * @PakageName : cn.yunzhisheng.ishuoshuo.view
 * @Author : Brant
 * @CreateDate : 2013-1-25
 */
package cn.yunzhisheng.vui.assistant.tv.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.yunzhisheng.common.util.LogUtil;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.assistant.model.TVByDateGroupItem;
import cn.yunzhisheng.vui.assistant.model.TVProgramItem;
import cn.yunzhisheng.vui.assistant.model.TVSearchResult;
import cn.yunzhisheng.vui.assistant.tv.view.ELinearLayout.ViewBinder;
import cn.yunzhisheng.vui.assistant.util.MessageManager;
import cn.yunzhisheng.vui.assistant.util.Util;
import cn.yunzhisheng.vui.assistant.widget.TabWidget;
import cn.yunzhisheng.vui.assistant.widget.TabWidget.OnTabSelectionChanged;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Brant
 * @CreateDate : 2013-1-25
 * @ModifiedBy : Brant
 * @ModifiedDate: 2013-1-25
 * @Modified: 2013-1-25: 实现基本功能
 */
public abstract class TVSearchContentView extends LinearLayout implements ISessionView {
	public static final String TAG = "TVSearchContentView";
	private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	private SparseArray<List<HashMap<String, Object>>> mDataCache = new SparseArray<List<HashMap<String, Object>>>();
	private Context mContext;
	private Resources res;
	private LayoutInflater mInflater;
	private TabWidget mTabWidget;
	private int mCurrentTab = -1;
	private ELinearLayout mList;
	private TVSearchResult mResult;
	private ScrollView mScrollLayout;
	private int mItemPaddingLeft, mItemPaddingRight, mItemPaddingTop, mItemPaddingBottom;

	public static final String MSG_KEY_LEFT = "MSG_KEY_LEFT";
	public static final String MSG_KEY_RIGHT = "MSG_KEY_RIGHT";

	private OnTabSelectionChanged mTabSelectionChangedListener = new OnTabSelectionChanged() {

		@Override
		public void onTabSelectionChanged(int tabIndex, boolean clicked) {
			setCurrentTab(tabIndex);
		}
	};

	public TVSearchContentView(Context context) {
		super(context);
		mContext = context;
		// setFocusable(true);
		res = getResources();
		mItemPaddingLeft = res.getDimensionPixelSize(Res.dimen.function_item_padding_left);
		mItemPaddingRight = res.getDimensionPixelSize(Res.dimen.function_item_padding_right);
		mItemPaddingTop = 0;
		mItemPaddingBottom = 0;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater.inflate(Res.layout.tv_search_content_view, this, true);
		findViews();
		setListener();
		init();

		registReceiver();
	}

	private void findViews() {
		mTabWidget = (TabWidget) findViewById(android.R.id.tabs);
		mTabWidget.setDividerDrawable(Res.drawable.horizontal_divider);
		mList = (ELinearLayout) findViewById(android.R.id.list);
		mScrollLayout = (ScrollView) findViewById(Res.id.tv_scroll_item);
	}

	private void setListener() {
		mTabWidget.setTabSelectionListener(mTabSelectionChangedListener);
	}

	private void registReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(MSG_KEY_LEFT);
		filter.addAction(MSG_KEY_RIGHT);
		filter = MessageManager.registPrivateIntentFilter(filter, mContext);
		mContext.registerReceiver(mBroadcastReceiver, filter);
	}

	private void init() {
		// setFocusable(true);
		mTabWidget.setDrawBottomStrips(false);
		mList.setBindResource(getBindResource());
		mList.setDividerDrawable(Res.drawable.horizontal_divider);
		mList.setViewBinder(new ViewBinder() {

			@Override
			public void bindViewData(int position, View view, Map<String, ?> data) {
				bindView(position, view, data);
			}
		});
	}

	private View getTabIndicator(String title) {
		View tab = mInflater.inflate(Res.layout.tv_tab_indicator, mTabWidget, false);
		TextView tv = (TextView) tab.findViewById(Res.id.title);
		if (mResult != null && mResult.byDate.size() == 1) {
			tv.setBackgroundResource(Res.drawable.list_item_mid_bg);
		} else {
			tv.setBackgroundResource(Res.drawable.tab_item_bg);
		}
		tv.setText(title);
		return tab;
	}

	public int getItemCount() {
		return mList.getItemCount();
	}

	public void setCurrentTab(int index) {
		if (index < 0 || index >= mTabWidget.getTabCount()) {
			return;
		}

		if (index == mCurrentTab) {
			return;
		}

		LogUtil.d(TAG, "setCurrentTab");
		mCurrentTab = index;

		// Call the tab widget's focusCurrentTab(), instead of just
		// selecting the tab.
		mTabWidget.focusCurrentTab(mCurrentTab);
		updateList(index);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtil.d(TAG, "onKeyDown:event " + event);
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_LEFT_BRACKET:
			if (mCurrentTab != 0) {
				setCurrentTab(mCurrentTab - 1);
				return true;
			}
			break;
		case KeyEvent.KEYCODE_RIGHT_BRACKET:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if (mCurrentTab != mTabWidget.getTabCount() - 1) {
				setCurrentTab(mCurrentTab + 1);
				return true;
			}
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean isTemporary() {
		return false;
	}

	@Override
	public void requestSuperFocus() {
		LogUtil.d(TAG, "requestSuperFocus:chilCount " + mList.getItemChildCount());
		if (mList.getItemChildCount() >= 0) {
			mList.getItemViewAt(0).requestFocus();
		}
	}

	private void updateList(int tabIndex) {
		if (tabIndex < 0 || tabIndex >= mResult.byDate.size()) {
			return;
		}
		LogUtil.d(TAG, "updateList");
		mList.setData(getListData(tabIndex));

		if (mList.getItemCount() < 5) {
			mScrollLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, res
				.getDimensionPixelSize(Res.dimen.function_item_height) * mList.getItemCount()));
		} else {
			mScrollLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, res
				.getDimensionPixelSize(Res.dimen.function_item_height) * 5));
		}

		requestSuperFocus();
	}

	private List<HashMap<String, Object>> getListData(int tabIndex) {
		List<HashMap<String, Object>> data = mDataCache.get(tabIndex);
		if (data == null) {
			data = trans2HasMapData(mResult.byDate.get(tabIndex).programs);
		}
		return data;
	}

	public abstract List<HashMap<String, Object>> trans2HasMapData(ArrayList<TVProgramItem> programs);

	public abstract int getBindResource();

	public void bindView(int position, View view, Map<String, ?> data) {
		if (position == getItemCount() - 1) {
			view.setBackgroundResource(Res.drawable.list_item_mid_bg);
		} else {
			view.setBackgroundResource(Res.drawable.list_item_mid_bg);
		}
		view.setPadding(mItemPaddingLeft, mItemPaddingTop, mItemPaddingRight, mItemPaddingBottom);
	}

	public void setTVSearchResult(TVSearchResult result) {
		if (result == null || result.byDate.size() == 0) {
			return;
		}
		mResult = result;
		mTabWidget.removeAllViews();
		for (int i = 0; i < result.byDate.size(); i++) {
			TVByDateGroupItem item = result.byDate.get(i);
			mTabWidget.addView(getTabIndicator(getReadableDateTime(getContext(), item.date)));
		}
		setCurrentTab(0);
	}

	public static String getReadableDateTime(Context context, String date) {
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(DEFAULT_DATE_FORMAT.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
			return date;
		}
		int days = Util.daysOfTwo(Calendar.getInstance(), calendar);
		switch (days) {
		case 3:
			return context.getString(Res.string.three_days_later_tv);
		case 2:
			return context.getString(Res.string.day_after_tomorrow);
		case 1:
			return context.getString(Res.string.tomorrow);
		case 0:
			return context.getString(Res.string.today);
		case -1:
			return context.getString(Res.string.yesterday);
		case -2:
			return context.getString(Res.string.day_before_yesterday);
		default:
			return date;
		}
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			LogUtil.d(TAG, intent.toString());
			String action = intent.getAction();
			if (MSG_KEY_LEFT.equals(action)) {
				setCurrentTab(mCurrentTab - 1);
			} else if (MSG_KEY_RIGHT.equals(action)) {
				if (mCurrentTab == mTabWidget.getTabCount() - 1) {
					setCurrentTab(0);
				} else {
					setCurrentTab(mCurrentTab + 1);
				}
			}

			try {
				abortBroadcast();
			} catch (Exception e) {
			} 
		}
	};

}
