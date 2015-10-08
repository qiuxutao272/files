package cn.yunzhisheng.vui.assistant.model;

import java.io.Serializable;
public class DeviceDisplay implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final String TAG = "Device";
	
	private String ip;
	private String deviceName;
	private boolean talkEnable;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public boolean isTalkEnable() {
		return talkEnable;
	}
	public void setTalkEnable(boolean talkEnable) {
		this.talkEnable = talkEnable;
	}
	
}
