package cn.yunzhisheng.vui.assistant.tv;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import cn.yunzhisheng.common.util.LogUtil;
import cn.yunzhisheng.vui.assistant.preference.PrivatePreference;
import cn.yunzhisheng.vui.assistant.util.CrashHandler;

public class MainApplication extends Application {
	private static final String TAG = "MainApp";
	public static int mChatNum = 0;
	public static int mNoVoiceNum = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		// 捕获错误日志
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);

		init();
		PrivatePreference.init(this);
	}

	private void init() {
	}

	@Override
	public void onTerminate() {
		LogUtil.d(TAG, "onTerminate");
		super.onTerminate();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	public static String getAppVersionName(Context context) {
		String versionName = "";

		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

			versionName = packageInfo.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return versionName;
	}
}
