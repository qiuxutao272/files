package cn.yunzhisheng.vui.assistant.model;

public interface IWeiboClientListener {
	public void onAuthorSuccess();
	public void onAuthorError(int error);
	public void onAuthorCancel();
	
	public void onUpdateSuccess();
	public void onUpdateError(int error);
}
