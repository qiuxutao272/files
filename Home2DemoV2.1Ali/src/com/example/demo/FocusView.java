package com.example.demo;


import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class FocusView extends ImageView{

	/**
	 * direction One of FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT, FOCUS_FORWARD,
     * or FOCUS_BACKWARD.
	 */
	private int direction;
	private float n;
	
	public float getN() {
		return n;
	}
	public void setN(float n) {
		this.n = n;
	}
	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public FocusView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	private float[] whxy = new float[4];
	
	public float[] getWhxy() {
		return whxy;
	}
	/**
	 * width, height, x, y
	 * @param whxy
	 */
	public void setWhxy(float[] whxy) {
		this.whxy = whxy;
		FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams((int)whxy[0], (int)whxy[1]);
		layoutParams.gravity=Gravity.TOP|Gravity.LEFT;
		layoutParams.topMargin=(int)whxy[3];
		layoutParams.leftMargin=(int)whxy[2];
		this.setLayoutParams(layoutParams);
	}
	public void setXy(float[]xy){
		this.setX(xy[0]);
		this.setY(xy[1]);
	}
	
	@Override
	public String toString() {
		String s = "x:" + getX() + " ,y:" + getY() + ", width:" + getWidth() + ", height:" + getHeight() + ", ScaleX:" + getScaleX() + " , scaleY: " + getScaleY();
		return s + "\r\n" + super.toString();
	}
	
	
}
