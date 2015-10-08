/**
 * Copyright (c) 2012-2012 Mango(Shanghai) Co.Ltd. All right reserved.
 * @FileName : SessionContainer.java
 * @ProjectName : iShuoShuo2
 * @PakageName : cn.yunzhisheng.ishuoshuo.view
 * @Author : Brant
 * @CreateDate : 2012-11-10
 */
package cn.yunzhisheng.vui.assistant.tv.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.yunzhisheng.common.util.LogUtil;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.assistant.oem.RomControl;

public class SessionContainer extends LinearLayout implements OnFocusListener {
	public static final String TAG = "SessionContainer";
	private final static int SESSION_TYPE_INVALID = -1;
	private final static int SESSION_TYPE_QUESTION = 1;
	private final static int SESSION_TYPE_ANSWER = 2;
	private int mLastestSessionType = SESSION_TYPE_INVALID;
	private String mLastestSession;
	private LinearLayout mSessionContainer;
	private LayoutInflater mLayoutInflater = null;
	private boolean mRequestFullScroll = true;
	private boolean mScrollable = false;

	private int mCurrentY = 0;
	private Context mContext;

	private static ArrayList<Integer> mViewShowTimeList = new ArrayList<Integer>();

	public static void addViewNow(int view_hashCode) {
		mViewShowTimeList.add(view_hashCode);
	}

	public SessionContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mSessionContainer = new LinearLayout(context);
		mSessionContainer.setOrientation(LinearLayout.VERTICAL);
		mSessionContainer.setLayoutParams(new LinearLayout.LayoutParams(
			LinearLayout.LayoutParams.MATCH_PARENT,
			LinearLayout.LayoutParams.WRAP_CONTENT));
		Resources res = getResources();

		int left = res.getDimensionPixelSize(Res.dimen.session_padding_left);
		int right = res.getDimensionPixelSize(Res.dimen.session_padding_right);
		int top = res.getDimensionPixelSize(Res.dimen.session_padding_top);
		int bottom = res.getDimensionPixelSize(Res.dimen.session_padding_bottom);
		mSessionContainer.setPadding(left, top, right, bottom);

		addView(mSessionContainer);
		mContext = context;

