/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : StockShowSession.java
 * @ProjectName : vui_assistant
 * @PakageName : cn.yunzhisheng.ishuoshuo.session
 * @Author : Dancindream
 * @CreateDate : 2013-9-3
 */
package cn.yunzhisheng.vui.assistant.tv.session;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import cn.yunzhisheng.vui.assistant.tv.view.StockContentView;
import cn.yunzhisheng.vui.modes.StockInfo;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Dancindream
 * @CreateDate : 2013-9-3
 * @ModifiedBy : Dancindream
 * @ModifiedDate: 2013-9-3
 * @Modified:
 * 2013-9-3: 实现基本功能
 */
public class StockShowSession extends BaseSession {
	public static final String TAG = "StockShowSession";
	private StockContentView mContentView = null;

	/**
	 * @Author : Dancindream
	 * @CreateDate : 2013-9-3
	 * @param context
	 * @param sessionManagerHandler
	 */
	public StockShowSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
	}

	public void putProtocol(JSONObject jsonProtocol) {
		super.putProtocol(jsonProtocol);

		addQuestionViewText(mQuestion);
		
		JSONObject resultObject = getJSONObject(mDataObject, "result");

		StockInfo stockInfo = new StockInfo();

		stockInfo.setChartImgUrl(getJsonValue(resultObject, "imageUrl", ""));
		stockInfo.setName(getJsonValue(resultObject, "name", ""));
		stockInfo.setCode(getJsonValue(resultObject, "code", ""));
		stockInfo.setCurrentPrice(getJsonValue(resultObject, "currentPrice", ""));
		stockInfo.setChangeAmount(Double.parseDouble(getJsonValue(resultObject, "changeAmount", "0.0")));
		stockInfo.setChangeRate(getJsonValue(resultObject, "changeRate", ""));
		stockInfo.setTurnover(getJsonValue(resultObject, "turnover", ""));
		stockInfo.setHighestPrice(getJsonValue(resultObject, "highestPrice", ""));
		stockInfo.setLowestPrice(getJsonValue(resultObject, "lowestPrice", ""));
		stockInfo.setYesterdayClosingPrice(getJsonValue(resultObject, "yesterdayClosePrice", ""));
		stockInfo.setTodayOpeningPrice(getJsonValue(resultObject, "todayOpenPrice", ""));
		stockInfo.setUpdateTime(getJsonValue(resultObject, "updateTime", ""));

		mContentView = new StockContentView(mContext);
		mContentView.setStockInfo(stockInfo);
		addAnswerView(mContentView);
		playTTS(mAnswer);
	}

}
