/**
 * Copyright (c) 2012-2012 Mango(Shanghai) Co.Ltd. All right reserved.
 * @FileName : FunctionView.java
 * @ProjectName : iShuoShuo2
 * @PakageName : cn.yunzhisheng.ishuoshuo.view
 * @Author : Brant
 * @CreateDate : 2012-11-12
 */
package cn.yunzhisheng.vui.assistant.tv.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import cn.yunzhisheng.common.util.LogUtil;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.assistant.model.KnowledgeBase;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.vui.assistant.tv.view.HelpELinearLayout.OnItemClickListener;
import cn.yunzhisheng.vui.assistant.tv.view.HelpELinearLayout.OnTabSelectionChanged;
import cn.yunzhisheng.vui.assistant.tv.view.HelpELinearLayout.HelpViewBinder;

public class HelpShowView extends FrameLayout implements ISessionView {
	public static final String TAG = "HelpShowView";
	private int mItemPaddingLeft, mItemPaddingRight, mItemPaddingTop, mItemPaddingBottom;
	private LayoutInflater mInflater;
	private Resources res;
	private HelpELinearLayout mList;
	private Context mContext;
	private TextView titleTextView, contentOneTextView, contentTwoTextView, contentThreeTextView, contentFourTextView;
	private String[] answerArr;
	private int titleRes, functionRes;
	private ImageView icon;

	public HelpShowView(Context context) {
		super(context);
		mContext = context;
		res = getResources();
		int margin = res.getDimensionPixelSize(Res.dimen.session_item_margin);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, margin, 0, margin);
		setLayoutParams(lp);

		int l = res.getDimensionPixelSize(Res.dimen.function_padding_left);
		int r = res.getDimensionPixelSize(Res.dimen.function_padding_right);
		int t = res.getDimensionPixelSize(Res.dimen.function_padding_top);
		int b = res.getDimensionPixelSize(Res.dimen.function_padding_bottom);
		setPadding(l, t, r, b);

		mItemPaddingLeft = res.getDimensionPixelSize(Res.dimen.function_item_padding_left);
		mItemPaddingRight = res.getDimensionPixelSize(Res.dimen.function_item_padding_right);
		mItemPaddingTop = 0;
		mItemPaddingBottom = 0;

		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater.inflate(Res.layout.help_content_view, this, true);

		titleTextView = (TextView) findViewById(Res.id.imageViewHelpTitle);
		contentOneTextView = (TextView) findViewById(Res.id.imageViewHelpContent_one);
		contentTwoTextView = (TextView) findViewById(Res.id.imageViewHelpContent_two);
		contentThreeTextView = (TextView) findViewById(Res.id.imageViewHelpContent_three);
		contentFourTextView = (TextView) findViewById(Res.id.imageViewHelpContent_four);

		mList = (HelpELinearLayout) findViewById(android.R.id.list);

		mList.setDividerDrawable(Res.drawable.horizontal_divider);
		mList.setBindResource(Res.layout.help_item_view);

