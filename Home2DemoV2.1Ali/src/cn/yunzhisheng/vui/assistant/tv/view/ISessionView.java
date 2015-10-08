/**
 * Copyright (c) 2012-2012 Mango(Shanghai) Co.Ltd. All right reserved.
 * @FileName : ISessionComponent.java
 * @ProjectName : iShuoShuo2
 * @PakageName : cn.yunzhisheng.ishuoshuo.view
 * @Author : Brant
 * @CreateDate : 2012-11-10
 */
package cn.yunzhisheng.vui.assistant.tv.view;

public interface ISessionView extends OnFocusListener {
	public boolean isTemporary();
	public void release();
}
