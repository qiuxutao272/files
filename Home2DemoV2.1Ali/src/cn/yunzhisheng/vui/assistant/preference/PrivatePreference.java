/**
 * Copyright (c) 2012-2012 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : PrivatePreference.java
 * @ProjectName : V Plus 1.0
 * @PakageName : cn.yunzhisheng.vui.assistant.preference
 * @Author : Dancindream
 * @CreateDate : 2012-5-28
 */
package cn.yunzhisheng.vui.assistant.preference;

import java.io.File;

import android.content.Context;
import cn.yunzhisheng.vui.assistant.oem.RomDevice;

/**
 * @Module : PrivatePreference
 * @Comments : 描述
 * @Author : Dancindream
 * @CreateDate : 2012-5-28
 * @ModifiedBy : Dancindream
 * @ModifiedDate: 2012-5-28
 * @Modified: 2012-5-28: 实现基本功能
 */
public class PrivatePreference {
	public static final String TAG = "PrivatePreference";

	public static volatile float mRecordingVoiceVolumn = 0.0f;

	// TODO 百度地图的key
	// 申请密钥地址：http://developer.baidu.com/map/
	public static final String BAIDU_MAP_API_KEY = "";

	// TODO 新浪微博的key
	// 申请地址：http://open.weibo.com/
	// 注意：新浪微博的限制，需要使用者自行申请后替换以下两个值
	public static String SINA_CONSUMER_KEY = "1071923808";
	public static String SINA_CONSUMER_SECRET = "c484faa308a3927faaf55cf8b1937e73";
	
	// TODO 腾讯微博的key
	// 申请地址：http://dev.t.qq.com/
	public static String TENCENT_CONSUMER_KEY = "801430969";
	public static String TENCENT_CONSUMER_SECRET = "463ad87a587092db4544853ca1cada1e";
	
	// TODO 人人网的key
	// 申请地址：http://dev.renren.com/
	public static String RENREN_API_KEY = "683b270f30304393a1889a439c02d40c";
	public static String RENREN_SECRET_KEY = "bf2e0d9be28a47a1b34d0905fb05945a";
	public static String RENREN_APP_ID = "243046";

	public static final int APPLICATION_ID = 0x6CE898A4;
	public static final int REQUEST_CONTACT_CODE = 0x6CE898A5;
	public static final int REQUEST_START_INTENT_CODE = 0x6CE898A6;
	public static final int REQUEST_SELECT_DATETIME_CODE = 0x6CE898A7;
	
	public static float WEIBO_IMAGE_MAX_WIDTH = 400.0f;
	public static float WEIBO_IMAGE_MAX_HEIGHT = 400.0f;

	public static int SCREEN_WIDTH = 480;
	public static int SCREEN_HEIGHT = 800;

	public static int HTTP_NETWORK_TIMEOUT = 10000;

	public static String IMEI = "";
	public static String CURRENT_CITY = "";
	public static String VERSION = "";

	public static final String FOLDER_HOME = "YunZhiSheng/";
	public static final String FOLDER_ISHUOSHUO = "iShuoShuo/";
	public static final String FOLDER_DEBUG = "debug/";
	public static final String FOLDER_BACKUP = "backup/";
	public static final String FOLDER_PCM = "pcm/";
	public static final String FOLDER_DUMP = "dump/";
	public static final String FOLDER_COMPILER = "compiler/";
	public static final String FOLDER_IMG = "img/";
	public static final String FOLDER_MODEL = "model/";
	public static String FOLDER_PACKAGE_CACHE = "";
	public static String FOLDER_PACKAGE_FILES = "";

	public static void init(Context context) {
		IMEI = RomDevice.getDeviceId(context);
		VERSION = RomDevice.getAppVersionName(context);
		FOLDER_PACKAGE_CACHE = context.getCacheDir().getAbsolutePath() + File.separator;
		FOLDER_PACKAGE_FILES = context.getFilesDir().getAbsolutePath() + File.separator;
	}
}
