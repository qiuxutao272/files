package cn.yunzhisheng.vui.assistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import cn.yunzhisheng.vui.assistant.model.BaseWeiboClient;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;

public class WeiBoActivity extends Activity {
	static public BaseWeiboClient mCurrWeiboClient = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(Res.layout.activity_weibo);
		WeiBoActivity.mCurrWeiboClient.mContext = this;
		WeiBoActivity.mCurrWeiboClient.authorize();

	}

	@Override
	protected void onResume() {
		super.onResume();

	   }
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode,requestCode,data);
		switch (requestCode) {
		case SessionPreference.REQUEST_CODE_WEIBO_SINA_AUTHOR:
		case SessionPreference.REQUEST_CODE_WEIBO_TENCENT_AUTHOR: {
			WeiBoActivity.mCurrWeiboClient.authorizeCallBack(requestCode, resultCode, data);
		}
		}
	}
}
