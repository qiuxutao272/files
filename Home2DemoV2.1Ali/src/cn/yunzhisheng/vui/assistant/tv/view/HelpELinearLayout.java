/**
 * Copyright (c) 2012-2013 Yunzhisheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : ELinearLayout.java
 * @ProjectName : iShuoShuo2
 * @PakageName : cn.yunzhisheng.ishuoshuo.view
 * @Author : Brant
 * @CreateDate : 2013-1-31
 */
package cn.yunzhisheng.vui.assistant.tv.view;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.assistant.widget.TabWidget.OnTabSelectionChanged;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Brant
 * @CreateDate : 2013-1-31
 * @ModifiedBy : Brant
 * @ModifiedDate: 2013-1-31
 * @Modified: 2013-1-31: 实现基本功能
 */
@SuppressLint("NewApi")
public class HelpELinearLayout extends LinearLayout implements OnFocusChangeListener{
	public static final String TAG = "ELinearLayout";
	private HelpViewBinder mViewBinder;
	private List<? extends Map<String, ?>> mData;
	private LayoutInflater mInflater;
	private int mResource;
	private int mSelectedTab = 0;
	private Drawable mDividerDrawable;

	protected LinearLayout mItemsContainer;
	protected LinearLayout mFooterContainer;
	private int mDividerHorizontalMargin;

