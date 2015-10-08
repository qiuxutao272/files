/**
 * Copyright (c) 2012-2012 Yunzhisheng(Shanghai) Co.Ltd. All right reserved.
 * @FileName : PickAppView.java
 * @ProjectName : iShuoShuo2
 * @PakageName : cn.yunzhisheng.ishuoshuo.view
 * @Author : Brant
 * @CreateDate : 2012-11-15
 */
package cn.yunzhisheng.vui.assistant.tv.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.yunzhisheng.vui.assistant.Res;
import cn.yunzhisheng.vui.modes.AppInfo;

public class PickAppView extends PickBaseView {
	public static final String TAG = "PickAppView";
	private int mItemPaddingLeft, mItemPaddingRight, mItemPaddingTop, mItemPaddingBottom;

	public PickAppView(Context context) {
		super(context);
		mContainer.setBackgroundResource(Res.drawable.ic_content_view_body_bg);
		Resources res = getResources();
		mItemPaddingLeft = res.getDimensionPixelSize(Res.dimen.function_item_padding_left);
		mItemPaddingRight = res.getDimensionPixelSize(Res.dimen.function_item_padding_right);
		mItemPaddingTop = 0;
		mItemPaddingBottom = 0;
	}

	public void initView(ArrayList<AppInfo> appInfos) {
		PackageManager packageManager = getContext().getPackageManager();
		for (AppInfo appInfo : appInfos) {
			View view = mLayoutInflater.inflate(Res.layout.pickview_item_app, this, false);
			TextView tvName = (TextView) view.findViewById(Res.id.textViewAppName);
			tvName.setText(appInfo.mAppLabel);
			ImageView imageViewApp = (ImageView) view.findViewById(Res.id.imageViewAppIcon);
			Drawable drawable = null;
			try {
				drawable = packageManager.getApplicationIcon(appInfo.mPackageName);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}

			if (drawable != null) {
				imageViewApp.setImageDrawable(drawable);
			} else {
				imageViewApp.setImageResource(0);
			}

			View divider = view.findViewById(Res.id.divider);
			if (getItemCount() == appInfos.size() - 1) {
				divider.setVisibility(View.GONE);
				view.setBackgroundResource(Res.drawable.list_item_down_bg);
			} else {
				view.setBackgroundResource(Res.drawable.list_item_mid_bg);
			}

			view.setPadding(mItemPaddingLeft, mItemPaddingTop, mItemPaddingRight, mItemPaddingBottom);
			view.setClickable(true);
			view.setFocusable(true);
			addItem(view);
		}

	}

}
