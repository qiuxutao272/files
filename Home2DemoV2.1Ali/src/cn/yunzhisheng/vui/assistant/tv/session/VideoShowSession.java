package cn.yunzhisheng.vui.assistant.tv.session;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import cn.yunzhisheng.vui.assistant.oem.RomControl;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.common.util.LogUtil;

public class VideoShowSession extends BaseSession {
	private static final String TAG = "VideoShowSession";

	public VideoShowSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
	}

	@Override
	public void putProtocol(JSONObject jsonProtocol) {
		super.putProtocol(jsonProtocol);
		addQuestionViewText(mQuestion);

		String originType = getJsonValue(jsonProtocol, SessionPreference.KEY_ORIGIN_TYPE);
		String keyword = "";
		String url = "";

		if (originType.equals(SessionPreference.DOMAIN_VIDEO)) {
			JSONObject semanticObject = getJSONObject(jsonProtocol, SessionPreference.KEY_DATA);
			JSONObject intentObject = getJSONObject(semanticObject, SessionPreference.KEY_RESULT);

			keyword = getJsonValue(intentObject, SessionPreference.KEY_KEYWORD);
			url = getJsonValue(intentObject, SessionPreference.KEY_URL);
		} else {
			LogUtil.d(TAG, "video  --- originType:" + originType);
			return;
		}

		LogUtil.d(TAG, "video  --- keyword:" + keyword + ";url:" + url);

		String keyString = "相关视频讯息";
		if (keyword.equals("NEW")) {
			keyString = "最新视频讯息";
		} else if (keyword.equals("HOT")) {
			keyString = "热门视频讯息";
		}
		String ttsString = "已为您查找到" + keyString;
		playTTS(ttsString);
		RomControl.enterControl(mContext, RomControl.OEM_VIDEO_SHOW, keyword);
		mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_HIDE_WINDOW);
	}

}
