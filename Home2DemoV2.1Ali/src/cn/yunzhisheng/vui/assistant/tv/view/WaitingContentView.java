/**
 * Copyright (c) 2012-2012 Yunzhisheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : WaitingView.java
 * @ProjectName : iShuoShuo2
 * @PakageName : cn.yunzhisheng.ishuoshuo.view
 * @Author : Brant
 * @CreateDate : 2012-12-26
 */
package cn.yunzhisheng.vui.assistant.tv.view;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import cn.yunzhisheng.vui.assistant.Res;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Brant
 * @CreateDate : 2012-12-26
 * @ModifiedBy : Brant
 * @ModifiedDate: 2012-12-26
 * @Modified: 2012-12-26: 实现基本功能
 */
public class WaitingContentView extends FrameLayout implements ISessionView {
	public static final String TAG = "WaitingView";
	private TextView mTextViewTitle;
	private ImageView mImgBuffering;
	private Button mBtnCancel;

	private IWaitingContentViewListener mListener;
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (mListener != null) {
				mListener.onCancel();
			}
		}
	};

	public WaitingContentView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(Res.layout.waiting_content_view, this, true);
		mTextViewTitle = (TextView) findViewById(Res.id.textViewTitle);
		mImgBuffering = (ImageView) findViewById(Res.id.imageViewBuffering);
		mImgBuffering.post(new Runnable() {
			@Override
			public void run() {
				Drawable drawable = mImgBuffering.getDrawable();
				if (drawable != null && drawable instanceof AnimationDrawable) {
					((AnimationDrawable) drawable).start();
				}
			}
		});

		mBtnCancel = (Button) findViewById(Res.id.btnCancel);
		mBtnCancel.setFocusable(true);
		mBtnCancel.setOnClickListener(mOnClickListener);
	}

	public WaitingContentView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WaitingContentView(Context context) {
		this(context, null);
	}

	@Override
	public boolean isTemporary() {
		return true;
	}

	public void setTitle(String title) {
		mTextViewTitle.setText(title);
	}

	public void setTitle(int resid) {
		mTextViewTitle.setText(resid);
	}

	public void setLisener(IWaitingContentViewListener listener) {
		mListener = listener;
	}

	@Override
	public void requestSuperFocus() {
		mBtnCancel.requestFocus();
	}

	public interface IWaitingContentViewListener {
		public void onCancel();
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
