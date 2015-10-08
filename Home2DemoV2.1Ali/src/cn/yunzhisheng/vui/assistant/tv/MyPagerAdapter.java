/**
 * Copyright (c) 2012-2013 YunZhiSheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : MyPagerAdapter.java
 * @ProjectName : Voizard
 * @PakageName : cn.yunzhisheng.voizard.view
 * @Author : Alieen
 * @CreateDate : 2013-4-18
 */
package cn.yunzhisheng.vui.assistant.tv;

import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @Module : 隶属模块名
 * @Comments : 描述
 * @Author : Alieen
 * @CreateDate : 2013-4-18
 * @ModifiedBy : Alieen
 * @ModifiedDate: 2013-4-18
 * @Modified:
 * 2013-4-18: 实现基本功能
 */
public class MyPagerAdapter extends PagerAdapter {
	public static final String TAG = "MyPagerAdapter";

	public List<View> mListViews;

	public MyPagerAdapter(List<View> mListViews) {
		this.mListViews = mListViews;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView(mListViews.get(arg1));
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public int getCount() {
		return mListViews.size();
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
		((ViewPager) arg0).addView(mListViews.get(arg1), 0);
		return mListViews.get(arg1);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

}
