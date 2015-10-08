/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : WeatherSessionNew.java
 * @ProjectName : iShuoShuo2_work
 * @PakageName : cn.yunzhisheng.ishuoshuo.controller
 * @Author : CavanShi
 * @CreateDate : 2013-4-22
 */
package cn.yunzhisheng.vui.assistant.tv.session;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import cn.yunzhisheng.vui.assistant.model.WeatherDay;
import cn.yunzhisheng.vui.assistant.model.WeatherInfo;
import cn.yunzhisheng.vui.assistant.preference.SessionPreference;
import cn.yunzhisheng.vui.assistant.tv.view.WeatherContentView;
import cn.yunzhisheng.vui.assistant.util.Util;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : CavanShi
 * @CreateDate : 2013-4-22
 * @ModifiedBy : CavanShi
 * @ModifiedDate: 2013-4-22
 * @Modified:
 * 2013-4-22: 实现基本功能
 */
public class WeatherShowSession extends BaseSession {
	public static final String TAG = "WeatherSessionNew";

	private String mCity;
	private String mCityCode;

	private WeatherContentView mWeatherView;

	private JSONObject mWeatherResultJson;

	public WeatherShowSession(Context context, Handler sessionManagerHandler) {
		super(context, sessionManagerHandler);
	}

	public void putProtocol(JSONObject jsonProtocol) {
		super.putProtocol(jsonProtocol);

		addQuestionViewText(mQuestion);

		mWeatherResultJson = getJSONObject(mDataObject, SessionPreference.KEY_RESULT);

		showWeather();
	}

	private WeatherDay getweatherItemFromJsonObject(JSONObject jObject) {
		WeatherDay w = new WeatherDay();
		String year = getJsonValue(jObject, "year", "0");
		String month = getJsonValue(jObject, "month", "0");
		String day = getJsonValue(jObject, "day", "0");
		String dayofweek = getJsonValue(jObject, "dayOfWeek", "2");
		String weather = getJsonValue(jObject, "weather", "");
		String highTemp = getJsonValue(jObject, "highestTemperature", "0");
		String lowTemp = getJsonValue(jObject, "lowestTemperature", "0");
		String currentTemp = getJsonValue(jObject, "currentTemperature", "0");
		String wind = getJsonValue(jObject, "wind", "");
		
		String pm2_5 = getJsonValue(jObject, "pm2_5", "0");
		
		String quality = getJsonValue(jObject, "quality", "");
		
		w.setYear(Integer.parseInt(year));
		w.setMonth(Integer.parseInt(month));
		w.setDay(Integer.parseInt(day));
		/* 2013.05.13 added by shichao for amend weather */
		w.setDayOfWeek(Integer.parseInt(dayofweek));
		String s = modfifyWeatherImage(weather);
		w.setWeather(s);
		w.setImageTitle(s);
		/* end */
		w.setTemperatureRange(Integer.parseInt(highTemp), Integer.parseInt(lowTemp));
		w.setCurrentTemp(Integer.parseInt(currentTemp));
		w.setWind(wind, null);
		w.setmPM(Integer.parseInt(pm2_5));
		w.setQuality(quality);
		return w;
	}

	private String modfifyWeatherImage(String weather) {
		String weatherDao = "到";
		String weatherZhuan = "转";

		if (weather != null && !(weather = weather.trim()).equals("")) {
			int zhuanIndex = -1;
			int daoIndex = -1;

			if ((zhuanIndex = weather.indexOf(weatherZhuan)) > 0) {
				weather = weather.substring(0, zhuanIndex);
			}

			if ((daoIndex = weather.indexOf(weatherDao)) > 0) {
				weather = weather.substring(daoIndex + weatherDao.length(), weather.length());
			}
		}
		return weather;
	}

