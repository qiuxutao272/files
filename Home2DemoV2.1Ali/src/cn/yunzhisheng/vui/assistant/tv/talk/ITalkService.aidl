package cn.yunzhisheng.vui.assistant.tv.talk;

interface ITalkService {
	void startTalk();
	void stopTalk();
	void cancelTalk();
	void putCustomText(String text);
	void setProtocal(String protocal);
	void playTTS(String tts);
	void stopTTS();
}