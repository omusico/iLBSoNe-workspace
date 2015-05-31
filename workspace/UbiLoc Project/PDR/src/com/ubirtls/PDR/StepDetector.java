/**
 * 
 */
package com.ubirtls.PDR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.ubirtls.util.MyMath;

/**
 * 捕获步行事件,并计算每步的步长
 * 
 * @author 胡旭科
 * 
 */
public class StepDetector {
	/** 正常行走一步采集的加速度测量值最小/大数 */
	private int sampleMinCount = 14;
	private int sampleMaxCount = 22;
	/** 滑动平滑窗口 */
	int w = 2;
	/** 保存数据用于下次计算 */
	private double[] smoothWindows = new double[sampleMaxCount - sampleMinCount
			+ w];
	private double[] preWindows = new double[w];

	/** 高通滤波器参数 */
	private double highPassA = 0.9;
	private double ACC_HIGH_PASS;
	private double[] gaosiACC = new double[sampleMaxCount - sampleMinCount + 1];
	boolean isFirst = true;
	/** 标准差阈值以及自相关系数阈值 用于确定步行事件是否发生 */
	private double stdThreshold = 0.2;
	private double cofThreshold = 0.7;
	/***/
	private boolean boolLastMatch = false;
	private double K = 0.56; // 步长参数

	/*
	 * private double magnitudeAcc = 0;
	 */
	/** 构造函数 */
	public StepDetector(int minCount, int maxCount, int w, int highPassA) {
		this.sampleMinCount = minCount;
		this.sampleMaxCount = maxCount;
		this.w = w;
		this.highPassA = highPassA;
	}

	public StepDetector() {
	}

	/**
	 * 获得一步的最大加速度测量值数量
	 * 
	 * @return
	 */
	public int getStepSampleCount() {
		if (isFirst)
			return sampleMaxCount * 2 + w + 1;
		else
			return sampleMaxCount * 2 + w;
	}

	/**
	 * 获得滑动平滑滤波窗口大小W
	 * 
	 * @return滤波窗口大小W
	 */
	public int getSmoothWindowSize() {
		return w;
	}

	public StepDetectResult detectStep(List<double[]> accS) {
		/* 计算总价速度值 */
		List<Double> magnitudeOfAcceleration = getMagnitudeOfAcceleration(accS);
		/* 高通滤波器处理 */
		List<Double> highPassFilteredAcc = highPassFilter(magnitudeOfAcceleration);
		if (!isFirst) {
			for (int i = 0; i < w; i++) {
				highPassFilteredAcc.add(i, this.preWindows[i]);
			}
		}
		/* 低通滤波器处理 */
		List<Double> localMeanAcceleration = getLocalMeanAcceleration(highPassFilteredAcc);
		StepDetectResult result = new StepDetectResult(false, 1, 1, 1);
		int accSize = localMeanAcceleration.size();
		int m = 0;
		/*
		 * if(m+2 * sampleMinCount > accSize) Log.i("accErrorErrorrrrrrrrrrrrr",
		 * String.valueOf(accSize));
		 */
		if (m + 2 * sampleMinCount <= accSize) {
			/* %选择自相关系数最大的SampleCount_opt，t_min:t_max */
			double cof = 0.;
			int sampleCountTemp = sampleMinCount;
			for (int sampleCountOpt = sampleMinCount; sampleCountOpt <= sampleMaxCount; sampleCountOpt++) {
				if (m + 2 * sampleCountOpt <= accSize) {
					double cofTemp = correlation(MyMath.cloneSubList(
							localMeanAcceleration, m, m + 2 * sampleCountOpt
									- 1));
					if (cofTemp > cof) {
						cof = cofTemp;
						sampleCountTemp = sampleCountOpt;
					}
				} else
					break;
			}
			/** 返回需要删除的样本数目 */
			int deletedSampleCount;
			if (isFirst)
				deletedSampleCount = sampleCountTemp + 1;
			else
				deletedSampleCount = sampleCountTemp;

			/* 求这段加速度序列的标准差 判断用户是否处于静止状态 */
			double stdAcc = MyMath.std(MyMath.cloneSubList(
					localMeanAcceleration, m, m + 2 * sampleCountTemp - 1));
			if (stdAcc <= stdThreshold) {
				boolLastMatch = false;
				result.setResult(false, 0, 0, deletedSampleCount);
			} else if (cof >= cofThreshold) {
				boolLastMatch = true;
				int stepOneStPoint = m;
				int stepOneEndPoint = m + (int) (sampleCountTemp / 2) - 1;
				int stepTwoStPoint = m + (int) (sampleCountTemp / 2);
				int stepTwoEndPoint = m + sampleCountTemp - 1;
				/* 计算第一步步长 */
				double stepOneLength = CalculateStrideLength(MyMath.max(MyMath
						.cloneSubList(localMeanAcceleration, stepOneStPoint,
								stepOneEndPoint)), MyMath.min(MyMath
						.cloneSubList(localMeanAcceleration, stepOneStPoint,
								stepOneEndPoint)));

				/* 计算第二步步长 */
				double stepTwoLength = CalculateStrideLength(MyMath.max(MyMath
						.cloneSubList(localMeanAcceleration, stepTwoStPoint,
								stepTwoEndPoint)), MyMath.min(MyMath
						.cloneSubList(localMeanAcceleration, stepTwoStPoint,
								stepTwoEndPoint)));
				result = new StepDetectResult(true, stepOneLength,
						stepTwoLength, deletedSampleCount);
			} else if (boolLastMatch) {
				boolLastMatch = false;
				int stepOneStPoint = m;
				int stepOneEndPoint = m + (int) (sampleCountTemp / 2) - 1;
				int stepTwoStPoint = m + (int) (sampleCountTemp / 2);
				int stepTwoEndPoint = m + sampleCountTemp - 1;
				/* 计算第一步步长 */
				double stepOneLength = CalculateStrideLength(MyMath.max(MyMath
						.cloneSubList(localMeanAcceleration, stepOneStPoint,
								stepOneEndPoint)), MyMath.min(MyMath
						.cloneSubList(localMeanAcceleration, stepOneStPoint,
								stepOneEndPoint)));

				/* 计算第二步步长 */
				double stepTwoLength = CalculateStrideLength(MyMath.max(MyMath
						.cloneSubList(localMeanAcceleration, stepTwoStPoint,
								stepTwoEndPoint)), MyMath.min(MyMath
						.cloneSubList(localMeanAcceleration, stepTwoStPoint,
								stepTwoEndPoint)));
				result = new StepDetectResult(true, stepOneLength,
						stepTwoLength, deletedSampleCount);
			} else {
				result = new StepDetectResult(false, 0, 0, deletedSampleCount);
				boolLastMatch = false;
			}
			ACC_HIGH_PASS = this.gaosiACC[sampleCountTemp - this.sampleMinCount];
			preWindows[0] = smoothWindows[sampleCountTemp - this.sampleMinCount];
			preWindows[1] = smoothWindows[sampleCountTemp - this.sampleMinCount
					+ 1];

		}
		if (isFirst)
			isFirst = false;
		return result;
	}

