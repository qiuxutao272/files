/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName	: KeyUtil.java
 * @ProjectName	: vui_voicetv
 * @PakageName	: cn.yunzhisheng.voicetv.util
 * @Author		: Conquer
 * @CreateDate	: 2013-11-5
 */
package cn.yunzhisheng.vui.assistant.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.KeyEvent;

/**
 * @Module		: 隶属模块名
 * @Comments	: 描述
 * @Author		: Conquer
 * @CreateDate	: 2013-11-5
 * @ModifiedBy	: Conquer
 * @ModifiedDate: 2013-11-5
 * @Modified: 
 * 2013-11-5: 实现基本功能
 */
public class KeyUtil {
	public static final String TAG = "KeyUtil"; 
	
	public static int voiceKey=KeyEvent.KEYCODE_F5;
	
	public static boolean isServiceExisted(Context context, String className) {
	        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);

	        if(!(serviceList.size() > 0)) {
	            return false;
	        }

	        for(int i = 0; i < serviceList.size(); i++) {
	            RunningServiceInfo serviceInfo = serviceList.get(i);
	            ComponentName serviceName = serviceInfo.service;

	            if(serviceName.getClassName().equals(className)) {
	                return true;
	            }
	        } 
	        return false;
	    }
	
	
	public static boolean hasAppClient(Context context) {
		PackageInfo packageInfo;
		try {
			//cn.yunzhisheng.virtual_key
			packageInfo = context.getPackageManager().getPackageInfo("cn.yunzhisheng.virtual_key", 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
		}
		if (packageInfo == null) {
			return false;
		} else {
			return true;
		}
	}
}
