/**
 * Copyright (c) 2012-2012 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName	: ITalkServicePresentorListener.java
 * @ProjectName	: V Plus 1.0
 * @PakageName	: cn.yunzhisheng.ishuoshuo.talk
 * @Author		: Dancindream
 * @CreateDate	: 2012-5-22
 */
package cn.yunzhisheng.vui.assistant.tv.talk;

import cn.yunzhisheng.tts.offline.basic.TTSPlayerListener;


public interface ITalkServicePresentorListener extends TTSPlayerListener {
	public static final String TAG = "ITalkServicePresentorListener";	
	// Server
	public void onConnectClient(String client);
	public void onDisconnectClient(String client);	
	// Talk
	public void onTalkInitDone();
	public void onTalkRecordingStart();
	public void onTalkStart();
	public void onTalkStop();
	public void onTalkCancel();
	public void onSessionProtocal(String protocal);
	
	public void onUpdateVolume(int volume);
	// Ex
	public void onConnectivityChanged();
	
	public void onMoveUp();
	
	public void onMoveDown();
	
	public void onErrorSingleTalk(String host,String talkStatus);
}
