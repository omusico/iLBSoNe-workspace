/**
 * 
 */
package com.ubirtls.particlefilter;

import java.util.ArrayList;

/**
 * 粒子滤波类
 * 
 * @author 胡旭科
 * 
 */
public class ParticleFilter {
	/** 初始位置 */
	private double initX;

	public double getInitX() {
		return initX;
	}

	public void setInitX(double initX) {
		this.initX = initX;
	}

	private double initY;

	public double getInitY() {
		return initY;
	}

	public void setInitY(double initY) {
		this.initY = initY;
	}

	/** 用于滤波的粒子样本个数 */
	private int particleSize = 50;
	/** 存放所有粒子 */
	private ArrayList<Particle> particles = null;
	private double pi = 3.1415926;
	private double e = 0.000003;
	private double threshold_w = 0;
	private boolean boolWiFiEnable = false;
	/** 正太分布标准差 */
	private static final double std = 1;

	/**
	 * 构造函数
	 * 
	 * @param init_x
	 *            初始位置 x
	 * @param init_y
	 *            初始位置 y
	 * @param particleNumber
	 *            粒子个数
	 */
	public ParticleFilter(double init_x, double init_y, int particleNumber) {
		this.initX = init_x;
		this.initY = init_y;
		this.particleSize = particleNumber;
	}

	/**
	 * 构造函数 此时默认的粒子样本个数为60
	 * 
	 * @param init_x
	 *            初始位置 x
	 * @param init_y
	 *            初始位置 y
	 */
	public ParticleFilter(double init_x, double init_y) {
		this.initX = init_x;
		this.initY = init_y;
	}

	/**
	 * 设置初始位置 可通过Wi-Fi指纹方法确定初始位置
	 * 
	 * @param init_x
	 *            初始x位置
	 * @param init_y
	 *            初始 y位置
	 */
	public void setInitLocation(double init_x, double init_y) {
		this.initX = init_x;
		this.initY = init_y;
	}

	public void enableWiFi() {
		this.boolWiFiEnable = true;
	}

	/** 根据目标的初始位置初始化所有粒子 */
	public void init() {
		particles = new ArrayList<Particle>(particleSize);
		for (int i = 0; i < particleSize; i++) {
			/*-1到+1之间的随机数*/
			double randX = 2 * Math.random() - 1;
			double randY = 2 * Math.random() - 1;
			/* 保证初始化的粒子不会穿过墙壁 */
			while (WallDetector.getInstance().wallHittingDetect(initX, initY,
					initX + randX, initY + randY)) {
				randX = 2 * Math.random() - 1;
				randY = 2 * Math.random() - 1;
			}
			particles.add(new Particle(initX + randX, initY + randY,
					1.0 / particleSize));
		}
	}

	/**
	 * 采样，每个粒子按照PDR进行传播，对于撞墙的粒子将其权重设置为0
	 * 
	 * @param stepLength
	 *            PDR计算的步长
	 * @param heading
	 *            PDR计算的朝向
	 */
	public void sampling(double stepLength, double heading) {
		/** 每个粒子按照PDR进行传播，对于撞墙的粒子将其权重设置为0 */
		for (int i = 0; i < particleSize; i++) {
			double curX = particles.get(i).getX() + stepLength
					* Math.cos(heading * pi / 180);
			double curY = particles.get(i).getY() + stepLength
					* Math.sin(heading * pi / 180);
			boolean isHittingWall = WallDetector.getInstance()
					.wallHittingDetect(particles.get(i).getX(),
							particles.get(i).getY(), curX, curY);
			if (isHittingWall)
				particles.get(i).setWeight(0);
			particles.get(i).setCoorXY(curX, curY);
		}
	}