		mList.setViewBinder(new HelpViewBinder() {

			@Override
			public void bindViewData(int position, View view, Map<String, ?> data) {
				icon = (ImageView) view.findViewById(Res.id.imageViewHelpIcon);
				String tag = (String) data.get("Tag");
				int iconRes = (Integer) data.get("IconRes");
				icon.setImageResource(iconRes);
				view.setBackgroundResource(Res.drawable.tab_item_bg);
				view.setTag(tag);
				view.setFocusable(true);
				view.setClickable(true);
			}
		});
		mList.setOnItemClickListener(onItemClickListener);
		mList.setTabSelectionListener(mTabSelectionChangedListener);
		mList.setFocusable(true);
	}

	private HashMap<String, Object> getDataItem(String tag, int iconRes, int functionRes, int functionArrRes) {
		HashMap<String, Object> item = new HashMap<String, Object>();
		item.put("Tag", tag);
		item.put("IconRes", iconRes);
		item.put("Function", functionRes);
		item.put("FunctionArrRes", functionArrRes);
		return item;
	}

	public void initHelpShowViews(boolean hasNetWork) {
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		// video
		data.add(getDataItem(
			SessionPreference.DOMAIN_VIDEO,
			Res.drawable.ic_function_video,
			Res.string.function_video,
			Res.array.function_example_video));

		// tv
		if (hasNetWork) {
			data.add(getDataItem(
				SessionPreference.DOMAIN_TV,
				Res.drawable.ic_function_tv,
				Res.string.function_tv,
				Res.array.function_example_tv));
		}

		// channel swich
//		if (hasNetWork) {
//			data.add(getDataItem(
//				SessionPreference.DOMAIN_CHANNEL_SWITCH,
//				Res.drawable.ic_function_channel,
//				Res.string.function_channel_switch,
//				Res.array.function_example_channel_switch));
//		}

//		// Music
//		if (hasNetWork) {
//			data.add(getDataItem(
//				SessionPreference.DOMAIN_MUSIC,
//				Res.drawable.ic_function_music,
//				Res.string.function_music,
//				Res.array.function_example_music));
//		}

		// Setting
		data.add(getDataItem(
			SessionPreference.DOMAIN_SETTING,
			Res.drawable.ic_function_setting,
			Res.string.function_settings,
			Res.array.function_example_setting));

		// Weather
		if (hasNetWork) {
			data.add(getDataItem(
				SessionPreference.DOMAIN_WEATHER,
				Res.drawable.ic_function_weather,
				Res.string.function_weather,
				Res.array.function_example_weather));
		}
		// App
		data.add(getDataItem(
			SessionPreference.DOMAIN_APP,
			Res.drawable.ic_function_app,
			Res.string.function_app,
			Res.array.function_example_app));

		// movie
		if (hasNetWork) {
			data.add(getDataItem(
				SessionPreference.DOMAIN_MOVIE,
				Res.drawable.ic_function_video,
				Res.string.function_movie,
				Res.array.function_example_movie));
		}

//		// Weibo
//		if (hasNetWork) {
//			data.add(getDataItem(
//				SessionPreference.DOMAIN_WEIBO,
//				Res.drawable.ic_function_weibo,
//				Res.string.function_weibo,
//				Res.array.function_example_weibo));
//		}

		// shop
		if (hasNetWork) {
			data.add(getDataItem(
				SessionPreference.DOMAIN_SHOP,
				Res.drawable.ic_function_shop,
				Res.string.function_shop,
				Res.array.function_example_shop));
		}

		// stock
		if (hasNetWork) {
			data.add(getDataItem(
				SessionPreference.DOMAIN_STOCK,
				Res.drawable.ic_function_stock,
				Res.string.function_stock,
				Res.array.function_example_stock));
		}

		// news
		if (hasNetWork) {
			data.add(getDataItem(
				SessionPreference.DOMAIN_NEWS,
				Res.drawable.ic_function_news,
				Res.string.function_news,
				Res.array.function_example_news));
		}
//		// sitemap
//		if (hasNetWork) {
//			data.add(getDataItem(
//				SessionPreference.DOMAIN_SITEMAP,
//				Res.drawable.ic_function_sitemap,
//				Res.string.function_sitemap,
//				Res.array.function_example_sitemap));
//		}

		mList.setData(data);

		requestSuperFocus();
	}

	@Override
	public void requestSuperFocus() {
		LogUtil.d(TAG, "requestSuperFocus");
		if (mList.getItemChildCount() >= 0) {
			mList.getItemViewAt(0).requestFocus();
		}
	}

	@Override
	public boolean isTemporary() {
		return false;
	}

	/**
	 * @Description : release
	 * @Author : Dancindream
	 * @CreateDate : 2013-12-19
	 * @see cn.yunzhisheng.vui.assistant.tv.view.ISessionView#release()
	 */
	@Override
	public void release() {

	}

	private void changeText(int tabIndex) {
		HashMap<String, Object> dataText = new HashMap<String, Object>();
		if (tabIndex == 0) {
			dataText = getDataItem(
				SessionPreference.DOMAIN_VIDEO,
				Res.drawable.ic_function_video,
				Res.string.function_video,
				Res.array.function_example_video);
		} else if (tabIndex == 1) {
			dataText = getDataItem(
				SessionPreference.DOMAIN_TV,
				Res.drawable.ic_function_tv,
				Res.string.function_tv,
				Res.array.function_example_tv);
		} else if (tabIndex == 2) {
			dataText = getDataItem(
				SessionPreference.DOMAIN_CHANNEL_SWITCH,
				Res.drawable.ic_function_channel,
				Res.string.function_channel_switch,
				Res.array.function_example_channel_switch);
		} else if (tabIndex == 3) {
			dataText = getDataItem(
				SessionPreference.DOMAIN_MUSIC,
				Res.drawable.ic_function_music,
				Res.string.function_music,
				Res.array.function_example_music);
		} else if (tabIndex == 4) {
			dataText = getDataItem(
				SessionPreference.DOMAIN_SETTING,
				Res.drawable.ic_function_setting,
				Res.string.function_settings,
				Res.array.function_example_setting);
		} else if (tabIndex == 5) {
			dataText = getDataItem(
				SessionPreference.DOMAIN_WEATHER,
				Res.drawable.ic_function_weather,
				Res.string.function_weather,
				Res.array.function_example_weather);
		} else if (tabIndex == 6) {
			dataText = getDataItem(
				SessionPreference.DOMAIN_APP,
				Res.drawable.ic_function_app,
				Res.string.function_app,
				Res.array.function_example_app);
		} else if (tabIndex == 7) {
			dataText = getDataItem(
				SessionPreference.DOMAIN_MOVIE,
				Res.drawable.ic_function_video,
				Res.string.function_movie,
				Res.array.function_example_movie);
		} else if (tabIndex == 8) {
			dataText = getDataItem(
				SessionPreference.DOMAIN_WEIBO,
				Res.drawable.ic_function_weibo,
				Res.string.function_weibo,
				Res.array.function_example_weibo);
		} else if (tabIndex == 9) {
			dataText = getDataItem(
				SessionPreference.DOMAIN_SHOP,
				Res.drawable.ic_function_shop,
				Res.string.function_shop,
				Res.array.function_example_shop);
		} else if (tabIndex == 10) {
			dataText = getDataItem(
				SessionPreference.DOMAIN_STOCK,
				Res.drawable.ic_function_stock,
				Res.string.function_stock,
				Res.array.function_example_stock);
		} else if (tabIndex == 11) {
			dataText = getDataItem(
				SessionPreference.DOMAIN_NEWS,
				Res.drawable.ic_function_news,
				Res.string.function_news,
				Res.array.function_example_news);
		} else if (tabIndex == 12) {
			dataText = getDataItem(
				SessionPreference.DOMAIN_SITEMAP,
				Res.drawable.ic_function_sitemap,
				Res.string.function_sitemap,
				Res.array.function_example_sitemap);
		} else {
			dataText = getDataItem(
				SessionPreference.DOMAIN_VIDEO,
				Res.drawable.ic_function_video,
				Res.string.function_video,
				Res.array.function_example_video);
		}

		titleRes = (Integer) dataText.get("Function");
		functionRes = (Integer) dataText.get("FunctionArrRes");
		answerArr = KnowledgeBase.getStringArray(mContext, functionRes);
		titleTextView.setText(titleRes);

		if (answerArr != null) {
			if (answerArr.length == 2) {
				contentOneTextView.setText(answerArr[0]);
				contentTwoTextView.setText(answerArr[1]);
				contentThreeTextView.setVisibility(View.GONE);
				contentFourTextView.setVisibility(View.GONE);
			} else if (answerArr.length == 3) {
				contentOneTextView.setText(answerArr[0]);
				contentTwoTextView.setText(answerArr[1]);
				contentThreeTextView.setVisibility(View.VISIBLE);
				contentThreeTextView.setText(answerArr[2]);
				contentFourTextView.setVisibility(View.GONE);
			} else if (answerArr.length == 4) {
				contentOneTextView.setText(answerArr[0]);
				contentTwoTextView.setText(answerArr[1]);
				contentThreeTextView.setVisibility(View.VISIBLE);
				contentThreeTextView.setText(answerArr[2]);
				contentFourTextView.setVisibility(View.VISIBLE);
				contentFourTextView.setText(answerArr[3]);
			}
		}

	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onTabSelectionChanged(int tabIndex, boolean clicked) {
			changeText(tabIndex);
		}
	};

	private OnTabSelectionChanged mTabSelectionChangedListener = new OnTabSelectionChanged() {

		@Override
		public void onTabSelectionChanged(int tabIndex, boolean clicked) {
			changeText(tabIndex);
		}
	};
}
