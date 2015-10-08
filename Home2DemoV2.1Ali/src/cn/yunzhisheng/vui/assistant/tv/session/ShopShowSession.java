/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : ShopShowSession.java
 * @ProjectName : vui_voicetv
 * @PakageName : cn.yunzhisheng.voicetv.session
 * @Author : Conquer
 * @CreateDate : 2013-12-20
 */
package cn.yunzhisheng.vui.assistant.tv.session;

import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import cn.yunzhisheng.vui.assistant.oem.RomControl;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Conquer
 * @CreateDate : 2013-12-20
 * @ModifiedBy : Conquer
 * @ModifiedDate: 2013-12-20
 * @Modified:
 * 2013-12-20: 实现基本功能
 */
public class ShopShowSession extends BaseSession {
	public static final String TAG = "ShopShowSession";
	private static String mTaobao = "http://s.taobao.com/search?q=";

	public ShopShowSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
	}

	@Override
	public void putProtocol(JSONObject jsonProtocol) {
		super.putProtocol(jsonProtocol);

		String keyword = getJsonValue(mDataObject, SessionPreference.KEY_KEYWORD);
		try {
			RomControl.enterControl(mContext, RomControl.ROM_BROWSER_URL, mTaobao + Uri.encode(keyword));
			mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_HIDE_WINDOW);
		} catch (Exception e) {
			e.printStackTrace();
			mAnswer = "很抱歉，找不到浏览器";
		}
		playTTS(mAnswer);
		addAnswerViewText(mAnswer);
	}

}
