package com.ubirtls.util;

import java.util.ArrayList;
import java.util.List;

public class MyMath {
	/**
	 * 求和函数
	 * 
	 * @param datas
	 * @return
	 */
	public static double sum(List<Double> datas) {
		double sum = 0;
		for (int i = 0; i < datas.size(); i++)
			sum = sum + datas.get(i);
		return sum;
	}

	/** 标准差 */
	public static double std(List<Double> datas) {
		double sumStdSqure = 0;
		double avg = avg(datas);
		for (int i = 0; i < datas.size(); i++) {
			sumStdSqure += (avg - datas.get(i)) * (avg - datas.get(i));
		}
		double std = Math.sqrt(sumStdSqure / datas.size());
		return std;
	}

	/** 平均值 */
	public static double avg(List<Double> datas) {
		return sum(datas) / datas.size();
	}

	/** 最小值 */
	public static double min(List<Double> datas) {
		int size = datas.size();
		double min = datas.get(0);
		for (int i = 1; i < size; i++) {
			if (datas.get(i) < min)
				min = datas.get(i);
		}
		return min;
	}

	/** 最大值 */
	public static double max(List<Double> datas) {
		int size = datas.size();
		double max = datas.get(0);
		for (int i = 1; i < size; i++) {
			if (datas.get(i) > max)
				max = datas.get(i);
		}
		return max;
	}

	/** 子序列浅拷贝 */
	public static ArrayList<double[]> cloneSubList(ArrayList<double[]> datas,
			int start, int end) {
		ArrayList<double[]> result = new ArrayList<double[]>();
		for (int i = start; i <= end; i++) {
			result.add(datas.get(i));
		}
		return result;
	}

	/** 子序列浅拷贝 */
	public static List<Double> cloneSubList(List<Double> datas, int start,
			int end) {
		ArrayList<Double> result = new ArrayList<Double>();
		for (int i = start; i <= end; i++) {
			result.add(datas.get(i));
		}
		return result;
	}

	/** 子序列浅拷贝 */
	public static ArrayList<Double> cloneSubListFirst(
			ArrayList<double[]> datas, int start, int end) {
		ArrayList<Double> result = new ArrayList<Double>();
		if (datas != null && datas.size() >= 0)
			for (int i = start; i <= end && i <= datas.size(); i++) {
				result.add(datas.get(i)[0]);
			}
		return result;
	}

	public static double mod(double x, double y) {
		double temp = x % y;
		if (temp < 0)
			temp = y + temp;
		return temp;
	}
}
