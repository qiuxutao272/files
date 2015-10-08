package cn.yunzhisheng.vui.assistant.tv.session;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.vui.assistant.preference.UserPreference;

public class CommBaseSession extends BaseSession implements IUpdateSession {
	public static String TAG = "";

	public String mCancelProtocal = null;
	public String mOkProtocal = null;
	public JSONObject mJsonObject = null;
	protected UserPreference mUserPreference = new UserPreference(mContext);
	protected String mTtsText = "";

	protected void addTextCommonView() {
		if (mIsNeedAddTextView) {
			addSessionAnswerText(mQuestion);
			addSessionAnswerText(mAnswer);
		}
		playTTS(mQuestion);
		playTTS(mAnswer);
	}

	protected void editShowContent() {

	}

	CommBaseSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
		TAG = this.getClass().getName();
		mIsNeedAddTextView = true;
	}

	public void addSessionAnswerText(String ttsText) {
		addAnswerViewText(ttsText);
	}

	public void addSessionView(View view) {
		addAnswerView(view);
	}

	public void putProtocol(JSONObject jsonObject) {
		super.putProtocol(jsonObject);
		mJsonObject = getJSONObject(mDataObject, SessionPreference.KEY_RESULT);
		mCancelProtocal = getJsonValue(mJsonObject, SessionPreference.KEY_ON_CANCEL);
		mOkProtocal = getJsonValue(mJsonObject, "onOK");
		addTextCommonView();
	}

	@Override
	public void onTTSEnd() {
		super.onTTSEnd();
		if (mUserPreference.getAutoStartMicInLoop()) {
			mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_START_TALK);

		} else {

		}
	}

	public void requestStartTalk() {
		cancelTTS();
	}

	public void requestStopTalk() {
	}

	@Override
	public void editSession() {
		editShowContent();
	}

	@Override
	public void updateSession(JSONObject jsonObject) {

	}

}