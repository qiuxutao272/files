/**
 * Copyright (c) 2012-2012 Yunzhisheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : KnowledgeMode.java
 * @ProjectName : iShuoShuo2
 * @PakageName : cn.yunzhisheng.vui.assistant.knowledge
 * @Author : Brant
 * @CreateDate : 2012-11-30
 */
package cn.yunzhisheng.vui.assistant.model;
import java.util.Random;

import android.content.Context;
import android.text.TextUtils;
import cn.yunzhisheng.vui.assistant.Res;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Brant
 * @CreateDate : 2012-11-30
 * @ModifiedBy : Brant
 * @ModifiedDate: 2012-11-30
 * @Modified: 2012-11-30: 实现基本功能
 */
public class KnowledgeMode {
	public static final String TAG = "KnowledgeMode";

	public static final int KNOWLEDGE_STAGE_CONTACT_NOT_FOUND = 10001;
	public static final int KNOWLEDGE_STAGE_CONTACT_AUTO_PICK = 10002;
	public static final int KNOWLEDGE_STAGE_NUMBER_AUTO_PICK = 10003;
	public static final int KNOWLEDGE_STAGE_NUMBER_NOT_FOUND = 10004;
	public static final int KNOWLEDGE_STAGE_NETWORK_EXCEPTION = 10005;
	public static final int KNOWLEDGE_STAGE_NO_NETWORK = 10006;
	public static final int KNOWLEDGE_STAGE_NETWORK_TIMEOUT = 10007;
	public static final int KNOWLEDGE_STAGE_NO_RECOGNISE_RESULT = 10008;
	public static final int KNOWLEDGE_STAGE_NO_INPUT = 10009;
	public static final int KNOWLEDGE_STAGE_HELP = 10010;

	public static final int KNOWLEDGE_STAGE_CALL_CONTACT_NAME = 10901;
	public static final int KNOWLEDGE_STAGE_CALL_CONTACT_NAME_NO_RECOGNISE_RESULT = 10902;
	public static final int KNOWLEDGE_STAGE_CALL_CONTACT_NAME_NO_INPUT = 10903;
	public static final int KNOWLEDGE_STAGE_CALL_DIALING = 10904;

	public static final int KNOWLEDGE_STAGE_SMS_CONTACT_NAME = 11001;
	public static final int KNOWLEDGE_STAGE_SMS_CONTENT = 11002;
	public static final int KNOWLEDGE_STAGE_SMS_CONTACT_NAME_NO_RECOGNISE_RESULT = 11003;

	public static final int KNOWLEDGE_STAGE_WEIBO_CONTENT = 11101;

	public static final int KNOWLEDGE_STAGE_MEMO_DATETIME = 11201;
	public static final int KNOWLEDGE_STAGE_MEMO_CREATED = 11202;
	public static final int KNOWLEDGE_STAGE_MEMO_DATETIME_EXPIRED = 11203;
	public static final int KNOWLEDGE_STAGE_MEMO_DATETIME_NO_RECOGNISE_RESULT = 11204;

	public static final int KNOWLEDGE_STAGE_APP_NOT_FOUND = 11301;
	public static final int KNOWLEDGE_STAGE_APP_AUTO_PICK = 11302;
	public static final int KNOWLEDGE_STAGE_APP_OPEN_NAME = 11303;
	public static final int KNOWLEDGE_STAGE_APP_OPENING = 11304;
	public static final int KNOWLEDGE_STAGE_APP_OPEN_NAME_NO_RECOGNISE_RESULT = 11305;
	public static final int KNOWLEDGE_STAGE_APP_UNINSTALL_NAME = 11306;
	public static final int KNOWLEDGE_STAGE_APP_UNINSTALLING = 11307;
	public static final int KNOWLEDGE_STAGE_APP_UNINSTALL_NAME_NO_RECOGNISE_RESULT = 11308;

	public static final int KNOWLEDGE_STAGE_WEATHER_FETCHING = 11401;
	public static final int KNOWLEDGE_STAGE_WEATHER_CITY_NOT_FOUND = 11402;
	public static final int KNOWLEDGE_STAGE_WEATHER_LOCATE_FAILED = 11403;

	public static final int KNOWLEDGE_STAGE_SEARCH_KEYWORD = 11501;

	public static final int KNOWLEDGE_STAGE_MUSIC_NOT_FOUND = 11601;

	public static final int KNOWLEDGE_STAGE_WEB_NOT_FOUND = 11701;

	public static final int KNOWLEDGE_HELP_ERROR = 11801;

	public static final int KNOWLEDGE_NOSUPPORT = 11901;
	