	/**
	 * 计算每个粒子的权重（wi-fi获得的位置信息，以及利用墙壁限制），利用加权平均获得目标的位置
	 * 
	 * @return 目标的位置
	 */
	public double[] weightComputing(double wifiX, double wifiY) {
		/** 首先判断wifi结果是否可用，wifi定位间隔较长5s，在没有结果返回期间，仅利用墙壁限制更新粒子权值 */
		if (boolWiFiEnable) {
			for (int i = 0; i < particleSize; i++) {
				/** 对于撞墙的粒子不再通过wifi进行权值的更新 */
				if (particles.get(i).getWeight() != 0) {
					double distance = Math.sqrt((wifiX - particles.get(i)
							.getX())
							* (wifiX - particles.get(i).getX())
							+ (wifiY - particles.get(i).getY())
							* (wifiY - particles.get(i).getY()));
					double p = (1.0 / (Math.sqrt(2 * pi) * std))
							* Math.exp((-1 * distance * distance)
									/ (2 * std * std));
					particles.get(i)
							.setWeight(p * particles.get(i).getWeight());
				}
			}
			boolWiFiEnable = false;
		}
		/* 首先对粒子权值进行归一化处理 */
		double sumWeight = 0;
		double objectX = 0;
		double objectY = 0;
		for (int i = 0; i < particleSize; i++) {
			sumWeight += particles.get(i).getWeight();
		}
		/* 利用加权平均获得目标的位置 */
		for (int i = 0; i < particleSize; i++) {
			particles.get(i)
					.setWeight(particles.get(i).getWeight() / sumWeight);
			objectX += particles.get(i).getX() * particles.get(i).getWeight();
			objectY += particles.get(i).getY() * particles.get(i).getWeight();
		}
		/* 判断估计的目标位置是否穿墙，即使粒子没有穿墙，但是加权平均的位置可能穿墙 */
		boolean isHittingWall = true;
		for (int i = 0; i < particleSize; i++) {
			if (particles.get(i).getWeight() != 0
					&& !WallDetector.getInstance().wallHittingDetect(objectX,
							objectY, particles.get(i).getX(),
							particles.get(i).getY())) {
				isHittingWall = false;
				break;
			}
		}
		/* 估计的目标位置穿墙，需要进行校准 */
		if (isHittingWall) {
			double[] point = WallDetector.getInstance().dragOutOfWall(objectX,
					objectY);
			objectX = point[0];
			objectY = point[1];

			for (int i = 0; i < particleSize; i++) {
				if (particles.get(i).getWeight() != 0
						&& !WallDetector.getInstance().wallHittingDetect(
								objectX + e, objectY + e,
								particles.get(i).getX(),
								particles.get(i).getY())) {
					objectX = objectX + e;
					objectY = objectY + e;
					break;
				} else if (particles.get(i).getWeight() != 0
						&& !WallDetector.getInstance().wallHittingDetect(
								objectX - e, objectY - e,
								particles.get(i).getX(),
								particles.get(i).getY())) {
					objectX = objectX - e;
					objectY = objectY - e;
					break;
				} else if (particles.get(i).getWeight() != 0
						&& !WallDetector.getInstance().wallHittingDetect(
								objectX - e, objectY + e,
								particles.get(i).getX(),
								particles.get(i).getY())) {
					objectX = objectX - e;
					objectY = objectY + e;
					break;
				} else if (particles.get(i).getWeight() != 0
						&& !WallDetector.getInstance().wallHittingDetect(
								objectX + e, objectY - e,
								particles.get(i).getX(),
								particles.get(i).getY())) {
					objectX = objectX + e;
					objectY = objectY - e;
					break;
				} else {

				}
			}
		}
		/** 重新设置目标初始位置 */
		setInitLocation(objectX, objectY);
		double[] ObjectCoor = { objectX, objectY };
		return ObjectCoor;
	}

	/** 重采样，对于权重比较低的粒子（如撞墙的粒子）进行重新采样，采样的中心为当前评估的目标的位置 */
	public void resampling() {
		for (int i = 0; i < particleSize; i++) {
			/** 粒子权值小于某个阈值时需要进行重采样 */
			if (particles.get(i).getWeight() <= this.threshold_w) {
				/*-1.5到+1.5之间的随机数*/
				double randX = 4 * Math.random() - 2;
				double randY = 4 * Math.random() - 2;
				/* 保证初始化的粒子不会穿过墙壁 */
				while (WallDetector.getInstance().wallHittingDetect(initX,
						initY, initX + randX, initY + randY)) {
					randX = 4 * Math.random() - 2;
					randY = 4 * Math.random() - 2;
				}
				particles.get(i).setCoorXY(initX + randX, initY + randY);
			}
			particles.get(i).setWeight(1.0 / particleSize);
		}
	}
}