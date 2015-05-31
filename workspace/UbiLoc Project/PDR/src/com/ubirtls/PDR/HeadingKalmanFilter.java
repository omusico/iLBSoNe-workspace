package com.ubirtls.PDR;

import java.util.ArrayList;

import com.ubirtls.util.MyMath;

/**
 * 陀螺仪、电子罗盘数据通过卡尔曼滤波获得较准确的步行朝向
 * 
 * @author 胡旭科
 * 
 */
public class HeadingKalmanFilter {
	/**
	 * 
	 */
	private static final double PI = 3.1415926;
	/** 卡尔曼滤波参数 */
	private double Q = 0.00085;
	private double R = 9.0657;
	private double pre_p = 0.7;
	private double cur_p;

	private double pre_ori = 90; // 初始朝向

	private double cur_ori;
	private double kg = 0;
	/** 当前罗盘数据对应的陀螺仪数据起点与终点 */
	private int gyroStart = 0;
	private int gyroEnd;
	private double gyroHZ = 0.0667;// 0.0458;

	/**
	 * 构造函数
	 * 
	 * @param q
	 * @param r
	 * @param pre_p
	 * @param pre_ori
	 * @param kg
	 *            卡尔曼增益
	 */
	public HeadingKalmanFilter(double q, double r, double pre_p,
			double pre_ori, double gyroHZ) {
		Q = q;
		R = r;
		this.pre_p = pre_p;
		this.pre_ori = pre_ori;
		this.gyroHZ = gyroHZ;
	}

	public HeadingKalmanFilter(double pre_p, double pre_ori, double gyroHZ) {
		this.pre_p = pre_p;
		this.pre_ori = pre_ori;
		this.gyroHZ = gyroHZ;

	}

	/***
	 * 
	 * @param pre_ori
	 *            方向初始值
	 * @param gyroHZ
	 *            每个陀螺仪数据对应的时间间隔
	 */
	public HeadingKalmanFilter(double pre_ori, double gyroHZ) {
		this.pre_ori = pre_ori;
		this.gyroHZ = gyroHZ;
	}

	public HeadingKalmanFilter(double pre_ori) {
		double temp = (90 - (pre_ori - 10)) % 360;// (90-(360 + 260 -
													// pre_ori))%360;
		if (temp < 0)
			this.pre_ori = 360 + temp;
		else
			this.pre_ori = temp;

	}

	public double mod(double x, double y) {
		double temp = x % y;
		if (temp < 0)
			temp = y + temp;
		return temp;
	}

	/** 启动卡尔曼滤波 */
	public double getFilteredHeading(ArrayList<Double> oriX,
			ArrayList<Double> gyroZ) {
		int oriSize = oriX.size();
		int gyroSize = gyroZ.size();
		/** 对方向数据进行处理,电子罗盘获得方位需进行校准（三星手机） */
		for (int i = 0; i < oriSize; i++) {
			double temp = (90 - (oriX.get(i) - 10)) % 360; // (90-(360 + 260 -
															// oriX.get(i)))%360;
			if (temp < 0)
				oriX.set(i, 360 + temp);
			else
				oriX.set(i, temp);
		}
		oriX.set(0, pre_ori);
		/** 开始进行滤波 */
		for (int k = 1; k < oriSize; k++) {
			gyroEnd = (int) ((gyroSize - 1) * k / (oriSize - 1));
			/** 状态预测 */
			double dQ = MyMath.sum(MyMath.cloneSubList(gyroZ, gyroStart,
					gyroEnd)) * gyroHZ * 180 / PI;
			cur_ori = pre_ori + dQ;
			cur_p = pre_p + Q;
			kg = cur_p / (cur_p + R);
			if (Math.abs(oriX.get(k) - cur_ori) > 180) {
				if (oriX.get(k) < cur_ori)
					cur_ori = mod(cur_ori + kg * (oriX.get(k) + 360 - cur_ori),
							360);
				else
					cur_ori = mod(cur_ori + kg * (oriX.get(k) - cur_ori - 360),
							360);
			} else
				cur_ori = mod(cur_ori + kg * (oriX.get(k) - cur_ori), 360);
			cur_p = (1 - kg) * cur_p;
			pre_ori = cur_ori;
			pre_p = cur_p;
			gyroStart = gyroEnd + 1;
		}
		gyroStart = 0;

		return cur_ori;
	}

}
