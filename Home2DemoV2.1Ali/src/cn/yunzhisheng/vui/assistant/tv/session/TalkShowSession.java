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
import cn.yunzhisheng.vui.assistant.model.KnowledgeMode;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.vui.assistant.tv.MainApplication;
import cn.yunzhisheng.vui.assistant.util.Util;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Dancindream
 * @CreateDate : 2013-9-6
 * @ModifiedBy : Dancindream
 * @ModifiedDate: 2013-9-6
 * @Modified: 2013-9-6: 实现基本功能
 */
public class TalkShowSession extends BaseSession {
	public static final String TAG = "TalkShowSession";

	/**
	 * @Author : Dancindream
	 * @CreateDate : 2013-9-6
	 * @param context
	 * @param sessionManagerHandler
	 */
	public TalkShowSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
	}

	public void putProtocol(JSONObject jsonProtocol) {
		super.putProtocol(jsonProtocol);
		addQuestionViewText(mQuestion);
		/*if (!TextUtils.isEmpty(mOriginType) && mOriginType.equals(SessionPreference.DOMAIN_CHAT)){
			MainApplication.mChatNum++;
			if(MainApplication.mChatNum == 1 || (MainApplication.mChatNum-1) % 3 == 0) {
				String helpStr = KnowledgeMode.getKnowledgeHelpAnswer(mContext,
						KnowledgeMode.KNOWLEDGE_NOSUPPORT);
				addAnswerViewText(Util.appendChatAnswer(mAnswer,helpStr));
				playTTS(Util.appendAnswer(mAnswer,helpStr));
			} else {
				addAnswerViewText(mAnswer);
				playTTS(mAnswer);
			}
		} else {*/
			addAnswerViewText(mAnswer);
			playTTS(mAnswer);
		//}

	}

}