		mViewShowTimeList.clear();
	}

	public SessionContainer(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SessionContainer(Context context) {
		this(context, null);
	}

	/*
	 * @Override
	 * protected void onLayout(boolean changed, int l, int t, int r, int b) {
	 * super.onLayout(changed, l, t, r, b);
	 * if (mRequestFullScroll) {
	 * mRequestFullScroll = false;
	 * fullScroll(ScrollView.FOCUS_DOWN);
	 * }
	 * }
	 */

	/*
	 * @Override
	 * public boolean onTouchEvent(MotionEvent ev) {
	 * switch (ev.getAction()) {
	 * case MotionEvent.ACTION_DOWN:
	 * if (mScrollable) {
	 * return super.onTouchEvent(ev);
	 * }
	 * return false;
	 * default:
	 * return super.onTouchEvent(ev);
	 * }
	 * }
	 */

	/*
	 * @Override
	 * public boolean onInterceptTouchEvent(MotionEvent ev) {
	 * if (mScrollable) {
	 * return super.onInterceptTouchEvent(ev);
	 * }
	 * return false;
	 * }
	 */

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		// if (mCurrentY > 0) {
		// scrollTo(getScrollX(), mCurrentY);
		// }
	}

	// @Override
	// protected void onVisibilityChanged(View changedView, int visibility) {
	// super.onVisibilityChanged(changedView, visibility);
	// if (visibility == View.VISIBLE) {
	// requestSuperFocus();
	// }
	// }

	public void setScrollingEnabled(boolean enabled) {
		mScrollable = enabled;
	}

	public LinearLayout getContentView() {
		return mSessionContainer;
	}

	public void clearTemporaryViews() {
		LogUtil.d(TAG, "clearTemporaryViews");
		mViewShowTimeList.clear();
		for (int i = 0; i < mSessionContainer.getChildCount(); i++) {
			View child = mSessionContainer.getChildAt(i);
			if (child instanceof ISessionView && ((ISessionView) child).isTemporary()) {
				mSessionContainer.removeViewAt(i);
			}
		}
	}

	public void removeAllSessionViews() {
		mLastestSession = "";
		mViewShowTimeList.clear();
		mSessionContainer.removeAllViews();
	}

	public void removeSessionView(View view) {
		mViewShowTimeList.clear();
		mSessionContainer.removeView(view);
	}

	public void addSessionView(View view) {
		addSessionView(view, true);
	}

	public void addSessionView(View view, boolean fullScroll) {
		if (view == null) {
			return;
		}
		if (view instanceof ISessionView) {
			Integer hashCode = view.hashCode();
			if (mViewShowTimeList.contains(hashCode)) {
				mViewShowTimeList.remove((Integer) hashCode);
			} else {
				return;
			}
		}
		mRequestFullScroll = fullScroll;
		mSessionContainer.addView(view);
		requestSuperFocus();
	}

	public void addQustionView(String question) {
		if (TextUtils.isEmpty(question)
			|| (mLastestSessionType == SESSION_TYPE_QUESTION && question.equals(mLastestSession))) {
			return;
		}
		mLastestSessionType = SESSION_TYPE_QUESTION;
		mLastestSession = question;

		TextView tv2 = (TextView) mLayoutInflater.inflate(Res.layout.session_question_view, mSessionContainer, false);
		tv2.setText(question);
		addSessionView(tv2, true);
	}

	public void addAnswerView(String answer) {
		LogUtil.d(TAG, "addAnswerView:answer=" + answer);
		if (TextUtils.isEmpty(answer) || (mLastestSessionType == SESSION_TYPE_ANSWER && answer.equals(mLastestSession))) {
			return;
		}
		mLastestSessionType = SESSION_TYPE_ANSWER;
		mLastestSession = answer;
		View v = mLayoutInflater.inflate(Res.layout.session_answer_view, mSessionContainer, false);
		TextView tv1 = (TextView) v.findViewById(Res.id.textViewSessionAnswer);
		tv1.setText(answer);
		addSessionView(v);
	}

	public void addAnswerViewEx(String text, String imgURL, String imgAlt, String url, String urlAlt, boolean openNow) {
		LogUtil.d(TAG, "addAnswerViewEx text=" + text);
		if (TextUtils.isEmpty(text) && TextUtils.isEmpty(imgURL) && TextUtils.isEmpty(url)) {
			return;
		}

		url = TextUtils.isEmpty(url) ? "" : url.replaceAll(" ", "%20");

		if (!TextUtils.isEmpty(text) && TextUtils.isEmpty(imgURL)) {
			if (TextUtils.isEmpty(url)) {
				addAnswerView(text);
				return;
			} else if (openNow) {
				addAnswerView(text);
				RomControl.enterControl(mContext, RomControl.ROM_BROWSER_URL, url);
				return;
			}
		}

		if (!TextUtils.isEmpty(text) && (mLastestSessionType == SESSION_TYPE_ANSWER && text.equals(mLastestSession))) {
			return;
		}
		mLastestSessionType = SESSION_TYPE_ANSWER;
		mLastestSession = text;

		if (openNow) {
			RomControl.enterControl(mContext, RomControl.ROM_BROWSER_URL, url);
		}
	}

	public void addAnswerViewEx(String text, String imgURL, String imgAlt, String url, String urlAlt) {
		addAnswerViewEx(text, imgURL, imgAlt, url, urlAlt, false);
	}

	public boolean hasContent() {
		return mSessionContainer.getChildCount() > 0;
	}

	/*
	 * @Override
	 * protected void onScrollChanged(int l, int t, int oldl, int oldt) {
	 * super.onScrollChanged(l, t, oldl, oldt);
	 * mCurrentY = t;
	 * }
	 */

	@Override
	public void requestSuperFocus() {
		LogUtil.d(TAG, "requestSuperFocus count = " + mSessionContainer.getChildCount());
		View lastChild = mSessionContainer.getChildAt(mSessionContainer.getChildCount() - 1);
		// View lastChild =
		// mSessionContainer.getChildAt(mSessionContainer.getChildCount());
		if (lastChild instanceof OnFocusListener) {
			((OnFocusListener) lastChild).requestSuperFocus();
		}
	}

}
