/**
 * Copyright (c) 2012-2013 Yunzhisheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : PackageUtil.java
 * @ProjectName : Voizard
 * @PakageName : cn.yunzhisheng.voizard.utils
 * @Author : Brant
 * @CreateDate : 2013-5-29
 */
package cn.yunzhisheng.vui.assistant.util;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Brant
 * @CreateDate : 2013-5-29
 * @ModifiedBy : Brant
 * @ModifiedDate: 2013-5-29
 * @Modified:
 * 2013-5-29: 实现基本功能
 */
public class PackageUtil {
	private static final String TAG = "PackageUtil";

	public static List<String> getLauncherPackages(Context context) {
		List<String> packages = new ArrayList<String>();
		PackageManager packageManager = context.getPackageManager();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
		for (ResolveInfo info : resolveInfo) {
			packages.add(info.activityInfo.packageName);
		}
		return packages;
	}

	public static String getCurrentTasks(Context context) {
		String currentTask="";
		 ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		 List<RecentTaskInfo> recentTasks=am.getRecentTasks(1, 0);
		 for(RecentTaskInfo info:recentTasks){
			 currentTask=info.baseIntent.getComponent().getPackageName();
			 break;
		 }
		return currentTask;
	}

	public static boolean isHome(Context context, List<String> launchers) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> rti = activityManager.getRunningTasks(1);
		return launchers.contains(rti.get(0).topActivity.getPackageName());
	}

	public static String getAppVersionName(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public static int getAppVersionCode(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;
	}

}
