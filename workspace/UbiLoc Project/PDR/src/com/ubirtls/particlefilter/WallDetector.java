/**
 * 
 */
package com.ubirtls.particlefilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.os.Environment;

/**
 * ��������ڴ����������Ƿ�ײǽ��,���Ŀ���Ƿ񱻶�λ��ǽ���� ����ģʽ
 * 
 * @author �����
 * 
 */
public class WallDetector {
	/** ����ģʽ */
	public static WallDetector singleInstance = null;
	/** ��������������wall */
	ArrayList<Wall> walls = new ArrayList<Wall>();
	/** ǽ����Ϣ�����ļ�·�� */
	private static final String wallsInfoFile = "PDR/walls.txt";

	/** ���캯�� ���ļ��м������е�wall */
	private WallDetector() {
		String encoding = "GBK";
		// ��׷���ļ��ķ�ʽ�������
		try {
			File signalfile = new File(
					Environment.getExternalStorageDirectory(), wallsInfoFile);
			;
			InputStreamReader signalstream = null;
			signalstream = new InputStreamReader(
					new FileInputStream(signalfile), encoding);
			BufferedReader bufferedReader = new BufferedReader(signalstream);
			String lineTxt = null;
			/* ���ж�ȡ�ļ��е����� */
			while ((lineTxt = bufferedReader.readLine()) != null) {
				String[] acc = lineTxt.split(" ");
				if (acc.length == 4) {
					double startX = Double.parseDouble(acc[0]);
					double startY = Double.parseDouble(acc[1]);
					double endX = Double.parseDouble(acc[2]);
					double endY = Double.parseDouble(acc[3]);
					walls.add(new Wall(startX, startY, endX, endY));
				}
			}
			signalstream.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ����������ACC����
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static synchronized WallDetector getInstance() {
		if (singleInstance == null) {
			singleInstance = new WallDetector();
		}
		return singleInstance;
	}

	/**
	 * �ж����Ӵ�ǰһʱ���˶�����ǰʱ��ʱ�Ƿ񴩹�ǽ��
	 * 
	 * @param preX
	 *            ǰһʱ�����ӵ�X����
	 * @param preY
	 *            ǰһʱ�����ӵ�Y����
	 * @param curX
	 *            ��ǰʱ�����ӵ�X����
	 * @param curY
	 *            ��ǰʱ�����ӵ�Y����
	 * @return �������ǽ���򷵻�true ���򷵻�false
	 */
	public boolean wallHittingDetect(double preX, double preY, double curX,
			double curY) {

		int wallsNum = walls.size();
		boolean isHitting = false;
		for (int i = 0; i < wallsNum; i++) {
			isHitting = lineSegmentCross(preX, preY, curX, curY, walls.get(i)
					.getStartX(), walls.get(i).getStartY(), walls.get(i)
					.getEndX(), walls.get(i).getEndY());
			if (isHitting)
				break;
			return isHitting;
		}
		return false;
	}

	private double Multiply(double x1, double y1, double x2, double y2,
			double x0, double y0) {
		return ((x1 - x0) * (y2 - y0) - (x2 - x0) * (y1 - y0));
	}

	/**
	 * �ж��߶��Ƿ��ཻ
	 * 
	 * @param x1
	 *            �߶�1���x����
	 * @param y1
	 *            �߶�1���y����
	 * @param x2
	 *            �߶�1�յ�x����
	 * @param y2
	 *            �߶�1�յ�y����
	 * @param x3
	 *            �߶�2���x����
	 * @param y3
	 *            �߶�2���y����
	 * @param x4
	 *            �߶�2�յ�x����
	 * @param y4
	 *            �߶�2�յ�y����
	 * @return �ཻ����true ���򷵻�false
	 */
	private boolean lineSegmentCross(double x1, double y1, double x2,
			double y2, double x3, double y3, double x4, double y4) {
		return ((Math.max(x1, x2) >= Math.min(x3, x4))
				&& (Math.max(x3, x4) >= Math.min(x1, x2))
				&& (Math.max(y1, y2) >= Math.min(y3, y4))
				&& (Math.max(y3, y4) >= Math.min(y1, y2))
				&& (Multiply(x3, y3, x2, y2, x1, y1)
						* Multiply(x2, y2, x4, y4, x1, y1) >= 0) && (Multiply(
				x1, y1, x4, y4, x3, y3) * Multiply(x4, y4, x2, y2, x3, y3) >= 0));
	}

	/**
	 * �㵽�߶ε���̾���,��������������㵽�߶ε���̾��룬��������̾���ĵ�
	 * 
	 * @param x
	 *            ���x����
	 * @param y
	 *            ���y����
	 * @param x1
	 *            �߶����x����
	 * @param y1
	 *            �߶����y����
	 * @param x2
	 *            �߶��յ�x����
	 * @param y2
	 *            �߶��յ�y����
	 * @return ��̾����Լ���̾���ĵ�
	 */
	private double[] pointToSegDist(double x, double y, double x1, double y1,
			double x2, double y2) {
		double cross = ((x2 - x1) * (x - x1) + (y2 - y1) * (y - y1));
		double distance;
		double pointX, pointY;
		if (cross <= 0) {
			distance = Math.sqrt((x - x1) * (x - x1) + (y - y1) * (y - y1));
			pointX = x1;
			pointY = y1;
			double result[] = { pointX, pointY, distance };
			return result;
		}

		double d2 = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
		if (cross >= d2) {
			distance = Math.sqrt((x - x2) * (x - x2) + (y - y2) * (y - y2));
			pointX = x2;
			pointY = y2;
			double result[] = { pointX, pointY, distance };
			return result;
		}
		double r = cross / d2;
		double px = x1 + (x2 - x1) * r;
		double py = y1 + (y2 - y1) * r;
		distance = Math.sqrt((x - px) * (x - px) + (y - py) * (y - py));
		pointX = px;
		pointY = py;
		double result[] = { pointX, pointY, distance };
		return result;
	}

	/**
	 * �㣨curX��curY����ǽ���ڲ����ҵ�����ǽ���������ĵ㣬����ǰ���Ƴ�ǽ��
	 * 
	 * @param curX
	 *            ��ǰX����
	 * @param curY
	 *            ��ǰY����
	 * @return
	 */
	public double[] dragOutOfWall(double curX, double curY) {
		double minDistance = 100;
		double pointX = 0, pointY = 0;
		int wallsNum = walls.size();
		for (int i = 1; i < wallsNum; i++) {
			double[] result = pointToSegDist(curX, curY, walls.get(i)
					.getStartX(), walls.get(i).getStartY(), walls.get(i)
					.getEndX(), walls.get(i).getEndY());
			if (result[2] < minDistance) {
				minDistance = result[2];
				pointX = result[0];
				pointY = result[1];
			}
		}
		double[] point = { pointX, pointY };
		return point;
	}
}
