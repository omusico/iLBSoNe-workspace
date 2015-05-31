package com.ubirtls.particlefilter;
/***
 * 墙壁类（线段）
 * @author 胡旭科
 *
 */
public class Wall {
	/**墙壁的起点和终点*/
	private double startX;
	private double startY;
	private double endX;
	private double endY;
	/**
	 * 构造函数
	 * @param startX 线段起点X坐标
	 * @param startY 线段起点Y坐标
	 * @param endX 线段终点X坐标
	 * @param endY 线段终点Y坐标
	 */
	public Wall(double startX, double startY, double endX, double endY){
		setWall(startX,startY,endX,endY);
	}
	/**
	 * 
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 */
	public void setWall(double startX, double startY, double endX, double endY){
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}
	public double getStartX(){return startX;}
	public double getStartY(){return startY;}
	public double getEndX(){return endX;}
	public double getEndY(){return endY;}

}
