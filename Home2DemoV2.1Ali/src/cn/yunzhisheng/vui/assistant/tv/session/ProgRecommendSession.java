/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName	: ProgRecommendSession.java
 * @ProjectName	: vui_assistant
 * @PakageName	: cn.yunzhisheng.ishuoshuo.session
 * @Author		: Dancindream
 * @CreateDate	: 2013-9-3
 */
package cn.yunzhisheng.vui.assistant.tv.session;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import cn.yunzhisheng.vui.assistant.model.TVBroadcastsGroupItem;
import cn.yunzhisheng.vui.assistant.model.TVProgramItem;
import cn.yunzhisheng.vui.assistant.model.TVRecommendResult;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.vui.assistant.tv.view.TVProgramRecommendView;

/**
 * @Module		: 隶属模块名
 * @Comments	: 描述
 * @Author		: Dancindream
 * @CreateDate	: 2013-9-3
 * @ModifiedBy	: Dancindream
 * @ModifiedDate: 2013-9-3
 * @Modified: 
 * 2013-9-3: 实现基本功能
 */
public class ProgRecommendSession extends BaseSession {
	public static final String TAG = "ProgRecommendSession";
	
	private TVRecommendResult mTVRecommendResult = null;
	
	/**
	 * @Author		: Dancindream
	 * @CreateDate	: 2013-9-3
	 * @param context
	 * @param sessionManagerHandler
	 */
	public ProgRecommendSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
	}
	
	public void putProtocol(JSONObject jsonProtocol) {
		super.putProtocol(jsonProtocol);
		
		JSONObject resultObject = getJSONObject(mDataObject, SessionPreference.KEY_RESULT);

		mTVRecommendResult = new TVRecommendResult();

		mTVRecommendResult.category = getJsonValue(resultObject, SessionPreference.KEY_CATEGORY);
		mTVRecommendResult.period = getJsonValue(resultObject, SessionPreference.KEY_PERIOD);

		JSONArray broadcastsArray = getJsonArray(resultObject, SessionPreference.KEY_BROADCASTS);
		if (broadcastsArray != null && broadcastsArray.length() > 0) {
			for (int i = 0; i < broadcastsArray.length(); i++) {
				JSONObject json = getJSONObject(broadcastsArray, i);
				mTVRecommendResult.broadcasts.add(getTVBroadcastsGroupItemFromJsonObject(json));
			}
		}

		addQuestionViewText(mQuestion);
        addAnswerViewText(mAnswer);
		playTTS(mAnswer);

		if (mTVRecommendResult != null && mTVRecommendResult.broadcasts.size() > 0) {
			final TVProgramRecommendView tvView = new TVProgramRecommendView(mContext);
			tvView.setTVRecommendResult(mTVRecommendResult);
            addAnswerView(tvView);
		}
	}

	private TVBroadcastsGroupItem getTVBroadcastsGroupItemFromJsonObject(JSONObject json) {
		if (json == null) {
			return new TVBroadcastsGroupItem(null);
		}

		TVBroadcastsGroupItem item = new TVBroadcastsGroupItem(getJsonValue(json, SessionPreference.KEY_NAME));
		item.pid = getJsonValue(json, SessionPreference.KEY_PID);
		item.score = getJsonValue(json, SessionPreference.KEY_SCORE);
		JSONArray programs = getJsonArray(json, SessionPreference.KEY_PROGRAMS);
		if (programs != null && programs.length() > 0) {
			for (int i = 0; i < programs.length(); i++) {
				JSONObject programJson = getJSONObject(programs, i);
				item.programs.add(getProgramItemFromJsonObject(programJson));
			}
		}

		return item;
	}

	private TVProgramItem getProgramItemFromJsonObject(JSONObject json) {
		if (json == null) {
			return new TVProgramItem();
		}

		TVProgramItem item = new TVProgramItem();

		item.pid = getJsonValue(json, SessionPreference.KEY_PID);
		item.channel = getJsonValue(json, SessionPreference.KEY_CHANNEL);
		item.time = getJsonValue(json, SessionPreference.KEY_TIME);
		item.title = getJsonValue(json, SessionPreference.KEY_TITLE);

		return item;
	}
}
