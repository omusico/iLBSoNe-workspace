package com.ubirtls.PDR;

import java.util.ArrayList;

/**
 * 转角类，描述转角的位置，及转角方向
 * @author 胡旭科
 *
 */
public class Corner {
	private double x;
	private double y;
	ArrayList<Double[]> orientation = new ArrayList<Double[]>();
	/***
	 * 构造函数
	 * @param x 转角的x坐标
	 * @param y 转角的y坐标
	 * @param ori 转角的方向
	 */
	public Corner(double x,double y,ArrayList<Double[]> ori){
		this.x = x;
		this.y = y;
		for(int i = 0; i < ori.size(); i++){
			this.orientation.add(ori.get(i));
		}
	}
	public double getX(){return x;}
	public double getY(){return y;}
	public ArrayList<Double[]> getOri(){return this.orientation;}

}
