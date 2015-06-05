/**
 * 
 */
package com.ubirtls.particlefilter;

import java.util.ArrayList;

/**
 * �����˲���
 * 
 * @author �����
 * 
 */
public class ParticleFilter {
	/** ��ʼλ�� */
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

	/** �����˲��������������� */
	private int particleSize = 50;
	/** ����������� */
	private ArrayList<Particle> particles = null;
	private double pi = 3.1415926;
	private double e = 0.000003;
	private double threshold_w = 0;
	private boolean boolWiFiEnable = false;
	/** ��̫�ֲ���׼�� */
	private static final double std = 1;

	/**
	 * ���캯��
	 * 
	 * @param init_x
	 *            ��ʼλ�� x
	 * @param init_y
	 *            ��ʼλ�� y
	 * @param particleNumber
	 *            ���Ӹ���
	 */
	public ParticleFilter(double init_x, double init_y, int particleNumber) {
		this.initX = init_x;
		this.initY = init_y;
		this.particleSize = particleNumber;
	}

	/**
	 * ���캯�� ��ʱĬ�ϵ�������������Ϊ60
	 * 
	 * @param init_x
	 *            ��ʼλ�� x
	 * @param init_y
	 *            ��ʼλ�� y
	 */
	public ParticleFilter(double init_x, double init_y) {
		this.initX = init_x;
		this.initY = init_y;
	}

	/**
	 * ���ó�ʼλ�� ��ͨ��Wi-Fiָ�Ʒ���ȷ����ʼλ��
	 * 
	 * @param init_x
	 *            ��ʼxλ��
	 * @param init_y
	 *            ��ʼ yλ��
	 */
	public void setInitLocation(double init_x, double init_y) {
		this.initX = init_x;
		this.initY = init_y;
	}

	public void enableWiFi() {
		this.boolWiFiEnable = true;
	}

	/** ����Ŀ��ĳ�ʼλ�ó�ʼ���������� */
	public void init() {
		particles = new ArrayList<Particle>(particleSize);
		for (int i = 0; i < particleSize; i++) {
			/*-1��+1֮��������*/
			double randX = 2 * Math.random() - 1;
			double randY = 2 * Math.random() - 1;
			/* ��֤��ʼ�������Ӳ��ᴩ��ǽ�� */
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
	 * ������ÿ�����Ӱ���PDR���д���������ײǽ�����ӽ���Ȩ������Ϊ0
	 * 
	 * @param stepLength
	 *            PDR����Ĳ���
	 * @param heading
	 *            PDR����ĳ���
	 */
	public void sampling(double stepLength, double heading) {
		/** ÿ�����Ӱ���PDR���д���������ײǽ�����ӽ���Ȩ������Ϊ0 */
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
	 * ����ÿ�����ӵ�Ȩ�أ�wi-fi��õ�λ����Ϣ���Լ�����ǽ�����ƣ������ü�Ȩƽ�����Ŀ���λ��
	 * 
	 * @return Ŀ���λ��
	 */
	public double[] weightComputing(double wifiX, double wifiY) {
		/** �����ж�wifi����Ƿ���ã�wifi��λ����ϳ�5s����û�н�������ڼ䣬������ǽ�����Ƹ�������Ȩֵ */
		if (boolWiFiEnable) {
			for (int i = 0; i < particleSize; i++) {
				/** ����ײǽ�����Ӳ���ͨ��wifi����Ȩֵ�ĸ��� */
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
		/* ���ȶ�����Ȩֵ���й�һ������ */
		double sumWeight = 0;
		double objectX = 0;
		double objectY = 0;
		for (int i = 0; i < particleSize; i++) {
			sumWeight += particles.get(i).getWeight();
		}
		/* ���ü�Ȩƽ�����Ŀ���λ�� */
		for (int i = 0; i < particleSize; i++) {
			particles.get(i)
					.setWeight(particles.get(i).getWeight() / sumWeight);
			objectX += particles.get(i).getX() * particles.get(i).getWeight();
			objectY += particles.get(i).getY() * particles.get(i).getWeight();
		}
		/* �жϹ��Ƶ�Ŀ��λ���Ƿ�ǽ����ʹ����û�д�ǽ�����Ǽ�Ȩƽ����λ�ÿ��ܴ�ǽ */
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
		/* ���Ƶ�Ŀ��λ�ô�ǽ����Ҫ����У׼ */
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
		/** ��������Ŀ���ʼλ�� */
		setInitLocation(objectX, objectY);
		double[] ObjectCoor = { objectX, objectY };
		return ObjectCoor;
	}

	/** �ز���������Ȩ�رȽϵ͵����ӣ���ײǽ�����ӣ��������²���������������Ϊ��ǰ������Ŀ���λ�� */
	public void resampling() {
		for (int i = 0; i < particleSize; i++) {
			/** ����ȨֵС��ĳ����ֵʱ��Ҫ�����ز��� */
			if (particles.get(i).getWeight() <= this.threshold_w) {
				/*-1.5��+1.5֮��������*/
				double randX = 4 * Math.random() - 2;
				double randY = 4 * Math.random() - 2;
				/* ��֤��ʼ�������Ӳ��ᴩ��ǽ�� */
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