	private void showWeather() {
		mCity = getJsonValue(mWeatherResultJson, "cityName");
		mCityCode = getJsonValue(mWeatherResultJson, "cityCode");
		
		if (mCity.endsWith("市")) {
			mCity = mCity.substring(0, mCity.lastIndexOf("市"));
		}

		WeatherInfo weatherInfo = new WeatherInfo(mCity, mCityCode);

		JSONArray weatherArray = getJsonArray(mWeatherResultJson, "weatherDays");
		String updateTime = getJsonValue(mWeatherResultJson, "updateTime");
		String focusIndexString = getJsonValue(mWeatherResultJson, "focusDateIndex", "0");
		int focusIndex = Integer.parseInt(focusIndexString);

		weatherInfo.setUpdateTime(updateTime);

		if (weatherArray != null && weatherArray.length() > 0) {
			for (int i = 0; i < weatherArray.length() && i < WeatherInfo.DAY_COUNT; i++) {
				JSONObject object = getJSONObject(weatherArray, i);
				WeatherDay day = getweatherItemFromJsonObject(object);
				if (focusIndex == i) {
					day.setFocusDay();
				}
				// wList.add(day);
				weatherInfo.setWeatherDay(day, i);
			}
		} else {
		    String answer = "获取天气信息异常";
		    addAnswerViewText(answer);
	        playTTS(answer);
	        return;
		}

		WeatherDay weatherDay = weatherInfo.getWeatherFocusDay();
		if (weatherDay == null) {
			weatherDay = weatherInfo.getWeatherDay(0);
		}

		String dayName = "";
		int dayofweek;

		Calendar calendar = Calendar.getInstance();
		calendar.set(weatherDay.getYear(), weatherDay.getMonth() - 1, weatherDay.getDay());
		switch (Util.daysOfTwo(Calendar.getInstance(), calendar)) {
		case 0:
			dayName = "今天";
			break;
		case 1:
			dayName = "明天";
			break;
		case 2:
			dayName = "后天";
			break;
		case 3:
			dayofweek = weatherDay.getDayOfWeek();
			dayName = getWeekName(dayofweek);
			if (dayofweek == 2 || dayofweek == 3 || dayofweek == 4) {
				dayName = "下" + dayName;
			}
			break;
		case 4:
			dayofweek = weatherDay.getDayOfWeek();
			dayName = getWeekName(dayofweek);
			if (dayofweek == 2 || dayofweek == 3 || dayofweek == 4 || dayofweek == 5) {
				dayName = "下" + dayName;
			}
			break;
		default:
			break;
		}

		String showString;
		String tts;
		showString = mCity + dayName + "的天气情况是:";
		tts = mCity + dayName + "的天气情况是," + weatherDay.getWeather() + ",";
		tts += weatherDay.getWind() + weatherDay.getWindExt() + ",";
		//tts += "最高温度," + weatherDay.getHighestTemperature() + "度,";
		//tts += "最低温度," + weatherDay.getLowestTemperature() + "度";
		
		tts += "最高温度," + getTTSTemperature(weatherDay.getHighestTemperature()) + "度,";
		tts += "最低温度," + getTTSTemperature(weatherDay.getLowestTemperature()) + "度";
		if(dayName.equals("今天")){
			tts += weatherDay.getQuality();//getPMStr(weatherDay.getmPM());
		}
		addAnswerViewText(showString);
		playTTS(tts);
		mWeatherView = new WeatherContentView(mContext);
		mWeatherView.setWeatherInfo(weatherInfo);
		addAnswerView(mWeatherView);
	}

	
	/*private String getPMStr(int pmValue){
		String mPMStr = ",空气质量,";
		if(pmValue >= 0 && pmValue <= 50){
			mPMStr += "优";
		}else if(pmValue >= 51 && pmValue <= 100){
			mPMStr += "良"; 
		}else if(pmValue >= 101 && pmValue <= 150){
			mPMStr += "轻度污染";
		}else if(pmValue >= 151 && pmValue <= 200){
			mPMStr += "中度污染";
		}else if(pmValue >= 201 && pmValue <= 300){
			mPMStr += "重度污染";
		}else if(pmValue > 300){
			mPMStr += "严重污染";
		}
		return mPMStr;
	}*/
	
	@Override
	public void release() {
		mWeatherView = null;
		super.release();
	}

	private String getTTSTemperature(int temperature){
		String ttsTemperature ="";
		if(temperature < 0){
			ttsTemperature += "零下"+Math.abs(temperature);
		}else{
			ttsTemperature += temperature;
		}
		
		return ttsTemperature;
	}
	
	private String getWeekName(int dayofweek) {
		switch (dayofweek) {
		case 1:
			return "周日";
		case 2:
			return "周一";
		case 3:
			return "周二";
		case 4:
			return "周三";
		case 5:
			return "周四";
		case 6:
			return "周五";
		case 7:
			return "周六";
		default:
			return "周一";
		}
	}
}