	private OnItemClickListener mOnItemClickListener;
	private OnTabSelectionChanged mSelectionChangedListener;
	
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			default:
				if (mOnItemClickListener != null) {
					int position = (Integer) v.getTag();
					/*mOnItemClickListener.onItemClicked(position,
							mData.get(position), v);*/
					mOnItemClickListener.onTabSelectionChanged(position, false);
				}
				break;
			}

		}
	};
	
	
	private OnMoreClickListener mOnMoreClickListener;
	private OnClickListener mMoreClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mOnMoreClickListener.onMoreClick();
		}
	};

	public HelpELinearLayout(Context context) {
		this(context, null);
	}

	public HelpELinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(VERTICAL);
		mItemsContainer = new LinearLayout(context);
		mItemsContainer.setOrientation(VERTICAL);
		addView(mItemsContainer);

		/*mFooterContainer = new LinearLayout(context);
		mFooterContainer.setOrientation(VERTICAL);
		mFooterContainer.setGravity(Gravity.CENTER_HORIZONTAL);
		mFooterContainer.setBackgroundResource(Res.drawable.list_item_mid_bg);
		mFooterContainer.setFocusable(true);
		mFooterContainer.setClickable(true);
		mFooterContainer.setOnClickListener(mMoreClickListener);
		addView(mFooterContainer);*/

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setDividerHorizontalMargin(int pixel) {
		mDividerHorizontalMargin = pixel;
	}

	public int getDataSize() {
		return mData == null ? 0 : mData.size();
	}

	/**
	 * Current list item count.
	 * 
	 * @Description : getItemCount
	 * @Author : Brant
	 * @CreateDate : 2013-1-31
	 * @return
	 */
	public int getItemCount() {
		return getDataSize();
	}

	public Map<String, ?> getData(int position) {
		if (position >= 0 && position < getDataSize()) {
			return mData.get(position);
		}
		return null;
	}

	public void setBindResource(int resource) {
		mResource = resource;
	}

	public void setDividerDrawable(Drawable drawable) {
		mDividerDrawable = drawable;
	}

	/**
	 * Sets the drawable to use as a divider between the list items.
	 * 
	 * @param resId
	 *            the resource identifier of the drawable to use as a divider.
	 */
	public void setDividerDrawable(int resId) {
		mDividerDrawable = getResources().getDrawable(resId);
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		mOnItemClickListener = listener;
	}
	
	public void setOnMoreClickListener(OnMoreClickListener listener) {
		mOnMoreClickListener = listener;
	}

	private void bindData() {
		int count = getItemCount(); 
		if (count > 0) {
			for (int position = 0; position < count; position++) {
				View child = getItemViewAt(position);
				if (child == null) {
					if (mDividerDrawable != null && position != 0) {
						addDividerView();
					}
					child = createViewFromResource(position, this, mResource);
					mItemsContainer.addView(child);
				} else {
					bindView(position, child);
				}
				child.setTag(position);
				child.setClickable(true);
				child.setFocusable(true);
				child.setFocusableInTouchMode(true);
				child.setOnClickListener(mOnClickListener);
				child.setOnFocusChangeListener(this);
			}
			
			// remove excess children
			int time = mItemsContainer.getChildCount() - 2 * count + 1;
			for (; time > 0; time--) {
				mItemsContainer
						.removeViewAt(mItemsContainer.getChildCount() - 1);
			}
		} else {
			mItemsContainer.removeAllViews();
		}
	}

	public void removeAllItems() {
		mItemsContainer.removeAllViews();
	}

	public List<? extends Map<String, ?>> getData() {
		return mData;
	}

	/**
	 * You should call setBindResource first.
	 * 
	 * @Description : setData
	 * @Author : Brant
	 * @CreateDate : 2013-1-31
	 * @param data
	 */
	public void setData(List<? extends Map<String, ?>> data) {
		if (mData != data) {
			if (mData != null) {
				mData.clear();
			}
		}
		mData = data;
		bindData();
	}

	public void notifyDataSetChanged() {
		bindData();
	}

	public void setViewBinder(HelpViewBinder viewBinder) {
		mViewBinder = viewBinder;
	}

	private View createViewFromResource(int position, ViewGroup parent,
			int resource) {
		View v = mInflater.inflate(resource, parent, false);
		bindView(position, v);
		return v;
	}

	private void bindView(int position, View view) {
		final Map<String, ?> dataSet = mData.get(position);
		if (dataSet != null && mViewBinder != null) {
			mViewBinder.bindViewData(position, view, dataSet);
		}
	}

	public void setViewImage(ImageView v, int value) {
		v.setImageResource(value);
	}

	public void setViewImage(ImageView v, String value) {
		try {
			v.setImageResource(Integer.parseInt(value));
		} catch (NumberFormatException nfe) {
			v.setImageURI(Uri.parse(value));
		}
	}

	public void setViewText(TextView v, String text) {
		v.setText(text);
	}

	public int getItemChildCount() {
		int children = mItemsContainer.getChildCount();

		// If we have dividers, then we will always have an odd number of
		// children: 1, 3, 5, ... and we want to convert that sequence to
		// this: 1, 2, 3, ...
		if (mDividerDrawable != null) {
			children = (children + 1) / 2;
		}
		return children;
	}

	public View getItemViewAt(int index) {
		int trueIndex;
		if (mDividerDrawable == null) {
			trueIndex = index;
		} else {
			trueIndex = 2 * index;
		}
		return mItemsContainer.getChildAt(trueIndex);
	}

	private void addDividerView() {
		if (mDividerDrawable == null) {
			throw new RuntimeException("Divider drawable null!");
		}
		ImageView divider = new ImageView(getContext());
		LinearLayout.LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT,
				mDividerDrawable.getIntrinsicHeight());
		lp.setMargins(mDividerHorizontalMargin, 0, mDividerHorizontalMargin, 0);
		divider.setLayoutParams(lp);
		if(Build.VERSION.SDK_INT > 15 ) {
		    divider.setBackground(mDividerDrawable); 
		} else {
		    divider.setBackgroundDrawable(mDividerDrawable);
		}
		mItemsContainer.addView(divider);
	}

	public void addFooterView(View view) {
		ImageView divider = new ImageView(getContext());
		LinearLayout.LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT,
				mDividerDrawable.getIntrinsicHeight());
		lp.setMargins(mDividerHorizontalMargin, 0, mDividerHorizontalMargin, 0);
		divider.setLayoutParams(lp);
		if(Build.VERSION.SDK_INT > 15 ) {
		    divider.setBackground(mDividerDrawable); 
		} else {
		    divider.setBackgroundDrawable(mDividerDrawable);
		}
		mFooterContainer.addView(divider);
		
		LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT ); 
		view.setLayoutParams(rlp);
		mFooterContainer.addView(view);
	}

	public void removeFooterView(View view) {
		mFooterContainer.removeView(view);
	}

	public static interface HelpViewBinder {
		void bindViewData(int position, View view, Map<String, ?> data);
	}

	public static interface OnItemClickListener {
		//void onItemClicked(int position, Map<String, ?> data, View view);
		void onTabSelectionChanged(int tabIndex, boolean clicked);
	}
	
	public static interface OnMoreClickListener {
		void onMoreClick();
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		if (v == this && hasFocus) {
			getItemViewAt(mSelectedTab).requestFocus();
			return;
		}

		if (hasFocus) {
			int i = 0;
			int numTabs = getItemChildCount();
			while (i < numTabs) {
				if (getItemViewAt(i) == v) {
					mSelectionChangedListener.onTabSelectionChanged(i, false);
					break;
				}
				i++;
			}
		}
	}
	
	public void setTabSelectionListener(OnTabSelectionChanged listener) {
		mSelectionChangedListener = listener;
	}
	
	public static interface OnTabSelectionChanged {
		void onTabSelectionChanged(int tabIndex, boolean clicked);
	}
}
