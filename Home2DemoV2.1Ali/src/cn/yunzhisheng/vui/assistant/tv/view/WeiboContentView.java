/**
 * Copyright (c) 2012-2012 Mango(Shanghai) Co.Ltd. All right reserved.
 * @FileName : WeiboComponent.java
 * @ProjectName : iShuoShuo2
 * @PakageName : cn.yunzhisheng.ishuoshuo.view
 * @Author : Brant
 * @CreateDate : 2012-11-12
 */
package cn.yunzhisheng.vui.assistant.tv.view;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import cn.yunzhisheng.vui.assistant.Res;

public class WeiboContentView extends FrameLayout implements ISessionView {
	public static final String TAG = "WeiboComponent";
	private TextView mTextViewWeibo,mEditTextWeiboContentLength;
	private EditText mEditTextWeiboInput;
	private IWeiboContentViewListener mListener;
	private Button mBtnCancel, mBtnOk, mBtnClearWeiboContent;
	private ImageView mImageViewWeiboStatus, mImageViewWeiboImage;
	
	private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			int id = v.getId();
			switch (id) {
			case Res.id.btnClearWeiboContent:
				if(hasFocus == true) {			
					mBtnClearWeiboContent.setBackgroundResource(Res.drawable.view_focused_bk);
				}else{
					mBtnClearWeiboContent.setBackgroundResource(android.R.color.transparent);
				}
				break;
			case Res.id.btnSendWeibo:
				if(hasFocus == true) {			
					mBtnOk.setBackgroundResource(Res.drawable.view_focused_bk);
				}else{
					mBtnOk.setBackgroundResource(android.R.color.transparent);
				}
				break;
			case Res.id.btnCancelWeibo:
				if(hasFocus == true) {			
					mBtnCancel.setBackgroundResource(Res.drawable.view_focused_bk);
				}else{
					mBtnCancel.setBackgroundResource(android.R.color.transparent);
				}
				break;
			default:
				break;
			}
			
		}
	};
	
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			if(id == Res.id.btnClearWeiboContent){
//				mBtnClearWeiboContent.setBackgroundResource(Res.drawable.list_item_up_bg);
				if (mListener != null) {
					mListener.onClearMessage();
				}
			}else if(id == Res.id.btnSendWeibo){
//				mBtnOk.setBackgroundResource(Res.drawable.view_press_bk);
				if (mListener != null) {
					mListener.onOk();
				}
			}else if(id == Res.id.btnCancelWeibo){
//				mBtnCancel.setBackgroundResource(Res.drawable.view_press_bk);
				if (mListener != null) {
					mListener.onCancel();
				}
			}
//			switch (v.getId()) {
//			case Res.id.btnClearWeiboContent:
//				if (mListener != null) {
//					mListener.onClearMessage();
//				}
//				break;
//			case Res.id.btnSendWeibo:
//				if (mListener != null) {
//					mListener.onOk();
//				}
//				break;
//			case Res.id.btnCancelWeibo:
//				if (mListener != null) {
//					mListener.onCancel();
//				}
//				break;
//			}
		}
	};

	public WeiboContentView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WeiboContentView(Context context) {
		this(context, null);
	}

	public WeiboContentView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(Res.layout.weibo_content_view, null);
		addView(view);
		findViews();
		setListener();
	}

	private void findViews() {
		mTextViewWeibo = (TextView) findViewById(Res.id.textViewWeibo);
		mEditTextWeiboContentLength = (TextView) findViewById(Res.id.editTextWeiboContentLength);
		mBtnClearWeiboContent = (Button) findViewById(Res.id.btnClearWeiboContent);
		mEditTextWeiboInput = (EditText) findViewById(Res.id.editTextWeiboInput);
		mEditTextWeiboInput.setEnabled(false);
		mEditTextWeiboInput.setFocusable(false);
		mEditTextWeiboInput.setFocusableInTouchMode(false);
		//mEditTextWeiboInput.setMovementMethod(ScrollingMovementMethod.getInstance());  
		mBtnOk = (Button) findViewById(Res.id.btnSendWeibo);
		mBtnCancel = (Button) findViewById(Res.id.btnCancelWeibo);
	}

	private void setListener() {
		mBtnClearWeiboContent.setOnClickListener(mOnClickListener);
		mBtnCancel.setOnClickListener(mOnClickListener);
		mBtnOk.setOnClickListener(mOnClickListener);
	}

	public void setTitle(String title) {
		mTextViewWeibo.setText(title);
	}

	public void setTitle(int titleRes) {
		mTextViewWeibo.setText(titleRes);
	}

	public void setInputError(CharSequence error) {
		mEditTextWeiboInput.setError(error);
	}

	public void setMessage(String msg) {
		/*mEditTextWeiboInput.setText(msg);
		int length = mEditTextWeiboInput.getText().length();
		mEditTextWeiboInput.setSelection(length);*/
		
		
		mEditTextWeiboInput.setText(msg);
		int length = mEditTextWeiboInput.getText().length();
		mEditTextWeiboInput.setSelection(length);
		int left = 140 - length;
		mEditTextWeiboContentLength.setText(String.valueOf(left));
	}

	public String getMessage() {
		return mEditTextWeiboInput.getText().toString();
	}

	public IWeiboContentViewListener getListener() {
		return mListener;
	}

	public void setListener(IWeiboContentViewListener mListener) {
		this.mListener = mListener;
	}

	@Override
	public boolean isTemporary() {
		return true;
	}

	@Override
	public void requestSuperFocus() {
		mBtnOk.requestFocus();
		/*mBtnOk.setBackgroundResource(Res.drawable.view_focus_bk);
		mBtnClearWeiboContent.setOnFocusChangeListener(mOnFocusChangeListener);
		mBtnCancel.setOnFocusChangeListener(mOnFocusChangeListener);
		mBtnOk.setOnFocusChangeListener(mOnFocusChangeListener);*/
		
	}
	
	public static interface IWeiboContentViewListener {
		public void onBeginEdit();

		public void onEndEdit(String msg);

		public void onCancel();

		public void onOk();

		public void onImageOperation(int operation);

		public void onClearMessage();
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
