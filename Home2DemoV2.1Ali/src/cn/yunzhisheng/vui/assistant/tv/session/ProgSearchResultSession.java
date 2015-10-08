/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName	: ProgSearchResultSession.java
 * @ProjectName	: vui_assistant
 * @PakageName	: cn.yunzhisheng.ishuoshuo.session
 * @Author		: Dancindream
 * @CreateDate	: 2013-9-3
 */
package cn.yunzhisheng.vui.assistant.tv.session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import cn.yunzhisheng.common.util.LogUtil;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.assistant.model.TVByDateGroupItem;
import cn.yunzhisheng.vui.assistant.model.TVProgramItem;
import cn.yunzhisheng.vui.assistant.model.TVSearchResult;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.vui.assistant.tv.view.TVSearchContentView;

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
public class ProgSearchResultSession extends BaseSession {
	public static final String TAG = "ProgSearchResultSession";
	private static final int MAX_DATE_COUNT_PROG_SEARCH_RESULT = 1;
	private TVSearchResult mTVSearchResult = null;
	
	/**
	 * @Author		: Dancindream
	 * @CreateDate	: 2013-9-3
	 * @param context
	 * @param sessionManagerHandler
	 */
	public ProgSearchResultSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
	}
	
	public void putProtocol(JSONObject jsonProtocol) {
		super.putProtocol(jsonProtocol);

		JSONObject resultObject = getJSONObject(mDataObject, SessionPreference.KEY_RESULT);

		addQuestionViewText(mQuestion);
        addAnswerViewText(mAnswer);
		playTTS(mAnswer);

		mTVSearchResult = new TVSearchResult();

		JSONArray byDateArray = getJsonArray(resultObject, SessionPreference.KEY_BYDATE);

		if (byDateArray != null && byDateArray.length() > 0) {
			for (int i = 0; i <= 1 && i < MAX_DATE_COUNT_PROG_SEARCH_RESULT; i++) {
				JSONObject json = getJSONObject(byDateArray, i);
				mTVSearchResult.byDate.add(getByDateItemFromJsonObject(json));
			}
		}

		if (mTVSearchResult != null && mTVSearchResult.byDate.size() > 0) {

			TVSearchContentView tvView = new TVSearchContentView(mContext) {

				@Override
				public List<HashMap<String, Object>> trans2HasMapData(ArrayList<TVProgramItem> programs) {
					List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();

					for (TVProgramItem pro : programs) {
						HashMap<String, Object> item = new HashMap<String, Object>();
						item.put("title", pro.title);
						item.put("time", pro.time);
						item.put("channel", pro.channel);
						data.add(item);
					}
					return data;
				}

				@Override
				public void bindView(int position, View view, Map<String, ?> data) {
					TextView tvTime = (TextView) view
							.findViewById(Res.id.textViewTVTime);
					TextView tvChannel = (TextView) view
							.findViewById(Res.id.textViewTVChannel);
					TextView tvTitle = (TextView) view
							.findViewById(Res.id.textViewTVTitle);
					LogUtil.d(TAG, "time = " + (String) data.get("time")+",channel = " + (String) data.get("channel")+",title = " + (String) data.get("title"));
					tvTime.setText((String) data.get("time"));
					tvChannel.setText((String) data.get("channel"));
					tvTitle.setText((String) data.get("title"));
					super.bindView(position, view, data);
				}
				
				@Override
				public int getBindResource() {
					return Res.layout.tv_program_list_item;
				}

				@Override
				public void release() {
					
				}
				
			};
			tvView.setTVSearchResult(mTVSearchResult);
            addAnswerView(tvView);
		}
	}


	private TVByDateGroupItem getByDateItemFromJsonObject(JSONObject json) {
		if (json == null) {
			return new TVByDateGroupItem(null);
		}

		TVByDateGroupItem item = new TVByDateGroupItem(getJsonValue(json, SessionPreference.KEY_DATE));
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
