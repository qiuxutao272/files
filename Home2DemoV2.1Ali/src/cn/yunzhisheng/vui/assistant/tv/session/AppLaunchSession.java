/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : AppLaunchSession.java
 * @ProjectName : vui_assistant
 * @PakageName : cn.yunzhisheng.ishuoshuo.session
 * @Author : Dancindream
 * @CreateDate : 2013-9-6
 */
package cn.yunzhisheng.vui.assistant.tv.session;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import cn.yunzhisheng.vui.assistant.oem.RomControl;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Dancindream
 * @CreateDate : 2013-9-6
 * @ModifiedBy : Dancindream
 * @ModifiedDate: 2013-9-6
 * @Modified:
 * 2013-9-6: 实现基本功能
 */
public class AppLaunchSession extends BaseSession {
	public static final String TAG = "AppLaunchSession";

	/**
	 * @Author : Dancindream
	 * @CreateDate : 2013-9-6
	 * @param context
	 * @param sessionManagerHandler
	 */
	public AppLaunchSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
	}

	public void putProtocol(JSONObject jsonProtocol) {
		super.putProtocol(jsonProtocol);

		JSONObject resultObject = getJSONObject(mDataObject, "result");

		String packageName = getJsonValue(resultObject, "package_name", "");
		String className = getJsonValue(resultObject, "class_name", "");

		String url = getJsonValue(resultObject, "url", "");
		addQuestionViewText(mQuestion);
		if (packageName != null && !"".equals(packageName) && className != null && !"".equals(className)) {
			addAnswerViewText(mAnswer);
			playTTS(mAnswer);
			RomControl.enterControl(mContext, RomControl.ROM_APP_LAUNCH, packageName, className);
			mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_HIDE_WINDOW);
		} else if (url != null && !"".equals(url)) {
			addAnswerViewText(mAnswer);
			playTTS(mAnswer);
			RomControl.enterControl(mContext, RomControl.ROM_BROWSER_URL, url);
			mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_HIDE_WINDOW);
		} else {
			addAnswerViewText("您找的应用不存在");
			playTTS("您找的应用不存在");
		}

	}
}
