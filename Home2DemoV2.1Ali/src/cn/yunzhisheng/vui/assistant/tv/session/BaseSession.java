package cn.yunzhisheng.vui.assistant.tv.session;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import cn.yunzhisheng.common.JsonTool;
import cn.yunzhisheng.common.util.LogUtil;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.vui.assistant.tv.view.SessionContainer;

public abstract class BaseSession {
	private static final String TAG = "BaseSession";
	public boolean mIsNeedAddTextView;

	protected static final String[] MESSAGE_TRIM_END = new String[] { "。", "，" };
	protected Handler mSessionManagerHandler = null;
	protected Context mContext = null;

	protected String mDomain = "";
	protected String mType = "", mOriginType = "";
	protected String mQuestion = "", mAnswer = "";

	protected JSONObject mDataObject = null;

	private boolean mReleased = false;
	
	public BaseSession(Context context, Handler sessionManagerHandler) {
		mContext = context;
		mSessionManagerHandler = sessionManagerHandler;
	}

	public void putProtocol(JSONArray jsonProtocolArray) {
		try {
			if (jsonProtocolArray != null && jsonProtocolArray.length() > 0) {
				putProtocol(jsonProtocolArray.getJSONArray(0));
			}
		} catch (JSONException e) {
			showUnSupport();
			return;
		}
	}

	public void putProtocol(JSONObject jsonProtocol) {
		mDomain = JsonTool.getJsonValue(jsonProtocol, SessionPreference.KEY_DOMAIN, "");
		mType = JsonTool.getJsonValue(jsonProtocol, SessionPreference.KEY_TYPE, "");
		mOriginType = JsonTool.getJsonValue(jsonProtocol, SessionPreference.KEY_ORIGIN_TYPE, "");

		mDataObject = JsonTool.getJSONObject(jsonProtocol, SessionPreference.KEY_DATA);

		if (mDataObject != null) {
			mQuestion = JsonTool.getJsonValue(mDataObject, SessionPreference.KEY_QUESTION, "");
			mAnswer = JsonTool.getJsonValue(mDataObject, SessionPreference.KEY_ANSWER, "");
		}
		
		if(!TextUtils.isEmpty(mQuestion)){
			mQuestion = "“"+mQuestion+"”";
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

	}

	public void onTTSEnd() {
	}

	protected void showUnSupport() {
	}

	public void cancelSession() {
		mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_CANCEL_TALK);
		addAnswerViewText("操作已取消");
		mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_SESSION_CANCEL);
	}

	public boolean isReleased() {
		return mReleased;
	}

	public String getSessionDomain() {
		return mDomain;
	}

	public void release() {
		LogUtil.d(TAG, "release");
		mReleased = true;
		mContext = null;
	}

	protected void playTTS(String text) {
		Message msg = mSessionManagerHandler.obtainMessage(SessionPreference.MESSAGE_REQUEST_PLAY_TTS);
		msg.obj = text;
		mSessionManagerHandler.sendMessage(msg);
	}

	protected void cancelTTS() {
		mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_REQUEST_CANCEL_TTS);
	}

	protected void addQuestionViewText(String text) {
		Message msg = new Message();
		msg.what = SessionPreference.MESSAGE_ADD_QUESTION_TEXT;
		msg.obj = text;
		mSessionManagerHandler.sendMessage(msg);
	}

	protected void addAnswerView(View view) {
		if (view == null) {
			return;
		}
		SessionContainer.addViewNow(view.hashCode());
		Message msg = new Message();
		msg.what = SessionPreference.MESSAGE_ADD_ANSWER_VIEW;
		msg.obj = view;
		mSessionManagerHandler.sendMessage(msg);
	}

	protected void addAnswerViewText(String text) {
		Message msg = new Message();
		msg.what = SessionPreference.MESSAGE_ADD_ANSWER_TEXT;
		msg.obj = text;
		mSessionManagerHandler.sendMessage(msg);
	}

	protected void onUiProtocal(String protocal) {
		Message msg = new Message();
		msg.what = SessionPreference.MESSAGE_UI_OPERATE_PROTOCAL;
		msg.obj = protocal;
		mSessionManagerHandler.sendMessage(msg);
	}

	protected void sendResetTimerMessage() {
		mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_REQUEST_RESET_TIMER);
	}

	protected void sendCancelTimerMessage() {
		mSessionManagerHandler.sendEmptyMessage(SessionPreference.MESSAGE_REQUEST_CANCEL_TIMER);
	}

	protected void sendDelayTaskMessage(int key, int value) {
		Message msg = mSessionManagerHandler.obtainMessage(SessionPreference.MESSAGE_TASK_DELY);
		msg.arg1 = key;
		msg.arg2 = value;
		mSessionManagerHandler.sendMessage(msg);
	}

	protected void sendDelayTaskTTSEndMessage() {
		sendDelayTaskMessage(SessionManager.TASK_DELAY_KEY_TTS_END, SessionManager.TASK_DELAY_VALUE_RESET_TIMER);
	}

	protected String transDateTTS(String str) {
		if (TextUtils.isEmpty(str)) {
			return "";
		}

		String regEx = "([0-9]+)-([0-9]+)-([0-9]+)";
		String year = "";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		if (m.find()) {
			int size = m.groupCount();
			if (size > 1) {
				year = m.group(1);
			}
		}
		str = str.replaceAll("([0-9]+)-([0-9]+)-([0-9]+)", transNumberTTS(year) + "年$2月$3日");
		return str;
	}

	protected String transNumberTTS(String number) {
		if (TextUtils.isEmpty(number)) {
			return "";
		}
		String str1 = "0123456789";
		String str2 = "零一二三四五六七八九";

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < number.length(); i++) {
			char c = number.charAt(i);
			int index = str1.indexOf(c);
			if (index >= 0) {
				sb.append(str2.charAt(index));
			}
		}
		return sb.toString();
	}

	public static JSONObject getJSONObject(JSONObject jsonObj, String name) {
		return JsonTool.getJSONObject(jsonObj, name);
	}

	public static JSONObject getJSONObject(JSONArray jsonArr, int index) {
		return JsonTool.getJSONObject(jsonArr, index);
	}

	public static String getJsonValue(JSONObject json, String key) {
		return JsonTool.getJsonValue(json, key);
	}

	public static String getJsonValue(JSONObject json, String key, String defValue) {
		return JsonTool.getJsonValue(json, key, defValue);
	}

	public static boolean getJsonValue(JSONObject json, String key, boolean defValue) {
		return JsonTool.getJsonValue(json, key, defValue);
	}

	public static int getJsonValue(JSONObject json, String key, int defValue) {
		return JsonTool.getJsonValue(json, key, defValue);
	}

	public static JSONArray getJsonArray(JSONObject jsonObj, String key) {
		return JsonTool.getJsonArray(jsonObj, key);
	}
}
