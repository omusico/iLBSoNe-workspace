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
 * 检测粒子在传播过程中是否“撞墙”,检测目标是否被定位到墙壁内 单例模式
 * 
 * @author 胡旭科
 * 
 */
public class WallDetector {
	/** 单例模式 */
	public static WallDetector singleInstance = null;
	/** 包含环境中所有wall */
	ArrayList<Wall> walls = new ArrayList<Wall>();
	/** 墙壁信息所在文件路径 */
	private static final String wallsInfoFile = "PDR/walls.txt";

	/** 构造函数 从文件中加载所有的wall */
	private WallDetector() {
		String encoding = "GBK";
		// 以追加文件的方式打开输出流
		try {
			File signalfile = new File(
					Environment.getExternalStorageDirectory(), wallsInfoFile);
			;
			InputStreamReader signalstream = null;
			signalstream = new InputStreamReader(
					new FileInputStream(signalfile), encoding);
			BufferedReader bufferedReader = new BufferedReader(signalstream);
			String lineTxt = null;
			/* 按行读取文件中的数据 */
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
		// 解析读出的ACC数据
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
	 * 判断粒子从前一时刻运动到当前时刻时是否穿过墙壁
	 * 
	 * @param preX
	 *            前一时刻粒子的X坐标
	 * @param preY
	 *            前一时刻粒子的Y坐标
	 * @param curX
	 *            当前时刻粒子的X坐标
	 * @param curY
	 *            当前时刻粒子的Y坐标
	 * @return 如果穿过墙壁则返回true 否则返回false
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
	 * 判断线段是否相交
	 * 
	 * @param x1
	 *            线段1起点x坐标
	 * @param y1
	 *            线段1起点y坐标
	 * @param x2
	 *            线段1终点x坐标
	 * @param y2
	 *            线段1终点y坐标
	 * @param x3
	 *            线段2起点x坐标
	 * @param y3
	 *            线段2起点y坐标
	 * @param x4
	 *            线段2终点x坐标
	 * @param y4
	 *            线段2终点y坐标
	 * @return 相交返回true 否则返回false
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
	 * 点到线段的最短距离,利用向量方法求点到线段的最短距离，并返回最短距离的点
	 * 
	 * @param x
	 *            点的x坐标
	 * @param y
	 *            点的y坐标
	 * @param x1
	 *            线段起点x坐标
	 * @param y1
	 *            线段起点y坐标
	 * @param x2
	 *            线段终点x坐标
	 * @param y2
	 *            线段终点y坐标
	 * @return 最短距离以及最短距离的点
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
	 * 点（curX，curY）在墙壁内部，找到点离墙壁最近距离的点，将当前点移出墙壁
	 * 
	 * @param curX
	 *            当前X坐标
	 * @param curY
	 *            当前Y坐标
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
