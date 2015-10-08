/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName	: FullVoiceShowSession.java
 * @ProjectName	: vui_voicetv
 * @PakageName	: cn.yunzhisheng.vui.assistant.tv.session
 * @Author		: 2014-06-27
 * @CreateDate	: 2014-06-27
 */
package cn.yunzhisheng.vui.assistant.tv.session;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
/**
 * @Module		: 隶属模块名
 * @Comments	: 描述
 * @Author		: ALieen
 * @CreateDate	: 2014-06-27
 * @ModifiedBy	: 2014-06-27
 * @ModifiedDate: 2014-06-27
 * @Modified: 
 * 2014-06-27: 实现基本功能
 */
public class FullVoiceShowSession extends BaseSession {
	public static final String TAG = "FullVoiceShowSession";
	
	
	public FullVoiceShowSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
	}
	
	@Override
	public void putProtocol(JSONObject jsonProtocol) { 
		super.putProtocol(jsonProtocol);
		addQuestionViewText(mQuestion);
		addAnswerViewText(mAnswer);
		playTTS(mAnswer);
		//mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_HIDE_WINDOW);
	}

}
