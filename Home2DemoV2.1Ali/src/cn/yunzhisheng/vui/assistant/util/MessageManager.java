/**
 * Copyright (c) 2012-2014 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName	: MessageManager.java
 * @ProjectName	: vui_tv_assistant
 * @PakageName	: cn.yunzhisheng.vui.assistant.util
 * @Author		: Dancindream
 * @CreateDate	: 2014-6-18
 */
package cn.yunzhisheng.vui.assistant.util;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import cn.yunzhisheng.vui.assistant.oem.RomDevice;

/**
 * @Module		: 隶属模块名
 * @Comments	: 描述
 * @Author		: Dancindream
 * @CreateDate	: 2014-6-18
 * @ModifiedBy	: Dancindream
 * @ModifiedDate: 2014-6-18
 * @Modified: 
 * 2014-6-18: 实现基本功能
 */
public class MessageManager {
	public static final String TAG = "MessageManager";

	public static void sendMessage(Context context, String message, Bundle extras) {
		if (context == null) {
			return;
		}
		Intent intent = new Intent(message);
		intent.putExtras(extras);
		context.sendOrderedBroadcast(intent, null);
	}

	public static void sendMessage(Context context, String message) {
		if (context == null) {
			return;
		}
		Intent intent = new Intent(message);
		context.sendOrderedBroadcast(intent, null);
	}
	
	public static void sendPrivateMessage(Context context, String message, Bundle extras) {
		if (context == null) {
			return;
		}
		Intent intent = new Intent(message);
		intent.putExtras(extras);
		intent.addCategory(RomDevice.getAppPackageName(context));
		context.sendOrderedBroadcast(intent, null);
	}

	public static void sendPrivateMessage(Context context, String message) {
		if (context == null) {
			return;
		}
		Intent intent = new Intent(message);
		intent.addCategory(RomDevice.getAppPackageName(context));
		context.sendOrderedBroadcast(intent, null);
	}
	
	public static void sendCategoryMessage(Context context, String category, String message, Bundle extras) {
		if (context == null) {
			return;
		}
		Intent intent = new Intent(message);
		intent.putExtras(extras);
		intent.addCategory(category);
		context.sendOrderedBroadcast(intent, null);
	}

	public static void sendCategoryMessage(Context context, String category, String message) {
		if (context == null) {
			return;
		}
		Intent intent = new Intent(message);
		intent.addCategory(category);
		context.sendOrderedBroadcast(intent, null);
	}
	
	public static IntentFilter registIntentFilter(IntentFilter filter) {
		if (filter == null) {
			return null;
		}
		return filter;
	}
	
	public static IntentFilter registPrivateIntentFilter(IntentFilter filter, Context context) {
		if (filter == null) {
			return null;
		}
		if (context == null) {
			return filter;
		}
		filter.addCategory(RomDevice.getAppPackageName(context));
		filter.setPriority(10000);
		return filter;
	}
	
	public static IntentFilter registCategoryIntentFilter(IntentFilter filter, String category) {
		if (filter == null) {
			return null;
		}
		filter.addCategory(category);
		filter.setPriority(10000);
		return filter;
	}
}