	public static final int KNOWLEDGE_FLOAT = 12101;
	
	public static final int KNOWLEDGE_NOVOICE = 12102;

	private static Random mRandom = new Random();
	private static int mKnowledgeModeSign = 0;
	private static int mRepeatCount = 0;

	private static final int KNOWLEDGE_ARRAY_START_INDEX = 1;

	public static String getKnowledgeAnswer(Context context, int stage) {
		if (mKnowledgeModeSign == stage) {
			mRepeatCount++;
		} else {
			mKnowledgeModeSign = stage;
			mRepeatCount = 0;
		}

		int[] repeatArr = null;
		switch (stage) {
		case KNOWLEDGE_STAGE_NETWORK_EXCEPTION:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_NETWORK_EXCEPTION;
			break;
		case KNOWLEDGE_STAGE_NO_NETWORK:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_NO_NETWORK;
			break;
		case KNOWLEDGE_STAGE_NETWORK_TIMEOUT:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_NETWORK_TIMEOUT;
			break;
		case KNOWLEDGE_STAGE_NO_RECOGNISE_RESULT:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_NO_RECOGNISE_RESULT;
			break;
		case KNOWLEDGE_STAGE_NO_INPUT:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_NO_INPUT;
			break;
		case KNOWLEDGE_STAGE_HELP:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_HELP;
			break;
		case KNOWLEDGE_STAGE_CALL_CONTACT_NAME:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_CALL_CONTACT_NAME;
			break;
		case KNOWLEDGE_STAGE_CONTACT_NOT_FOUND:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_CONTACT_NOT_FOUND;
			break;
		case KNOWLEDGE_STAGE_CONTACT_AUTO_PICK:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_CONTACT_AUTO_PICK;
			break;
		case KNOWLEDGE_STAGE_NUMBER_NOT_FOUND:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_NUMBER_NOT_FOUND;
			break;
		case KNOWLEDGE_STAGE_NUMBER_AUTO_PICK:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_NUMBER_AUTO_PICK;
			break;
		case KNOWLEDGE_STAGE_CALL_CONTACT_NAME_NO_RECOGNISE_RESULT:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_NUMBER_AUTO_PICK;
			break;
		case KNOWLEDGE_STAGE_CALL_CONTACT_NAME_NO_INPUT:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_CALL_CONTACT_NAME_NO_INPUT;
			break;
		case KNOWLEDGE_STAGE_CALL_DIALING:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_CALL_DIALING;
			break;
		case KNOWLEDGE_STAGE_SMS_CONTACT_NAME:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_SMS_CONTACT_NAME;
			break;
		case KNOWLEDGE_STAGE_SMS_CONTENT:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_SMS_CONTENT;
			break;
		case KNOWLEDGE_STAGE_SMS_CONTACT_NAME_NO_RECOGNISE_RESULT:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_SMS_CONTACT_NAME_NO_RECOGNISE_RESULT;
			break;
		case KNOWLEDGE_STAGE_WEIBO_CONTENT:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_WEIBO_CONTENT;
			break;
		case KNOWLEDGE_STAGE_MEMO_DATETIME:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_MEMO_DATETIME;
			break;
		case KNOWLEDGE_STAGE_MEMO_CREATED:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_MEMO_CREATED;
			break;
		case KNOWLEDGE_STAGE_MEMO_DATETIME_EXPIRED:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_MEMO_DATETIME_EXPIRED;
			break;
		case KNOWLEDGE_STAGE_MEMO_DATETIME_NO_RECOGNISE_RESULT:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_MEMO_DATETIME_NO_RECOGNISE_RESULT;
			break;
		case KNOWLEDGE_STAGE_APP_NOT_FOUND:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_APP_NOT_FOUND;
			break;
		case KNOWLEDGE_STAGE_WEB_NOT_FOUND:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_WEB_NOT_FOUND;
			break;
		case KNOWLEDGE_STAGE_APP_AUTO_PICK:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_APP_AUTO_PICK;
			break;
		case KNOWLEDGE_STAGE_APP_OPEN_NAME:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_APP_OPEN_NAME;
			break;
		case KNOWLEDGE_STAGE_APP_OPENING:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_APP_OPENING;
			break;
		case KNOWLEDGE_STAGE_APP_OPEN_NAME_NO_RECOGNISE_RESULT:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_APP_OPEN_NAME_NO_RECOGNISE_RESULT;
			break;
		case KNOWLEDGE_STAGE_APP_UNINSTALL_NAME:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_APP_UNINSTALL_NAME;
			break;
		case KNOWLEDGE_STAGE_APP_UNINSTALLING:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_APP_UNINSTALLING;
			break;
		case KNOWLEDGE_STAGE_APP_UNINSTALL_NAME_NO_RECOGNISE_RESULT:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_APP_UNINSTALL_NAME_NO_RECOGNISE_RESULT;
			break;
		case KNOWLEDGE_STAGE_WEATHER_FETCHING:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_WEATHER_FETCHING;
			break;
		case KNOWLEDGE_STAGE_WEATHER_CITY_NOT_FOUND:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_WEATHER_CITY_NOT_FOUND;
			break;
		case KNOWLEDGE_STAGE_WEATHER_LOCATE_FAILED:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_WEATHER_LOCATE_FAILED;
			break;
		case KNOWLEDGE_STAGE_SEARCH_KEYWORD:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_SEARCH_KEYWORD;
			break;
		case KNOWLEDGE_STAGE_MUSIC_NOT_FOUND:
			repeatArr = KnowledgeBase.KNOWLEDGE_STAGE_MUSIC_NOT_FOUND;
			break;
		}

		if (repeatArr == null || repeatArr.length == 0) {
			return "";
		}

		if (mRepeatCount >= repeatArr.length) {
			mRepeatCount = repeatArr.length - 1;
		}

		String[] answerArr = KnowledgeBase.getStringArray(context,
				repeatArr[mRepeatCount]);
		return getRandomItem(answerArr);
	}

