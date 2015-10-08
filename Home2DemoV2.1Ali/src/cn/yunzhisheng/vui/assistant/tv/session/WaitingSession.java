/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName	: WaitingSession.java
 * @ProjectName	: vui_assistant
 * @PakageName	: cn.yunzhisheng.ishuoshuo.session
 * @Author		: Dancindream
 * @CreateDate	: 2013-9-2
 */
package cn.yunzhisheng.vui.assistant.tv.session;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.vui.assistant.tv.view.WaitingContentView;
import cn.yunzhisheng.vui.assistant.tv.view.WaitingContentView.IWaitingContentViewListener;

/**
 * @Module		: 隶属模块名
 * @Comments	: 描述
 * @Author		: Dancindream
 * @CreateDate	: 2013-9-2
 * @ModifiedBy	: Dancindream
 * @ModifiedDate: 2013-9-2
 * @Modified: 
 * 2013-9-2: 实现基本功能
 */
public class WaitingSession extends BaseSession {
	public static final String TAG = "WaitingSession";
	private String mCancelProtocal = "";
	private WaitingContentView mWaitingContentView;
	

	private IWaitingContentViewListener mListener = new IWaitingContentViewListener() {

		@Override
		public void onCancel() {
			onUiProtocal(mCancelProtocal);
		}
	};
	
	/**
	 * @Author		: Dancindream
	 * @CreateDate	: 2013-9-2
	 * @param context
	 * @param sessionManagerHandler
	 * @param sessionViewContainer
	 */
	public WaitingSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
	}
	
	public void putProtocol(JSONObject jsonProtocol) {
		super.putProtocol(jsonProtocol);
		addQuestionViewText(mQuestion);
		/*mCancelProtocal = getJsonValue(mDataObject, SessionPreference.KEY_ON_CANCEL, "");
		
		if(mWaitingContentView == null){
			mWaitingContentView = new WaitingContentView(mContext);
			mWaitingContentView.setLisener(mListener);
		}
		mWaitingContentView.setTitle(mAnswer);
		
		addAnswerView(mWaitingContentView);*/
		playTTS(mAnswer);
	}
}
