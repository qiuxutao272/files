/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : SessionPreference.java
 * @ProjectName : vui_voice_center
 * @PakageName : cn.yunzhisheng.vui.voicecenter.preference
 * @Author : Conquer
 * @CreateDate : 2013-12-11
 */
package cn.yunzhisheng.vui.assistant.preference;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Conquer
 * @CreateDate : 2013-12-11
 * @ModifiedBy : Conquer
 * @ModifiedDate: 2013-12-11
 * @Modified:
 * 2013-12-11: 实现基本功能
 */
public class SessionPreferenceOfIntent {
	public static final String TAG = "SessionPreferenceOfIntent";

	public static final String KEY_KIND = "kind";
	public static final String KEY_TIME = "time";
	public static final String KEY_PARAM = "KEY_PARAM";
	public static final String INTENT_SETTVNAME = "cn.yunzhisheng.intent.settvname";
	
	public static final String KEY_MUSIC_BUFFER_PERCENT = "key_music_buffer_percent";
	public static final String KEY_MUSIC_PROGRESS_DURATION = "key_music_progress_duration";
	public static final String KEY_MUSIC_PROGRESS_TIME = "key_music_progress_time";
	
	public static final String INTENT_MUSIC_TO_CONTROL_START = "cn.yunzhisheng.intent.to.control.music.start";
	public static final String INTENT_MUSIC_TO_CONTROL_PLAY = "cn.yunzhisheng.intent.to.control.music.play";
	public static final String INTENT_MUSIC_TO_CONTROL_PAUSE = "cn.yunzhisheng.intent.to.control.music.pause";
	public static final String INTENT_MUSIC_TO_CONTROL_STOP = "cn.yunzhisheng.intent.to.control.music.stop";
	public static final String INTENT_MUSIC_TO_CONTROL_BUFFER = "cn.yunzhisheng.intent.to.control.music.buffer";
	public static final String INTENT_MUSIC_TO_CONTROL_PROGRESS = "cn.yunzhisheng.intent.to.control.music.progress";
	
	public static final String INTENT_MUSIC_FROM_CONTROL_PLAY = "cn.yunzhisheng.intent.from.control.music.play";
	public static final String INTENT_MUSIC_FROM_CONTROL_PAUSE = "cn.yunzhisheng.intent.from.control.music.pause";
	public static final String INTENT_MUSIC_FROM_CONTROL_STOP = "cn.yunzhisheng.intent.from.control.music.stop";
	public static final String INTENT_MUSIC_FROM_CONTROL_PREV = "cn.yunzhisheng.intent.from.control.music.prev";
	public static final String INTENT_MUSIC_FROM_CONTROL_NEXT = "cn.yunzhisheng.intent.from.control.music.next";
	
	public static final String INTENT_PLAY_CONTROL = "cn.yunzhisheng.voicetv.play.control";
	
	public static final String INTENT_CONNECT_CLIENT = "cn.yunzhisheng.intent.connect.client";
	public static final String INTENT_DISCONNECT_CLIENT = "cn.yunzhisheng.intent.disconnect.client";
}
