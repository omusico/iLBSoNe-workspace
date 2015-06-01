package com.ubirtls.tools;

public class LocationProjection {
	private static double pre_B = 0;
	private static double per_L = 0;
	private static double cur_B = 0;
	private static double cur_L = 0;
	private double a = 0;
	private static double r = 0;
	private static final double PI = 3.1415926;

	private LocationProjection() {
	}

	/**
	 * ��γ��
	 * 
	 * @param pre_B
	 *            ��ʼγ��
	 * @param pre_L
	 *            ��ʼ����
	 * @param positionx
	 *            �����X����
	 * @param positiony
	 *            �����Y����
	 * @param a
	 *            �������������ƫ��
	 * @return
	 */
	public static double[] Local2WGS84(double pre_B, double pre_L,
			double positionx, double positiony, double a) {

		r = Math.pow((positionx * positionx + positiony * positiony), 2);

		cur_L = per_L + (r * Math.sin(a * PI / 180))
				/ (111000 * Math.cos(pre_B * PI / 180));
		cur_B = pre_B + (r * Math.cos(a * PI / 180)) / 111000;
		double[] temp = new double[2];
		temp[0] = cur_L;
		temp[1] = cur_B;
		return temp;
	}
}