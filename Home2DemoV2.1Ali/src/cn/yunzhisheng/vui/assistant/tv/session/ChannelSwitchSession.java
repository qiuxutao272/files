/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : ChannelSwitchSession.java
 * @ProjectName : vui_voicetv
 * @PakageName : cn.yunzhisheng.voicetv.session
 * @Author : Conquer
 * @CreateDate : 2013-12-19
 */
package cn.yunzhisheng.vui.assistant.tv.session;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Conquer
 * @CreateDate : 2013-12-19
 * @ModifiedBy : Conquer
 * @ModifiedDate: 2013-12-19
 * @Modified:
 * 2013-12-19: 实现基本功能
 */
public class ChannelSwitchSession extends BaseSession {
	public static final String TAG = "ChannelSwitchSession";

	public ChannelSwitchSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
	}

	@Override
	public void putProtocol(JSONObject jsonProtocol) {
		super.putProtocol(jsonProtocol);
		
		
		String channel = getJsonValue(mDataObject, SessionPreference.KEY_CHANNEL);
		String ttsString = "";
		ttsString = "暂不支持频道切换到" + channel;
		playTTS(ttsString);
		addAnswerViewText(ttsString);
	}
}
