package com.example.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ViewItem extends TextView{
	private float[] whxy;
	public ViewItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public float[] getWhxy() {
		if(whxy==null){
			FrameLayout.LayoutParams layoutParams=(FrameLayout.LayoutParams)getLayoutParams();
			whxy=new float[4];
			whxy[0]=layoutParams.width;
			whxy[1]=layoutParams.height;
			whxy[2]=layoutParams.leftMargin;
			whxy[3]=layoutParams.topMargin;
		}
		return whxy;
	}
	public void setWhxy(float[] whxy) {
		this.whxy = whxy;
		FrameLayout.LayoutParams layoutParams=(FrameLayout.LayoutParams)getLayoutParams();
		layoutParams.width=(int)whxy[0];
		layoutParams.height=(int)whxy[1];
		layoutParams.topMargin=(int)whxy[3];
		layoutParams.leftMargin=(int)whxy[2];
		this.setLayoutParams(layoutParams);
	}
}
