package cn.yunzhisheng.vui.assistant.tv.session;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import cn.yunzhisheng.vui.assistant.oem.RomControl;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.common.util.LogUtil;

public class AppExitSession extends BaseSession {
	private static final String TAG = "AppExitSession";

	public AppExitSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
	}

	@Override
	public void putProtocol(JSONObject jsonProtocol) {
		super.putProtocol(jsonProtocol);
		addQuestionViewText(mQuestion);

		String originType = getJsonValue(jsonProtocol, SessionPreference.KEY_ORIGIN_TYPE);
		String className = "";
		String packageName = "";
		String mUrl = "";
		if (originType.equals(SessionPreference.DOMAIN_APP)) {
			JSONObject semanticObject = getJSONObject(jsonProtocol, SessionPreference.KEY_DATA);
			JSONObject intentObject = getJSONObject(semanticObject, SessionPreference.KEY_RESULT);
			className = getJsonValue(intentObject, SessionPreference.KEY_CLASS_NAME);
			packageName = getJsonValue(intentObject, SessionPreference.KEY_PACKAGE_NAME);
			mUrl = getJsonValue(jsonProtocol, "url");
		} else {
			LogUtil.d(TAG, "app  --- originType:" + originType);
			return;
		}
		if (packageName != null && !packageName.equals("") && className != null && !packageName.equals("")) {
			
			addAnswerViewText(mAnswer);
			playTTS(mAnswer);
			RomControl.enterControl(mContext, RomControl.ROM_APP_EXIT, packageName);
		}else {
			if (mUrl != null && !mUrl.equals("")) {
				RomControl.enterControl(mContext, RomControl.ROM_BROWSER_URL, mUrl);
				mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_HIDE_WINDOW);
			}else {
				addAnswerViewText("您找的应用不存在");
				playTTS("您找的应用不存在");
			}
			 
		}
	}

}
