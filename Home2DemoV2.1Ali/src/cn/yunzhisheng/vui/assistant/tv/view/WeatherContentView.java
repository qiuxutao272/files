/**
 * Copyright (c) 2012-2012 Yunzhisheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : WeatherContentView.java
 * @ProjectName : iShuoShuo2
 * @PakageName : cn.yunzhisheng.ishuoshuo.view
 * @Author : Brant
 * @CreateDate : 2012-11-15
 */
package cn.yunzhisheng.vui.assistant.tv.view;

import java.util.Calendar;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.assistant.model.WeatherDay;
import cn.yunzhisheng.vui.assistant.model.WeatherInfo;
import cn.yunzhisheng.vui.assistant.util.Util;

public class WeatherContentView extends FrameLayout implements ISessionView {
	public static final String TAG = "WeatherContentView";
	private Resources res;
	private static HashMap<String, Integer> mBigImageNameIdMap = new HashMap<String, Integer>();

	static {
		mBigImageNameIdMap.put("晴", Res.drawable.ic_weather_sunny_big);
		mBigImageNameIdMap.put("晴天", Res.drawable.ic_weather_sunny_big);
		mBigImageNameIdMap.put("多云", Res.drawable.ic_weather_cloudy_big);
		mBigImageNameIdMap.put("阴", Res.drawable.ic_weather_overcast_big);
		mBigImageNameIdMap.put("阴天", Res.drawable.ic_weather_overcast_big);
		mBigImageNameIdMap.put("雾", Res.drawable.ic_weather_foggy_big);
		mBigImageNameIdMap.put("扬沙", Res.drawable.ic_weather_dustblow_big);
		mBigImageNameIdMap.put("浮尘", Res.drawable.ic_weather_dust_big);
		mBigImageNameIdMap.put("沙尘暴", Res.drawable.ic_weather_sandstorm_big);
		mBigImageNameIdMap.put("强沙尘暴",
				Res.drawable.ic_weather_strong_sandstorm_big);
		mBigImageNameIdMap.put("冻雨", Res.drawable.ic_weather_icerain_big);
		mBigImageNameIdMap.put("阵雨", Res.drawable.ic_weather_shower_big);
		mBigImageNameIdMap.put("雷阵雨", Res.drawable.ic_weather_thunder_rain_big);
		mBigImageNameIdMap.put("雷阵雨伴有冰雹", Res.drawable.ic_weather_hail_big);
		mBigImageNameIdMap.put("雨夹雪", Res.drawable.ic_weather_sleety_big);
		mBigImageNameIdMap.put("小雨", Res.drawable.ic_weather_light_rain_big);
		mBigImageNameIdMap.put("中雨", Res.drawable.ic_weather_moderate_rain_big);
		mBigImageNameIdMap.put("大雨", Res.drawable.ic_weather_heavy_rain_big);
		mBigImageNameIdMap.put("暴雨", Res.drawable.ic_weather_rainstorm_big);
		mBigImageNameIdMap.put("大暴雨", Res.drawable.ic_weather_big_rainstorm_big);
		mBigImageNameIdMap.put("特大暴雨",
				Res.drawable.ic_weather_super_rainstorm_big);
		mBigImageNameIdMap.put("阵雪", Res.drawable.ic_weather_snow_shower_big);
		mBigImageNameIdMap.put("小雪", Res.drawable.ic_weather_light_snow_big);
		mBigImageNameIdMap.put("中雪", Res.drawable.ic_weather_moderate_snow_big);
		mBigImageNameIdMap.put("大雪", Res.drawable.ic_weather_heavy_snow_big);
		mBigImageNameIdMap.put("暴雪", Res.drawable.ic_weather_blizzard_big);
		mBigImageNameIdMap.put("霾", Res.drawable.ic_weather_haze);
		mBigImageNameIdMap.put("雾霾", Res.drawable.ic_weather_haze);
	}

	private TextView mTextViewCity;

	private View mViewSecondDay, mViewThirdDay, mViewFouthDay, mViewFifthDay,
			mViewDivider;