	public static String getRecognitionNoResultAnswer(Context context,
			String message) {
		if (!TextUtils.isEmpty(message)) {
			String[] grammerKeyWordCall = KnowledgeBase.getStringArray(context,
					Res.array.knowledge_grammer_keyword_call);
			for (String str : grammerKeyWordCall) {
				if (message.contains(str)) {
					return getRandomContentString(context,
							Res.array.knowledge_grammer_example_call);
				}
			}

			String[] grammerKeyWordSms = KnowledgeBase.getStringArray(context,
					Res.array.knowledge_grammer_keyword_sms);
			for (String str : grammerKeyWordSms) {
				if (message.contains(str)) {
					return getRandomContentString(context,
							Res.array.knowledge_grammer_example_sms);
				}
			}

			String[] grammerKeyWordWeibo = KnowledgeBase.getStringArray(
					context, Res.array.knowledge_grammer_keyword_weibo);
			for (String str : grammerKeyWordWeibo) {
				if (message.contains(str)) {
					return getRandomContentString(context,
							Res.array.knowledge_grammer_example_weibo);
				}
			}

			String[] grammerKeyWordWeather = KnowledgeBase.getStringArray(
					context, Res.array.knowledge_grammer_keyword_weather);
			for (String str : grammerKeyWordWeather) {
				if (message.contains(str)) {
					return getRandomContentString(context,
							Res.array.knowledge_grammer_example_weather);
				}
			}

			String[] grammerKeyWordApp = KnowledgeBase.getStringArray(context,
					Res.array.knowledge_grammer_keyword_app);
			for (String str : grammerKeyWordApp) {
				if (message.contains(str)) {
					return getRandomContentString(context,
							Res.array.knowledge_grammer_example_app);
				}
			}

			String[] grammerKeyWordMemo = KnowledgeBase.getStringArray(context,
					Res.array.knowledge_grammer_keyword_memo);
			for (String str : grammerKeyWordMemo) {
				if (message.contains(str)) {
					return getRandomContentString(context,
							Res.array.knowledge_grammer_example_memo);
				}
			}

			String[] grammerKeyWordSearch = KnowledgeBase.getStringArray(
					context, Res.array.knowledge_grammer_keyword_search);
			for (String str : grammerKeyWordSearch) {
				if (message.contains(str)) {
					return getRandomContentString(context,
							Res.array.knowledge_grammer_example_search);
				}
			}
		}
		return getRandomString(context,
				Res.array.knowledge_stage_no_recognise_result);
	}

