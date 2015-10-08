/**
 * Copyright (c) 2012-2012 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : UserPreference.java
 * @ProjectName : V Plus 1.0
 * @PakageName : cn.yunzhisheng.vui.assistant.preference
 * @Author : Dancindream
 * @CreateDate : 2012-6-6
 */
package cn.yunzhisheng.vui.assistant.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.preference.PreferenceManager;

public class UserPreference {
	public static final String TAG = "UserPreference";

	public static final String KEY_COPY_DATA = "Key_Copy_Data";
	public static final String KEY_SOFT_VERSION = "Key_Soft_Version";
	public static final String KEY_ENABLE_MEDIA = "Key_Enable_Media";
	public static final String KEY_ENABLE_BLUETOOTH = "Key_Enable_Bluetooth";
	public static final String KEY_TTS_ENGINE = "Key_TTS_Engine";
	public static final String KEY_ENABLE_TTS = "Key_Enable_TTS";
	public static final String KEY_ENABLE_SCREEN_FLIP_STOP_TTS = "Key_Enable_Screen_Flip_Stop_TTS";
	public static final String KEY_ENABLE_TTS_CALL = "Key_Enable_TTS_Call";
	public static final String KEY_ENABLE_TTS_SMS_CONTACT = "Key_Enable_TTS_SMS_Contact";
	public static final String KEY_ENABLE_TTS_SMS_CONTENT = "Key_Enable_TTS_SMS_Content";
	public static final String KEY_CHECK_NEW_VERSION = "Key_Check_New_Version";
	public static final String KEY_FEEDBACK = "Key_Feedback";
	public static final String KEY_SINA_WEIBO_ACCESS_TOKEN = "Key_Sina_Weibo_Access_Token";
	public static final String KEY_SINA_WEIBO_EXPIRE_TIME_MILLIS = "Key_Sina_Weibo_Expire_Time_Millis";
	public static final String KEY_TECENT_WEIBO_ACCESS_TOKEN = "Key_Tencent_Weibo_Access_Token";
	public static final String KEY_TECENT_WEIBO_ACCESS_TOKEN_SECRET = "Key_Tencent_Weibo_Access_Token_Secret";
	public static final String	KEY_TECENT_WEIBO_ACCESS_OPENID = "Key_Tencent_Weibo_Access_Openid";
	public static final String KEY_RENREN_ACCESS_TOKEN = "Key_Renren_Access_Token";
	public static final String KEY_RENREN_ACCESS_TOKEN_SECRET = "Key_Renren_Access_Token_Secret";
	public static final String KEY_WEIBO_SINA_BIND = "Key_Weibo_Sina_Bind";
	public static final String KEY_WEIBO_TECENT_BIND = "Key_Weibo_Tencent_Bind";
	public static final String KEY_WEIBO_RENREN_BIND = "Key_Weibo_Renren_Bind";
	public static final String KEY_ENABLE_MEDIA_BUTTON = "KEY_Enable_Media_Button";

	public static final String Key_Today_Date = "Value_Today_Date";
	
	private static final String KEY_PHONE_STATE = "Key_Phone_State";
	private static final String KEY_CURRENT_VOLUME = "Key_Current_Volume";
	private static final String KEY_SPEECH_SMS = "Key_Speeching_Sms";
	private static final String KEY_CURRENT_MEDIA_VOLUME = "Key_Current_Media_Volume";
	public static final String KEY_ENABLE_TTS_SMS = "Key_Enable_Tts_Sms";
	public static final String KEY_SMS_MANAGER = "Key_Sms_Manager";
	public static final String KEY_MUSIC_PLAYING = "Key_Music_Play";
	public static final String KEY_RECOGNIZER_ENGINE_CHOOSE = "Key_TTS_Engine_Choose";
	
	public static final String KEY_AUTO_START_MIC_IN_LOOP = "KEY_AUTO_START_MIC_IN_LOOP";
	public static final String KEY_CONFIRM_BEFORE_CALL = "KEY_CONFIRM_BEFORE_CALL";
	
	public static final String KEY_ENABLE_CLEAN_VIEW_WHEN_SESSION_BEGIN = "KEY_ENABLE_CLEAN_VIEW_WHEN_SESSION_BEGIN";
	
	public static final int RECOGNIZER_OFFLINE_ONLINE = 0;
	public static final int RECOGNIZER_OFFLINE_ONLY = 1;
	public static final int RECOGNIZER_ONLINE_ONLY = 2;
	
	public static final int RECOGNIZER_ONLINE_ONLY_ARM6 = 0;
	public static final String KEY_EXIST_NATIVE_DATABASE = "EXIST_DB";
	public static final String KEY_VIRTUAL_KEY_SERVER_ACTIVE = "KEY_SERVER_ACTIVE";
	public static final String KEY_VOLUME = "KEY_VOLUME";
	public static final String KEY_CHANNEL = "KEY_CHANNEL";
	/*********************key setting value**************************/
	public static final String NEED_SET_KEY = "NEED_SET_KEY";
	public static final String VOICE_KEY = "VOICE_KEY";
	public static final String VERSION_KEY = "VERSION_KEY";
	