	private TextView
			mTextViewWeatherFirstDayTemperature,
			mTextViewWeatherFirstDayMaxTemperature,
			mTextViewWeatherFirstDayMinTemperature,
			mTextViewWeatherFirstDayWeather, mTextViewWeatherFirstDayWind;

	private TextView mTextViewWeatherSecondDayDatetime,
			mTextViewWeatherSecondDayTemperature,
			mTextViewWeatherSecondDayWeather;

	private ImageView 
			mImageViewWeatherSecondDayWeather,
			mImageViewWeatherThirdDayWeather, mImageViewWeatherFouthDayWeather,
			mImageViewWeatherFifthDayWeather;
	//mImageViewFirstDayWeather,

	private TextView mTextViewWeatherThirdDayDatetime;
	private TextView mTextViewWeatherThirdDayTemperature;
	private TextView mTextViewWeatherThirdDayWeather;
	private TextView mTextViewWeatherFirstDayPm;
	private TextView mTextViewWeatherFirstDayPmName;
	private ImageView mImageViewWeatherFirstDayPm;
	

	private TextView mTextViewWeatherFouthDayDatetime;
	private TextView mTextViewWeatherFouthDayTemperature;
	private TextView mTextViewWeatherFouthDayWeather;
	private TextView mTextViewWeatherFifthDayDatetime;

	private TextView mTextViewWeatherFifthDayTemperature;
	private TextView mTextViewWeatherFifthDayWeather;
	private String[] sDayOfWeekNames;
	private LinearLayout linearLayoutOtherDaysWeather;