	/**
	 * Compute the magnitude of the acceleration for every sample
	 * 
	 * @return
	 * @throws IOException
	 */
	private List<Double> getMagnitudeOfAcceleration(List<double[]> accelerometer) {
		// acc: acceleration of x, y, z
		double[] acc = new double[4];
		// macc: magnitude of the acceleration
		double macc = 0;
		List<Double> magnitudeAcc = new ArrayList<Double>();
		Iterator<double[]> it = accelerometer.iterator();
		while (it.hasNext()) {
			acc = it.next();
			macc = Math.sqrt(acc[1] * acc[1] + acc[2] * acc[2] + acc[0]
					* acc[0]);
			magnitudeAcc.add(macc);
		}
		return magnitudeAcc;
	}

	/**
	 * To eliminate the influence of gravity, the signal is filtered with
	 * high-pass filtering
	 * 
	 * @param magnitudeOfAcceleration
	 * @return 通过高通滤波过滤后的加速度值
	 */
	private List<Double> highPassFilter(List<Double> magnitudeOfAcceleration) {
		List<Double> hmacc = new ArrayList<Double>();
		if (isFirst) {
			ACC_HIGH_PASS = magnitudeOfAcceleration.get(0);
		}
		for (int i = 0; i < magnitudeOfAcceleration.size(); i++) {
			/* 高通滤波器的核心算法 */
			ACC_HIGH_PASS = magnitudeOfAcceleration.get(i) * (1 - highPassA)
					+ ACC_HIGH_PASS * highPassA;
			/* 第一个数据损坏，直接丢掉 */
			if ((i != 0) || (!isFirst))
				hmacc.add(magnitudeOfAcceleration.get(i) - ACC_HIGH_PASS);
			int first = 0;
			if (isFirst)
				first = 1;
			if (i >= (this.sampleMinCount - 1 + first)
					&& (i <= this.sampleMaxCount - 1 + first))
				this.gaosiACC[i - sampleMinCount + 1 - first] = ACC_HIGH_PASS;
		}
		return hmacc;
	}

	/**
	 * Compute the local mean acceleration,moving average filter,基于滑动窗口的滤波器
	 * 
	 * @param magnitudeOfAcceleration
	 * @return
	 */
	private List<Double> getLocalMeanAcceleration(
			List<Double> magnitudeOfAcceleration) {
		// macc: magnitude of the acceleration
		List<Double> macc = new ArrayList<Double>();
		macc = magnitudeOfAcceleration;
		// lmacc: local mean acceleration value
		List<Double> lmacc = new ArrayList<Double>();
		double sum = 0.;
		for (int i = 0, size = macc.size(); i < size - w; i++) {
			// 平滑最前端数据
			if (i < w) {
				if (isFirst)
					lmacc.add(macc.get(i));
			}
			// 平滑中间数据
			else {
				sum = macc.get(i);
				for (int j = 1; j <= w; j++) {
					sum += macc.get(i + j) + macc.get(i - j);
				}
				lmacc.add(sum / (2 * w + 1));
				if ((i >= sampleMinCount) && (i <= w + sampleMaxCount - 1))
					this.smoothWindows[i - sampleMinCount] = sum / (2 * w + 1);
			}
		}
		return lmacc;
	}

	/**
	 * 计算自相关性
	 * 
	 * @param accS
	 * @return 自相关系数
	 */
	private double correlation(List<Double> accS) {
		double l = 0, n = 0;
		int t = accS.size() / 2;
		for (int k = 0; k < t; k++) {
			l = l + accS.get(k) * accS.get(k + t);
			n = n + accS.get(k) * accS.get(k) + accS.get(k + t)
					* accS.get(k + t);
		}
		return Math.abs(l) * 2 / n;
	}

	/**
	 * 计算步长
	 * 
	 * @param maxA
	 *            最大A值
	 * @param minA
	 *            最下加速度值
	 * @return
	 */
	public double CalculateStrideLength(double maxA, double minA) {
		return K * Math.pow(maxA - minA, 0.25);
	}

}
