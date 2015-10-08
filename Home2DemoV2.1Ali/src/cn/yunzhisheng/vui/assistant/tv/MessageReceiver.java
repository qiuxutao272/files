package cn.yunzhisheng.vui.assistant.tv;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import cn.yunzhisheng.vui.assistant.tv.media.MusicPlaybackService;
import cn.yunzhisheng.vui.assistant.tv.talk.TalkService;
import cn.yunzhisheng.common.util.LogUtil;

public class MessageReceiver extends BroadcastReceiver {
	private static final String TAG = "MessageReceiver";
	/**
	 * @Description : TODO 【重要】2.4G硬件遥控器启动录音的消息列表，至少需要有一个有效值
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	public static final String[] ACTION_START_TALK_ARRAY = { "cn.yunzhisheng.intent.voice.start" };
	/**
	 * @Description : TODO 【重要】2.4G硬件遥控器结束录音的消息列表，至少需要有一个有效值
	 * @Author : Dancindream
	 * @CreateDate : 2014-4-22
	 */
	public static final String[] ACTION_STOP_TALK_ARRAY = { "cn.yunzhisheng.intent.voice.stop" };

	public static final String ACTION_VIRTUAL_KEY_SERVER = "cn.yunzhisheng.intent.virtualKeyServer";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		LogUtil.d(TAG, "action:" + action);

		if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {

			Intent i = new Intent(context, TalkService.class);
			context.startService(i);

			Intent windowIntent = new Intent(context, WindowService.class);
			context.startService(windowIntent);

			Intent vritualKey = new Intent(ACTION_VIRTUAL_KEY_SERVER);
			context.startService(vritualKey);

			// Intent musicIntent = new Intent(context,
			// MusicPlaybackService.class);
			// context.startService(musicIntent);
		} else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
			intent.setClass(context, WindowService.class);
			context.stopService(intent);
		} else {
			boolean isVoiceKeyDown = false;
			if (!isVoiceKeyDown) {
				for (String key : ACTION_START_TALK_ARRAY) {
					if (key.equals(action)) {
						isVoiceKeyDown = true;
						break;
					}
				}
			}
			if (!isVoiceKeyDown) {
				for (String key : ACTION_STOP_TALK_ARRAY) {
					if (key.equals(action)) {
						isVoiceKeyDown = true;
						break;
					}
				}
			}
			if (isVoiceKeyDown) {
				intent.setClass(context, WindowService.class);
				context.startService(intent);
				try {
					abortBroadcast();
				} catch (Exception e) {
				}
			}
		}

		if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
			// Slog.d(TAG, "action : " + Intent.ACTION_MEDIA_MOUNTED);
			// Log.i("lizhongyu", "=======0000=====" + path);
			// Log.i("huage", "==999===" + select);
			// Log.i("sdka", "==sdsdsdsdsds===" );
			String path = intent.getDataString();
			Log.i("sdka", "==U盘的路径是===" + path);

			SharedPreferences mySharedPreferences = context
					.getSharedPreferences("uplu", Activity.MODE_PRIVATE);
			// 实例化SharedPreferences.Editor对象（第二步）
			SharedPreferences.Editor editor = mySharedPreferences.edit();
			// 用putString的方法保存数据
			editor.putString("up", path);
			
			// 提交当前数据
			editor.commit();

		}
	}
}