	public WeatherContentView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(Res.layout.weather_content_view, this, true);
		res = context.getResources();
		findViews();
		sDayOfWeekNames = context.getResources().getStringArray(
				Res.array.days_of_week);
	}

	public WeatherContentView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public WeatherContentView(Context context) {
		this(context, null);
	}

	private void findViews() {
		
		linearLayoutOtherDaysWeather = (LinearLayout) findViewById(Res.id.linearLayoutOtherDaysWeather);
		
		mTextViewCity = (TextView) findViewById(Res.id.textViewWeatherCity);
		//mTextViewUpdateTime = (TextView) findViewById(Res.id.textViewWeatherUpdateTime);
		//mImageViewFirstDayWeather = (ImageView) findViewById(Res.id.imageViewWeatherFirstDay);
		//mTextViewFirstDayDate = (TextView) findViewById(Res.id.textViewWeatherFirstDayDate);
		mTextViewWeatherFirstDayTemperature = (TextView) findViewById(Res.id.textViewWeatherFirstDayTemperature);
		mTextViewWeatherFirstDayMaxTemperature = (TextView) findViewById(Res.id.textViewWeatherFirstDayMaxTemperature);
		mTextViewWeatherFirstDayMinTemperature = (TextView) findViewById(Res.id.textViewWeatherFirstDayMinTemperature);
		mTextViewWeatherFirstDayWeather = (TextView) findViewById(Res.id.textViewWeatherFirstDayWeather);
		mTextViewWeatherFirstDayWind = (TextView) findViewById(Res.id.textViewWeatherFirstDayWind);
		mTextViewWeatherFirstDayPmName =  (TextView) findViewById(Res.id.textViewWeatherFirstDayPmName);
		mTextViewWeatherFirstDayPm = (TextView) findViewById(Res.id.textViewWeatherFirstDayPm);
		mImageViewWeatherFirstDayPm = (ImageView) findViewById(Res.id.imageViewWeatherFirstDayPm);
		

		mViewDivider = findViewById(Res.id.divider);
		mViewSecondDay = findViewById(Res.id.linearLayoutSecondDay);
		mTextViewWeatherSecondDayDatetime = (TextView) findViewById(Res.id.textViewWeatherSecondDayDatetime);
		mImageViewWeatherSecondDayWeather = (ImageView) findViewById(Res.id.imageViewWeatherSecondDayWeather);
		mTextViewWeatherSecondDayTemperature = (TextView) findViewById(Res.id.textViewWeatherSecondDayTemperature);
		mTextViewWeatherSecondDayWeather = (TextView) findViewById(Res.id.textViewWeatherSecondDayWeather);

		mViewThirdDay = findViewById(Res.id.linearLayoutThirdDay);
		mTextViewWeatherThirdDayDatetime = (TextView) findViewById(Res.id.textViewWeatherThirdDayDatetime);
		mImageViewWeatherThirdDayWeather = (ImageView) findViewById(Res.id.imageViewWeatherThirdDayWeather);
		mTextViewWeatherThirdDayTemperature = (TextView) findViewById(Res.id.textViewWeatherThirdDayTemperature);
		mTextViewWeatherThirdDayWeather = (TextView) findViewById(Res.id.textViewWeatherThirdDayWeather);

		mViewFouthDay = findViewById(Res.id.linearLayoutFouthDay);
		mTextViewWeatherFouthDayDatetime = (TextView) findViewById(Res.id.textViewWeatherFouthDayDatetime);
		mImageViewWeatherFouthDayWeather = (ImageView) findViewById(Res.id.imageViewWeatherFouthDayWeather);
		mTextViewWeatherFouthDayTemperature = (TextView) findViewById(Res.id.textViewWeatherFouthDayTemperature);
		mTextViewWeatherFouthDayWeather = (TextView) findViewById(Res.id.textViewWeatherFouthDayWeather);

		mViewFifthDay = findViewById(Res.id.linearLayoutFifthDay);
		mTextViewWeatherFifthDayDatetime = (TextView) findViewById(Res.id.textViewWeatherFifthDayDatetime);
		mImageViewWeatherFifthDayWeather = (ImageView) findViewById(Res.id.imageViewWeatherFifthDayWeather);
		mTextViewWeatherFifthDayTemperature = (TextView) findViewById(Res.id.textViewWeatherFifthDayTemperature);
		mTextViewWeatherFifthDayWeather = (TextView) findViewById(Res.id.textViewWeatherFifthDayWeather);
		
		
		/*linearLayoutOtherDaysWeather.setFocusable(true);
		mViewSecondDay.setFocusable(true);
		mViewThirdDay.setFocusable(true);
		mViewFouthDay.setFocusable(true);*/

	}

	public void setWeatherInfo(WeatherInfo weatherInfo) {
		WeatherDay todayWeather = weatherInfo.getWeatherDay(0);
		int toDayOfWeek=todayWeather.getDayOfWeek();
		
		mTextViewCity.setText(weatherInfo.getCityName());

		Calendar calendar = Calendar.getInstance();
		calendar.set(todayWeather.getYear(), todayWeather.getMonth() - 1,
				todayWeather.getDay());
		int days = Util.daysOfTwo(Calendar.getInstance(), calendar);
		String date = "";
		if (days == 0) {
			date = "今天 "
					+ sDayOfWeekNames[(weatherInfo.getWeatherDay(0)
							.getDayOfWeek() + 6) % 7];
		}
		mTextViewWeatherFirstDayTemperature.setText(todayWeather
				.getTemperatureRangeLarge());

		mTextViewWeatherFirstDayWeather.setText(todayWeather.getWeather());
		mTextViewWeatherFirstDayMaxTemperature.setText(todayWeather
				.getHighestTemperature() + "℃");
		mTextViewWeatherFirstDayMinTemperature.setText(todayWeather
				.getLowestTemperature() + "℃");
		mTextViewWeatherFirstDayWind.setText(todayWeather.getWind()
				+ todayWeather.getWindExt());

		WeatherDay weatherSecondDay = weatherInfo.getWeatherDay(1);

		if (weatherSecondDay == null) {
			mViewSecondDay.setVisibility(View.GONE);
		} else { 
			if(weatherSecondDay.getDayOfWeek()==toDayOfWeek){
				mViewSecondDay.setBackgroundResource(Res.drawable.view_disabled_foused_bk);
				mTextViewWeatherSecondDayDatetime.setTextColor(getResources().getColor(android.R.color.white));
				mTextViewWeatherSecondDayTemperature.setTextColor(getResources().getColor(android.R.color.white));
				mTextViewWeatherSecondDayWeather.setTextColor(getResources().getColor(android.R.color.white));
				mTextViewWeatherFirstDayPmName.setVisibility(View.VISIBLE);
				mTextViewWeatherFirstDayPm.setVisibility(View.VISIBLE);
				mImageViewWeatherFirstDayPm.setVisibility(View.VISIBLE);
				mTextViewWeatherFirstDayPm.setText(""+todayWeather.getmPM());
				mImageViewWeatherFirstDayPm.setImageResource(getPMImge(todayWeather.getQuality()));
				mTextViewWeatherFirstDayWind.setWidth(res.getDimensionPixelSize(Res.dimen.poi_sort_list_item_width));
				mTextViewWeatherFirstDayWind.setSingleLine();
				mTextViewWeatherFirstDayWind.setEllipsize(TruncateAt.MARQUEE);
				mTextViewWeatherFirstDayWind.setFocusable(true);
				mTextViewWeatherFirstDayWind.setFocusableInTouchMode(true);
				mTextViewWeatherFirstDayWind.setMarqueeRepeatLimit(-1);
			}
			
			
			mTextViewWeatherSecondDayDatetime
					.setText(sDayOfWeekNames[(weatherSecondDay.getDayOfWeek() + 6) % 7]); 
			mImageViewWeatherSecondDayWeather
					.setImageResource(getWeatherImage(weatherSecondDay
							.getImageTitle()));
			mTextViewWeatherSecondDayTemperature.setText(weatherSecondDay
					.getTemperatureRange());  
			mTextViewWeatherSecondDayWeather.setText(weatherSecondDay
					.getWeather());
			
		}

		WeatherDay weatherThirdDay = weatherInfo.getWeatherDay(2);

		if (weatherThirdDay == null) {
			mViewThirdDay.setVisibility(View.GONE);
		} else {
			if(weatherThirdDay.getDayOfWeek()==toDayOfWeek){
				mViewThirdDay.setBackgroundResource(Res.drawable.view_disabled_foused_bk);
				mTextViewWeatherThirdDayDatetime.setTextColor(getResources().getColor(android.R.color.white));
				mTextViewWeatherThirdDayTemperature.setTextColor(getResources().getColor(android.R.color.white));
				mTextViewWeatherThirdDayWeather.setTextColor(getResources().getColor(android.R.color.white));
			}
			
			
			mTextViewWeatherThirdDayDatetime
					.setText(sDayOfWeekNames[(weatherThirdDay.getDayOfWeek() + 6) % 7]);
			mImageViewWeatherThirdDayWeather
					.setImageResource(getWeatherImage(weatherThirdDay
							.getImageTitle()));
			mTextViewWeatherThirdDayTemperature.setText(weatherThirdDay
					.getTemperatureRange());
			mTextViewWeatherThirdDayWeather.setText(weatherThirdDay
					.getWeather());
		}

		WeatherDay weatherFouthDay = weatherInfo.getWeatherDay(3);
		if (weatherFouthDay == null) {
			mViewFouthDay.setVisibility(View.GONE);
		} else {
			if(weatherFouthDay.getDayOfWeek()==toDayOfWeek){
				mViewFouthDay.setBackgroundResource(Res.drawable.view_disabled_foused_bk);
				mTextViewWeatherFouthDayDatetime.setTextColor(getResources().getColor(android.R.color.white));
				mTextViewWeatherFouthDayTemperature.setTextColor(getResources().getColor(android.R.color.white));
				mTextViewWeatherFouthDayWeather.setTextColor(getResources().getColor(android.R.color.white));
			}
			mTextViewWeatherFouthDayDatetime
					.setText(sDayOfWeekNames[(weatherFouthDay.getDayOfWeek() + 6) % 7]);
			mImageViewWeatherFouthDayWeather
					.setImageResource(getWeatherImage(weatherFouthDay
							.getImageTitle()));
			mTextViewWeatherFouthDayTemperature.setText(weatherFouthDay
					.getTemperatureRange());
			mTextViewWeatherFouthDayWeather.setText(weatherFouthDay
					.getWeather());
		}

		WeatherDay weatherFifthDay = weatherInfo.getWeatherDay(4);

		if (weatherFifthDay == null) {
			mViewFifthDay.setVisibility(View.GONE);
		} else {
			if(weatherFifthDay.getDayOfWeek()==toDayOfWeek){
				mViewFifthDay.setBackgroundResource(Res.drawable.view_disabled_foused_bk);
				mTextViewWeatherFifthDayDatetime.setTextColor(getResources().getColor(android.R.color.white));
				mTextViewWeatherFifthDayTemperature.setTextColor(getResources().getColor(android.R.color.white));
				mTextViewWeatherFifthDayWeather.setTextColor(getResources().getColor(android.R.color.white));
			} 
			
			mTextViewWeatherFifthDayDatetime
					.setText(sDayOfWeekNames[(weatherFifthDay.getDayOfWeek() + 6) % 7]);
			mImageViewWeatherFifthDayWeather
					.setImageResource(getWeatherImage(weatherFifthDay
							.getImageTitle()));
			mTextViewWeatherFifthDayTemperature.setText(weatherFifthDay
					.getTemperatureRange());
			mTextViewWeatherFifthDayWeather.setText(weatherFifthDay
					.getWeather());
		}
	}

	private Integer getWeatherImage(String imgTitle) {
		return mBigImageNameIdMap.containsKey(imgTitle) ? mBigImageNameIdMap
				.get(imgTitle) : mBigImageNameIdMap.get("阴");
	}
	
	private Integer getPMImge(String quality){
		int mResId = Res.drawable.pm_2_5_1;
		if(quality.equals("优")){
			mResId = Res.drawable.pm_2_5_1;
		}else if(quality.equals("良")){
			mResId = Res.drawable.pm_2_5_2;
		}else if(quality.equals("轻度污染")){
			mResId = Res.drawable.pm_2_5_3;
		}else if(quality.equals("中度污染")){
			mResId = Res.drawable.pm_2_5_4;
		}else if(quality.equals("重度污染")){
			mResId = Res.drawable.pm_2_5_5;
		}else if(quality.equals("严重污染")){
			mResId = Res.drawable.pm_2_5_6;
		}
		return mResId;
	}
	
	
	/*private Integer getPMImge(int pmValue){
		int mResId = Res.drawable.view_weather_pm_2_5_1;
		if(pmValue >= 0 && pmValue <= 50){
			mResId = Res.drawable.view_weather_pm_2_5_1;
		}else if(pmValue >= 51 && pmValue <= 100){
			mResId = Res.drawable.view_weather_pm_2_5_2;
		}else if(pmValue >= 101 && pmValue <= 150){
			mResId = Res.drawable.view_weather_pm_2_5_3;
		}else if(pmValue >= 151 && pmValue <= 200){
			mResId = Res.drawable.view_weather_pm_2_5_4;
		}else if(pmValue >= 201 && pmValue <= 300){
			mResId = Res.drawable.view_weather_pm_2_5_5;
		}else if(pmValue > 300){
			mResId = Res.drawable.view_weather_pm_2_5_6;
		}
		return mResId;
	}*/

	@Override
	public boolean isTemporary() {
		return false;
	}

	@Override
	public void requestSuperFocus() {
		//linearLayoutOtherDaysWeather.requestFocus();
		mTextViewWeatherFirstDayWind.requestFocus();
	}

	/**
	 * @Description	: release
	 * @Author		: Dancindream
	 * @CreateDate	: 2013-12-19
	 * @see cn.yunzhisheng.vui.assistant.tv.view.ISessionView#release()
	 */
	@Override
	public void release() {
		
	}

}
