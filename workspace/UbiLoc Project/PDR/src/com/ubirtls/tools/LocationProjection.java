package com.ubirtls.tools;

public class LocationProjection {
	private static double pre_B = 0;
	private static double per_L = 0;
	private static double cur_B = 0;
	private static double cur_L = 0;
	private double a = 0;
	private static double r = 0;
	private static final double PI = 3.14159265358979;

	private LocationProjection() {
	}

	/**
	 * 经纬度
	 * 
	 * @param pre_B
	 *            初始纬度
	 * @param pre_L
	 *            初始经度
	 * @param positionx
	 *            相对于X坐标
	 * @param positiony
	 *            相对于Y坐标
	 * @param a
	 *            相对于正北方的偏角
	 * @return
	 */
	public static double[] Local2WGS84(double pre_B, double pre_L,
			double positionx, double positiony, double a) {

		r = Math.pow((positionx * positionx + positiony * positiony), 2);

		a = Math.atan(positionx / positiony);

		cur_L = pre_L + (r * Math.sin(a))
				/ ((PI * 12630824 * 1 / 360) * Math.cos(pre_B * PI / 180));
		cur_B = pre_B + (r * Math.cos(a)) / (PI * 12630824 * 1 / 360);

		double[] temp = new double[3];
		temp[0] = cur_L;
		temp[1] = cur_B;
		temp[2] = a;
		return temp;
	}
}