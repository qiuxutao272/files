/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName	: UnsupportShowSession.java
 * @ProjectName	: vui_voicetv
 * @PakageName	: cn.yunzhisheng.vui.assistant.tv.session
 * @Author		: Conquer
 * @CreateDate	: 2013-12-23
 */
package cn.yunzhisheng.vui.assistant.tv.session;

import org.json.JSONObject;

import cn.yunzhisheng.voicetv.R;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * @Module		: 隶属模块名
 * @Comments	: 描述
 * @Author		: Conquer
 * @CreateDate	: 2013-12-23
 * @ModifiedBy	: Conquer
 * @ModifiedDate: 2013-12-23
 * @Modified: 
 * 2013-12-23: 实现基本功能
 */
public class UnsupportShowSession extends BaseSession {
	public static final String TAG = "UnsupportShowSession";
	
	
	public UnsupportShowSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void putProtocol(JSONObject jsonProtocol) { 
		super.putProtocol(jsonProtocol);
		//JSONObject resultObject = getJSONObject(mDataObject, SessionPreference.KEY_RESULT);
		//JSONObject protocalObject = getJSONObject(resultObject, SessionPreference.KEY_PROTOCAL);
		//String answer = getJsonValue(protocalObject, SessionPreference.KEY_UNSUPPROT_TEXT);
		
		mAnswer = mContext.getResources().getString(Res.string.server_errorAnswer);
//		addAnswerViewText(mAnswer);
//		-----------------xuhua
		String toast = mContext.getResources().getString(Res.string.listen_errorAnswer);
		Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
//		playTTS(mAnswer);
	}

}
