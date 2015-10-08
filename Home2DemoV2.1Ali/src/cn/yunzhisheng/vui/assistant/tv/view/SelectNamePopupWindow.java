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
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import cn.yunzhisheng.common.util.LogUtil;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.assistant.preference.SessionPreferenceOfIntent;
import cn.yunzhisheng.vui.assistant.preference.UserPreference;
import cn.yunzhisheng.vui.assistant.util.MessageManager;

public class SelectNamePopupWindow extends PopupWindow implements OnItemClickListener {
	public static final String TAG = "SelectNamePopupWindow";

	private ListView mListMusic;
	private MusicListAdapter mAdapter;
	private ArrayList<HashMap<String, Object>> mData;
	View mLayoutView;
	View contentView;
	private Context mContext;
	private Resources res;
	private UserPreference mUserPreference;
	private static String mTVName = "";

	public SelectNamePopupWindow(Context context, ArrayList<HashMap<String, Object>> data) {
		super(context);
		mContext = context;
		mUserPreference = new UserPreference(mContext);
		mTVName = mUserPreference.getString(
			UserPreference.TV_FRIENDLY_NAME_KEY,
			UserPreference.TV_FRIENDLY_NAME_DEFAULT);
		res = mContext.getResources();
		setWidth(res.getDimensionPixelSize(Res.dimen.pop_selectname_width));
		setHeight(LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(new ColorDrawable());

		mLayoutView = View.inflate(context, Res.layout.selectname, null);
		mLayoutView.setFocusable(true);
		setContentView(mLayoutView);
		findViews();
		setListener();
		setFocusable(true);
		setOutsideTouchable(true);
		mData = new ArrayList<HashMap<String, Object>>();
		mData = data;
		mAdapter = new MusicListAdapter(context, mData);
		mAdapter.notifyDataSetChanged();
		mListMusic.setAdapter(mAdapter);
		mListMusic.requestFocus();
	}

	private void findViews() {
		contentView = getContentView();
		contentView.setFocusable(true);
		contentView.setFocusableInTouchMode(true);
		mListMusic = (ListView) contentView.findViewById(android.R.id.list);
		mListMusic.setFocusable(true);
		mListMusic.setFocusableInTouchMode(true);
	}

	private void setListener() {
		mListMusic.setOnItemClickListener(this);
	}

	@Override
	public void showAsDropDown(View anchor) {
		super.showAsDropDown(anchor);
	}

	@Override
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		TextView textView = (TextView) view.findViewById(Res.id.textViewTVName);
		mTVName = textView.getText().toString();
		if (mUserPreference != null) {
			mUserPreference.putString(UserPreference.TV_FRIENDLY_NAME_KEY, mTVName);
		}
		mAdapter.notifyDataSetChanged();
		Bundle extras = new Bundle();
		extras.putString(SessionPreferenceOfIntent.KEY_PARAM, "SERVER");
		showMessage(SessionPreferenceOfIntent.INTENT_SETTVNAME, extras);
		dismiss();
	}

	private static class MusicListAdapter extends SimpleAdapter {

		public MusicListAdapter(Context context, List<? extends Map<String, ?>> data) {
			super(
				context,
				data,
				Res.layout.selectname_list_item,
				new String[] { "Name" },
				new int[] { Res.id.textViewTVName });
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			TextView textView = (TextView) view.findViewById(Res.id.textViewTVName);
			ImageView imgSelected = (ImageView) view.findViewById(Res.id.imgSelected);
			if (textView.getText().toString().equals(mTVName)) {
				imgSelected.setVisibility(View.VISIBLE);
			} else {
				imgSelected.setVisibility(View.INVISIBLE);
			}
			return view;
		}
	}

	private void showMessage(String message, Bundle extras) {
		MessageManager.sendPrivateMessage(mContext, message, extras);
	}
}
