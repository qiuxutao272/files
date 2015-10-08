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
import android.content.Context;
import android.os.Handler;
import cn.yunzhisheng.common.net.Network;
import cn.yunzhisheng.vui.assistant.tv.view.HelpShowView;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : WLP
 * @CreateDate : 2014-1-22
 * @ModifiedBy : WLP
 * @ModifiedDate: 2014-1-22
 * @Modified : WLP 2014-1-22: 实现基本功能
 */
public class HelpShowSession extends BaseSession{
	public static final String TAG = "TalkShowSession";
	private HelpShowView mHelpShowView;

	/**
	 * @Author : WLP
	 * @CreateDate : 2014-1-22
	 * @param context
	 * @param sessionManagerHandler
	 */
	public HelpShowSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
	}

	public void putProtocol(JSONObject jsonProtocol) {
		super.putProtocol(jsonProtocol);
		addQuestionViewText(mQuestion);
		mAnswer = "你可以这样说:";
		addAnswerViewText(mAnswer);
		playTTS(mAnswer);
		mHelpShowView = new HelpShowView(mContext);
		mHelpShowView.initHelpShowViews(Network.hasNetWorkConnect());
		addAnswerView(mHelpShowView);
	}
}
