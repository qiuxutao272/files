/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : WebShowSession.java
 * @ProjectName : vui_assistant
 * @PakageName : cn.yunzhisheng.ishuoshuo.session
 * @Author : Dancindream
 * @CreateDate : 2013-9-3
 */
package cn.yunzhisheng.vui.assistant.tv.session;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import cn.yunzhisheng.vui.assistant.oem.RomApp;
import cn.yunzhisheng.vui.assistant.oem.RomControl;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Dancindream
 * @CreateDate : 2013-9-3
 * @ModifiedBy : Dancindream
 * @ModifiedDate: 2013-9-3
 * @Modified:
 * 2013-9-3: 实现基本功能
 */
public class WebShowSession extends BaseSession {
	public static final String TAG = "WebShowSession";
	private String mUrl = "";

	/**
	 * @Author : Dancindream
	 * @CreateDate : 2013-9-3
	 * @param context
	 * @param sessionManagerHandler
	 */
	public WebShowSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
	}

	public void putProtocol(JSONObject jsonProtocol) {
		super.putProtocol(jsonProtocol);

		JSONObject resultObject = getJSONObject(mDataObject, "result");

		/*mUrl = getJsonValue(resultObject, SessionPreference.KEY_URL, "");

		addQuestionViewText(mQuestion);
		if(mUrl.length() > 0){
			try {
				RomControl.enterControl(mContext, RomControl.ROM_BROWSER_URL, mUrl);
				mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_HIDE_WINDOW);
			} catch (Exception e) {
				e.printStackTrace();
				mAnswer = "很抱歉，找不到浏览器";
			}
		}else{
			mAnswer = "这个问题太难了，我不知道该怎么回答";
		}*/
		
		mAnswer = "这个问题太难了，我不知道该怎么回答";
		addQuestionViewText(mQuestion);
		addAnswerViewText(mAnswer);
		playTTS(mAnswer);
	}
}
