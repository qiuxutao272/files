package cn.yunzhisheng.vui.assistant.tv.session;

import org.json.JSONObject;

public abstract interface IUpdateSession {
	void updateSession(JSONObject jsonObject);
	void editSession();
}
