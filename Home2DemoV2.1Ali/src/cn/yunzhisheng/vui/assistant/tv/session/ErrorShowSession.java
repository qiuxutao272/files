/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName	: TalkShowSession.java
 * @ProjectName	: vui_assistant
 * @PakageName	: cn.yunzhisheng.ishuoshuo.session
 * @Author		: Dancindream
 * @CreateDate	: 2013-9-6
 */
package cn.yunzhisheng.vui.assistant.tv.session;
import org.json.JSONObject;

import cn.yunzhisheng.common.JsonTool;
import cn.yunzhisheng.common.net.Network;
import cn.yunzhisheng.voicetv.R;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.assistant.model.KnowledgeMode;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.vui.assistant.tv.MainApplication;
import cn.yunzhisheng.vui.assistant.tv.view.HelpShowView;
import cn.yunzhisheng.vui.assistant.util.Util;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Dancindream
 * @CreateDate : 2013-9-6
 * @ModifiedBy : Dancindream
 * @ModifiedDate: 2013-9-6
 * @Modified: 2013-9-6: 实现基本功能
 */
public class ErrorShowSession extends BaseSession{
	public static final String TAG = "ErrorShowSession";
	private HelpShowView mFunctionContainer;

	/**
	 * @Author : Dancindream
	 * @CreateDate : 2013-9-6
	 * @param context
	 * @param sessionManagerHandler
	 */
	public ErrorShowSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
	}

	public void putProtocol(JSONObject jsonProtocol) {
		super.putProtocol(jsonProtocol);
		addQuestionViewText(mQuestion);
		String helpStr = KnowledgeMode.getKnowledgeHelpAnswer(mContext,
				KnowledgeMode.KNOWLEDGE_NOSUPPORT);
		String codeString = JsonTool.getJsonValue(mDataObject, "code", "");
		if(SessionPreference.DOMAIN_CHANNEL_SWITCH.equals(mOriginType)){
			if(codeString.equals(SessionPreference.VALUE_SETTING_ACT_OPEN_CHANNEL)){
				mAnswer = mContext.getResources().getString(Res.string.channel_errorAnswer);
			}else{
				mAnswer = mContext.getResources().getString(Res.string.tv_other_errorAnswer);
			}
		} else if(SessionPreference.DOMAIN_CALL.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.call_errorAnswer);
		} else if(SessionPreference.DOMAIN_SMS.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.sms_errorAnswer);
		} else if(SessionPreference.DOMAIN_CONTACT.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.contact_errorAnswer);
		} else if(SessionPreference.DOMAIN_SETTING.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.setting_errorAnswer);
		} else if(SessionPreference.DOMAIN_REMINDER.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.reminder_errorAnswer);
		} else if(SessionPreference.DOMAIN_ALARM.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.alarm_errorAnswer);
		} else if(SessionPreference.DOMAIN_NOTE.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.note_errorAnswer);
		} else if(SessionPreference.DOMAIN_WEIBO.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.weibo_errorAnswer);
		} else if(SessionPreference.DOMAIN_APP.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.appmgr_errorAnswer);
		} else if(SessionPreference.DOMAIN_SITEMAP.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.website_errorAnswer);
		} else if(SessionPreference.DOMAIN_WEBSEARCH.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.websearch_errorAnswer);
		} else if(SessionPreference.DOMAIN_CALENDAR.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.calendar_errorAnswer);
		}  else if(SessionPreference.DOMAIN_WEATHER.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.weather_errorAnswer);
		}  else if(SessionPreference.DOMAIN_STOCK.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.stock_errorAnswer);
		}  else if(SessionPreference.DOMAIN_COOKBOOK.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.cookbook_errorAnswer);
		}  else if(SessionPreference.DOMAIN_DIANPING.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.localsearch_errorAnswer);
		}  else if(SessionPreference.DOMAIN_POSITION.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.map_errorAnswer);
		}  else if(SessionPreference.DOMAIN_TRAIN.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.train_errorAnswer);
		}  else if(SessionPreference.DOMAIN_FLIGHT.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.flight_errorAnswer);
		}  else if(SessionPreference.DOMAIN_TRANSLATION.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.translation_errorAnswer);
		}  else if(SessionPreference.DOMAIN_NEWS.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.news_errorAnswer);
		}  else if(SessionPreference.DOMAIN_VIDEO.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.video_errorAnswer);
		}  else if(SessionPreference.DOMAIN_MUSIC.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.music_errorAnswer);
		}  else if(SessionPreference.DOMAIN_MOVIE.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.movie_errorAnswer);
		}  else if(SessionPreference.DOMAIN_TV.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.tv_errorAnswer);
		}  else if(SessionPreference.DOMAIN_NOVEL.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.novel_errorAnswer);
		} else if(SessionPreference.DOMAIN_HOTEL.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.hotel_errorAnswer);
		} else if(SessionPreference.DOMAIN_YELLOWPAGE.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.hotline_errorAnswer);
		} else if(SessionPreference.DOMAIN_ERROR.equals(mOriginType)){
			mAnswer = mContext.getResources().getString(Res.string.server_errorAnswer);
			addQuestionViewText(mQuestion);
//			addAnswerViewText(mAnswer);
//			------------------------xuhua
			String toast = mContext.getResources().getString(Res.string.listen_errorAnswer);
			Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
//			playTTS(mAnswer);
			return;
		}else{
			addQuestionViewText(mQuestion);
			String rc = "";
			rc = JsonTool.getJsonValue(mDataObject, "rc", "");
			if (!TextUtils.isEmpty(rc)
					&& mType.equals(SessionPreference.VALUE_TYPE_ERROR_SHOW)) {
				if (rc.equals("5")) {//错误或不懂
					/*String helpErrStr = KnowledgeMode.getKnowledgeHelpAnswer(mContext,
							KnowledgeMode.KNOWLEDGE_HELP_ERROR);
					String morehelpString = mContext.getResources().getString(Res.string.more_help_answer);*/
					String morehelpString = "请按照下列提示跟我说:";
					addAnswerViewText(Util.appendAnswer(mAnswer,morehelpString));
//					playTTS(Util.appendAnswer(mAnswer,morehelpString));
					mFunctionContainer = new HelpShowView(mContext);
					mFunctionContainer.initHelpShowViews(Network
							.hasNetWorkConnect());
					addAnswerView(mFunctionContainer);
				} else if (rc.equals("4")) {//不支持
					String helpNosupportStr = KnowledgeMode.getKnowledgeHelpAnswer(mContext,
							KnowledgeMode.KNOWLEDGE_NOSUPPORT);
					addAnswerViewText(Util.appendAnswer(mAnswer,helpNosupportStr));
//					playTTS(Util.appendAnswer(mAnswer,helpNosupportStr));
				} else if (rc.equals("1000")) {
					mAnswer = mContext.getResources().getStringArray(Res.array.knowledge_novoice)[MainApplication.mNoVoiceNum];
					addAnswerViewText(mAnswer);
//					playTTS(mAnswer);
					MainApplication.mNoVoiceNum ++;
					if(MainApplication.mNoVoiceNum == 3){
						MainApplication.mNoVoiceNum = 0;
					}
				} else {
					addAnswerViewText(mAnswer);
//					playTTS(mAnswer);
				}
				return;
			} else {
				addAnswerViewText(mAnswer);
//				playTTS(mAnswer);
				return;
			}
		}
		addAnswerViewText(Util.appendAnswer(mAnswer,helpStr));
//		playTTS(Util.appendAnswer(mAnswer,helpStr));
	}

}