	public static final String TV_FRIENDLY_NAME_KEY = "TV_FRIENDLY_NAME_KEY";
	public static final String TV_FRIENDLY_NAME_DEFAULT = "我的电视";
	
	private SharedPreferences mPreferences;

	public UserPreference(Context context) {
		mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	public static SharedPreferences sharedUserPreference(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	public String getString(String key, String defValue) {
		return mPreferences.getString(key, defValue);
	}

	public void putString(String key, String value) {
		Editor editor = mPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public int getInt(String key, int defValue) {
		return mPreferences.getInt(key, defValue);
	}

	public void putInt(String key, int value) {
		Editor editor = mPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public boolean getBoolean(String key, boolean defValue) {
		return mPreferences.getBoolean(key, defValue);
	}

	public void putBoolean(String key, boolean value) {
		Editor editor = mPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public long getLong(String key, long defValue) {
		return mPreferences.getLong(key, defValue);
	}

	public void putLong(String key, long value) {
		Editor editor = mPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public float getFloat(String key, float defValue) {
		return mPreferences.getFloat(key, defValue);
	}

	public void putFloat(String key, float value) {
		Editor editor = mPreferences.edit();
		editor.putFloat(key, value);
		editor.commit();
	}
	
	public boolean getBluetoothMode() {
		return getBoolean(KEY_ENABLE_BLUETOOTH, false);
	}
	public void setBluetoothMode(Boolean f) {	
		putBoolean(KEY_ENABLE_BLUETOOTH, f);
	}
	
	public boolean getOrientationChangeValue() {
		return getBoolean(KEY_ENABLE_SCREEN_FLIP_STOP_TTS, false);
	}

	public boolean getTeleSpeech() {
		return getBoolean(UserPreference.KEY_ENABLE_TTS_CALL, false);
	}
	
	public boolean getSmsSpeechContact() {
		return  getBoolean(UserPreference.KEY_ENABLE_TTS_SMS_CONTACT , false);
	}

	public boolean getSmsSpeechContent() {
		return  getBoolean(UserPreference.KEY_ENABLE_TTS_SMS_CONTENT , false);
	}
	
	public void setPhoneState(boolean f) {
		putBoolean(KEY_PHONE_STATE, f);
	}
	public boolean getPhoneState() {
		return getBoolean(KEY_PHONE_STATE, false);
	}
	
	public int getTTSType() {
		return getInt(KEY_TTS_ENGINE, 0);
	}
	
	public void setRingerVolume(int volume) {
		putInt(KEY_CURRENT_VOLUME, volume);
	}
	public int getRingerVolume() {
		return getInt(KEY_CURRENT_VOLUME, 5);
	}
	
	public void setSpeechingSms(boolean f) {
		putBoolean(KEY_SPEECH_SMS, f);
	}
	public boolean getSpeechingSms() {
		return getBoolean(KEY_SPEECH_SMS, false);
	}

	public void setMediaVolume(int volume) {
		putInt(KEY_CURRENT_MEDIA_VOLUME, volume);
	}
	public int getMediaVolume() {
		return getInt(KEY_CURRENT_MEDIA_VOLUME, 9);
	}
	public boolean getSMSSpeech() {
		return getBoolean(KEY_ENABLE_TTS_SMS , false);
	}
	public boolean getMusicPlayAction() {
		return getBoolean(UserPreference.KEY_MUSIC_PLAYING, false);
	}
	
	public boolean getAutoStartMicInLoop() {
		return getBoolean(UserPreference.KEY_AUTO_START_MIC_IN_LOOP, false);
	}
	
	public void setAutoStartMicInLoop(boolean b) {
		putBoolean(UserPreference.KEY_AUTO_START_MIC_IN_LOOP, b);
	}
	
	public boolean getConfirmBeforeCall() {
		return getBoolean(UserPreference.KEY_CONFIRM_BEFORE_CALL, false);
	}
	
	public void setConfirmBeforeCall(boolean b) {
		putBoolean(UserPreference.KEY_CONFIRM_BEFORE_CALL, b);
	}
	
	public boolean getEnableCleanViewWhenSessionBegin() {
		return getBoolean(UserPreference.KEY_ENABLE_CLEAN_VIEW_WHEN_SESSION_BEGIN, false);
	}
	
	public void setEnableCleanViewWhenSessionBegin(boolean b) {
		putBoolean(UserPreference.KEY_ENABLE_CLEAN_VIEW_WHEN_SESSION_BEGIN, b);
	}
	
	public String getTodayDate() {
		return getString(Key_Today_Date, "");
	}
	
	public void setTodayDate(String s) {
		putString(Key_Today_Date, s);
	}
}
