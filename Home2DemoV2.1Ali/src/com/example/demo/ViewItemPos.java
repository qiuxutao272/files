package com.example.demo;

public class ViewItemPos {
	private int leftMargin;
	private int topMargin;
	private int width;
	private int height;
	public ViewItemPos(int width,int height,int leftMargin,int topMargin){
		this.width=width;
		this.height=height;
		this.leftMargin=leftMargin;
		this.topMargin=topMargin;
	}
	
	public int getLeftMargin(){
		return this.leftMargin;
	}
	
	public int getTopMargin(){
		return this.topMargin;
	}
	
	public int getWidth(){
		return this.width;
	}
	
	public int getHeight(){
		return this.height;
	}
}
