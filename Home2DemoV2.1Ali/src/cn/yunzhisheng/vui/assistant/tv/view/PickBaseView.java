/**
 * Copyright (c) 2012-2012 Mango(Shanghai) Co.Ltd. All right reserved.
 * @FileName : PickView.java
 * @ProjectName : iShuoShuo2
 * @PakageName : cn.yunzhisheng.ishuoshuo.view
 * @Author : Brant
 * @CreateDate : 2012-11-7
 */
package cn.yunzhisheng.vui.assistant.tv.view;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import cn.yunzhisheng.vui.assistant.Res;

public class PickBaseView extends LinearLayout implements ISessionView {
	public static final String TAG = "PickView";
	protected static final int PICK_VIEW_TAG_BUTTON_CANCEL = -1;
	private boolean mHasHeader;

	protected Button mBtnCancel;
	protected IPickListener mPickListener;
	protected LinearLayout mContainer;
	protected LayoutInflater mLayoutInflater;
	protected OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Integer tag = (Integer) v.getTag();
			onViewClick(tag);
		}
	};

	public PickBaseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Resources res = getResources();
		int margin = res.getDimensionPixelSize(Res.dimen.session_item_margin);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, margin, 0, margin);
		setLayoutParams(lp);

		mLayoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setOrientation(VERTICAL);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		mContainer = new LinearLayout(context);
		mContainer.setOrientation(VERTICAL);

		int height = res
				.getDimensionPixelSize(Res.dimen.pick_view_cancel_button_height);

		LayoutParams lpLayoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, height);
		lpLayoutParams.setMargins(0, margin, 0, 0);
		mBtnCancel = new Button(context);
		mBtnCancel.setLayoutParams(lpLayoutParams);
		mBtnCancel.setText(Res.string.cancel);
		addView(mContainer);
		addView(mBtnCancel);
		mBtnCancel.setTag(PICK_VIEW_TAG_BUTTON_CANCEL);
		mBtnCancel.setOnClickListener(mOnClickListener);
	}

	public PickBaseView(Context context) {
		this(context, null);
	}

	public void setHeader(View view) {
		mContainer.addView(view, 0);
		mHasHeader = true;
	}

	public void removeHeader() {
		if (mHasHeader) {
			mContainer.removeViewAt(0);
			mHasHeader = false;
		}
	}

	protected void addItem(View view) {
		mContainer.addView(view);
		view.setOnClickListener(mOnClickListener);
		view.setTag(mHasHeader ? mContainer.getChildCount() - 2 : mContainer
				.getChildCount() - 1);
	}

	public int getItemCount() {
		if (mHasHeader) {
			return mContainer.getChildCount() - 1;
		} else {
			return mContainer.getChildCount();
		}
	}

	public View getItem(int index) {
		int count = getItemCount();
		if (index >= 0 && index < count) {
			return mHasHeader ? mContainer.getChildAt(index + 1) : mContainer
					.getChildAt(index);
		}
		return null;
	}

	public IPickListener getPickListener() {
		return mPickListener;
	}

	public void setPickListener(IPickListener mPickListener) {
		this.mPickListener = mPickListener;
	}

	@Override
	public boolean isTemporary() {
		return true;
	}

	protected void onViewClick(int tag) {
		if (tag == PICK_VIEW_TAG_BUTTON_CANCEL) {
			if (mPickListener != null) {
				mPickListener.onPickCancel();
			}
			return;
		} else if (tag >= 0) {
			if (mPickListener != null) {
				mPickListener.onItemPicked(tag);
			}
			return;
		}
	}

	@Override
	public void requestSuperFocus() {
		if (mContainer.getChildCount() >= 0) {
			mContainer.getChildAt(0).requestFocus();
		}
	}

	public static interface IPickListener {
		void onItemPicked(int position);

		void onPickCancel();
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