	public static String getKnowledgeHelpAnswer(Context context, int stage) {
		String answerString = "";
		
		String videoString = "";
		String floatString = "";
		String[] video_Arr = KnowledgeBase.getStringArray(context, KnowledgeBase.KNOWLEDGE_VIDEO[mRepeatCount]);
		String[] video_content_1_Arr = KnowledgeBase.getStringArray(context, KnowledgeBase.KNOWLEDGE_VIDEO_CONTENT_ONE[mRepeatCount]);
		String[] video_content_2_Arr = KnowledgeBase.getStringArray(context, KnowledgeBase.KNOWLEDGE_VIDEO_CONTENT_TWO[mRepeatCount]);
		videoString = String.format(getRandomItem(video_Arr), getRandomItem(video_content_1_Arr) + getRandomItem(video_content_2_Arr));
		
		floatString = getRandomItem(video_content_1_Arr) + getRandomItem(video_content_2_Arr);
		
		if(stage == KNOWLEDGE_FLOAT){
			answerString = "“"+floatString+"”";
			return answerString;
		}else if(stage == KNOWLEDGE_HELP_ERROR){
			answerString = videoString;
			return answerString;
		}else if(stage == KNOWLEDGE_NOSUPPORT){
			String tvString = "";
			String[] tv_Arr = KnowledgeBase.getStringArray(context, KnowledgeBase.KNOWLEDGE_TV[mRepeatCount]);
			String[] tv_content_2_Arr = KnowledgeBase.getStringArray(context, KnowledgeBase.KNOWLEDGE_TV_CONTENT_TWO[mRepeatCount]);
			tvString = String.format(getRandomItem(tv_Arr), getRandomItem(tv_content_2_Arr));
			
//			String channelString = "";
//			String[] channel_Arr = KnowledgeBase.getStringArray(context, KnowledgeBase.KNOWLEDGE_CHANNEL[mRepeatCount]);
//			String[] channel_content_1_Arr = KnowledgeBase.getStringArray(context, KnowledgeBase.KNOWLEDGE_CHANNEL_CONTENT_ONE[mRepeatCount]);
//			String[] channel_content_2_Arr = KnowledgeBase.getStringArray(context, KnowledgeBase.KNOWLEDGE_CHANNEL_CONTENT_TWO[mRepeatCount]);
//			channelString = String.format(getRandomItem(channel_Arr), getRandomItem(channel_content_1_Arr) + getRandomItem(channel_content_2_Arr));
//			
//			String musicString = "";
//			String[] music_Arr = KnowledgeBase.getStringArray(context, KnowledgeBase.KNOWLEDGE_MUSIC[mRepeatCount]);
//			String[] music_content_2_Arr = KnowledgeBase.getStringArray(context, KnowledgeBase.KNOWLEDGE_MUSIC_CONTENT_TWO[mRepeatCount]);
//			musicString = String.format(getRandomItem(music_Arr), getRandomItem(music_content_2_Arr));
			
			String settingString = "";
			String[] setting_Arr = KnowledgeBase.getStringArray(context, KnowledgeBase.KNOWLEDGE_SETTING[mRepeatCount]);
			settingString = getRandomItem(setting_Arr);
			
			String[] mStrArrCache = new String[5];
			mStrArrCache[0] = videoString;
			mStrArrCache[1] = tvString;
//			mStrArrCache[2] = channelString;
//			mStrArrCache[3] = musicString;
			mStrArrCache[2] = settingString;
			
			answerString = getRandomItem(mStrArrCache);
		}
		return answerString;
	}

	public static String getRandomString(Context context, int res) {
		String[] answerArr = KnowledgeBase.getStringArray(context, res);
		return getRandomItem(answerArr);
	}

	public static String getHeadString(Context context, int res) {
		String[] answerArr = KnowledgeBase.getStringArray(context, res);
		if (answerArr.length >= KNOWLEDGE_ARRAY_START_INDEX) {
			return answerArr[0];
		}
		return "";
	}

	public static String getRandomContentString(Context context, int res) {
		String[] answerArr = KnowledgeBase.getStringArray(context, res);
		return getRandomItem(answerArr, KNOWLEDGE_ARRAY_START_INDEX);
	}

	public static String getAllString(Context context, int res) {
		String[] answerArr = KnowledgeBase.getStringArray(context, res);
		StringBuilder sb = new StringBuilder();

		if (answerArr != null && answerArr.length > 0) {
			for (int i = 0; i < answerArr.length - KNOWLEDGE_ARRAY_START_INDEX; i++) {
				if (sb.length() > 0) {
					sb.append("\n\n");
				}
				sb.append((i + 1) + "、"
						+ answerArr[i + KNOWLEDGE_ARRAY_START_INDEX]);
			}
		}
		return sb.toString();
	}

	private static String getRandomItem(String[] arr) {
		if (arr == null || arr.length == 0) {
			return "";
		} else {
			return arr[mRandom.nextInt(arr.length)];
		}
	}

	private static String getRandomItem(String[] arr, int startIndex) {
		if (arr == null || arr.length == 0) {
			return "";
		} else {
			return arr[mRandom.nextInt(arr.length - startIndex) + startIndex];
		}
	}
}
