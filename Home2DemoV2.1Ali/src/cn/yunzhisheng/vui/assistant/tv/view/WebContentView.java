/**
 * Copyright (c) 2012-2013 Yunzhisheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : WebContentView.java
 * @ProjectName : iShuoShuo2
 * @PakageName : cn.yunzhisheng.ishuoshuo.view
 * @Author : Brant
 * @CreateDate : 2013-3-25
 */
package cn.yunzhisheng.vui.assistant.tv.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import cn.yunzhisheng.vui.assistant.Res;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Brant
 * @CreateDate : 2013-3-25
 * @ModifiedBy : Brant
 * @ModifiedDate: 2013-3-25
 * @Modified:
 * 2013-3-25: 实现基本功能
 */
public class WebContentView extends FrameLayout implements ISessionView {
	public static final String TAG = "WebContentView";

	private String mUrl;

	private WebView mWebView;
	private View mViewLoading;
	private View mBtnViewWebSource;

	public WebContentView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(Res.layout.web_content_view, this, true);
		findViews();
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		mWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
//				if (newProgress == 100) {
//					mViewLoading.setVisibility(View.GONE);
//				} else {
					mViewLoading.setVisibility(View.VISIBLE);
//				}
			} 
		}); 
		mWebView.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				Log.i(TAG,  "---onKey : " +keyCode);
				 if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
	                	if (mWebView.canScrollVertically(1)) {
	                		mWebView.scrollBy(0, 100);                  
	                	}
	                } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
	                	if (mWebView.canScrollVertically(-1)) {
	                		mWebView.scrollBy(0, -100);   
	                	}
	                }
	                return true; 
			}
		});
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return false;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				Log.d(TAG, "onPageFinished url: "+url);
				super.onPageFinished(view, url);
				mViewLoading.setVisibility(View.GONE);
			}
		});
		
		 
		mBtnViewWebSource.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent view = new Intent(Intent.ACTION_VIEW);
				view.setData(Uri.parse(mUrl));
				v.getContext().startActivity(view);
			}
		});
	}

	public WebContentView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WebContentView(Context context) {
		this(context, null);
	}

	private void findViews() {
		mWebView = (WebView) findViewById(Res.id.webView);
		mViewLoading = findViewById(Res.id.progressBarLoading);
		mBtnViewWebSource = findViewById(Res.id.textViewViewWeb);
	}

	public void setUrl(String url) {
		mUrl = url;
		mWebView.loadUrl(url);
	};

	@Override
	public boolean isTemporary() {
		return false;
	}

    @Override
    public void requestSuperFocus() {
        
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
