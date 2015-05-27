/**
 * 
 */
package com.ubirtls.PDR;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import com.ubirtls.util.MyMath;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;


import coordinate.TwoDCoordinate;

/**
 * 捕获转角
 * @author 胡旭科
 * 
 */
public class CornerDetector {
	/**单例模式*/
	public static  CornerDetector singleInstance = null;
	/**包含环境中所有wall*/
	ArrayList<Corner> corners = new ArrayList<Corner>();

	/**累计角度变化，当超过cornerDectorThreshold时认为转角事件发生*/
	private double angleChange = 0;
	/**每步进行积累的角度变化最小值*/
	private double stepAngleThreshold = 7;
	/**转角捕获的角度阈值*/
	private double cornerDectorThreshold = 60;
	boolean boolCorner = false;
	/**转角发生的初始方向*/
	private double oriStart = 0;
	/**转角发生的终点方向*/
	private double oriEnd = 0;
	/**标识上次遇到的转角*/
	private int lastCorner = -1;
	/**当前位置与转角位置的距离差的最大值，超过该值则不进行转角的匹配*/
	private double maxDistance = 6.0;
	private double angleDifference = 30;
	private static final String cornersInfoFile = "client/corners.txt";

	/**构造函数，初始化环境中的转角*/
	public CornerDetector(){
		String encoding="GBK";
		//以追加文件的方式打开输出流
		try {
			File signalfile = new File(Environment.getExternalStorageDirectory(), cornersInfoFile);;
			InputStreamReader signalstream = null;
			signalstream = new InputStreamReader(new FileInputStream(signalfile),encoding);
			BufferedReader bufferedReader = new BufferedReader(signalstream);
			String lineTxt = null;
			/*按行读取文件中的数据 */
			while((lineTxt = bufferedReader.readLine()) != null){
				String[] acc = lineTxt.split(" ");
				if(acc.length >= 4){
					double startX = Double.parseDouble(acc[0]);
					double startY = Double.parseDouble(acc[1]);
					ArrayList<Double[]> cornerOri = new ArrayList<Double[]>();
					for(int i = 0; i < acc.length-2; i+=2){
						double startOri = Double.parseDouble(acc[i+2]);
						double endOri = Double.parseDouble(acc[i+3]);
						cornerOri.add(new Double[]{startOri,endOri});
					}
					corners.add(new Corner(startX, startY, cornerOri));
				}
			}
			signalstream.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//解析读出的ACC数据
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

	public TwoDCoordinate detectCorner(double gyroChange,double oriInit, double curX, double curY, Context context){
/*        Toast.makeText(context, "corner Dectored:", 2000).show();
*/
/*        Toast.makeText(context, "angleChange:" + angleChange + "gyroChange:" + gyroChange, 2000).show();
*/		if (Math.abs(gyroChange) >= stepAngleThreshold && (angleChange * gyroChange) >=0){
			if(angleChange == 0)
				oriStart = oriInit;
			angleChange = angleChange + gyroChange;
/*            Toast.makeText(context, "angleChange:" + angleChange, 2000).show();
*/
		}
		else if(Math.abs(angleChange) >= cornerDectorThreshold){
/*            Toast.makeText(context, "angleChange:", 2000).show();
*/
			oriEnd = oriInit +  gyroChange;
			angleChange = 0;
			/*转角事件已经发生，进行转角匹配*/
			double minDistance = 200;
			int cornerIndex = -1;
			int cornerSize = corners.size();
			for (int i = 0; i < cornerSize; i++)
				for(int j = 0; j < corners.get(i).orientation.size(); j++){
					//转角方向判断
					if(((Math.abs(oriStart - corners.get(i).orientation.get(j)[0]) <= angleDifference 
							|| Math.abs(oriStart - corners.get(i).orientation.get(j)[0]) >= 360-angleDifference)
							&& (Math.abs(oriEnd - corners.get(i).orientation.get(j)[1]) <= angleDifference 
									|| Math.abs(oriEnd - corners.get(i).orientation.get(j)[1]) >= 360-angleDifference))
									|| ( (Math.abs(oriStart - MyMath.mod((corners.get(i).orientation.get(j)[1] + 180),360)) <= angleDifference 
											|| Math.abs(oriStart - MyMath.mod((corners.get(i).orientation.get(j)[1] + 180),360)) >= 360-angleDifference) 
											&& (Math.abs(oriEnd - MyMath.mod((corners.get(i).orientation.get(j)[0] + 180),360)) <= angleDifference 
													|| Math.abs(oriEnd - MyMath.mod((corners.get(i).orientation.get(j)[0] + 180),360)) >= 360-angleDifference))){

						double distance = Math.sqrt((curX-corners.get(i).getX())*(curX-corners.get(i).getX())+ 
								(curY-corners.get(i).getY())*(curY-corners.get(i).getY()));

						if( (distance < maxDistance) &&  (distance < minDistance) && i != lastCorner){
							minDistance = distance;
							cornerIndex = i;
						}
					}
				}
			if(cornerIndex != -1 && minDistance>=2){
				lastCorner = cornerIndex;
/*                Toast.makeText(context, "corner Dectored:" + cornerIndex, 2000).show();
*/
				return new  TwoDCoordinate (corners.get(cornerIndex).getX(),corners.get(cornerIndex).getY());

			}
			else if (cornerIndex != -1 && minDistance<2){
/*                Toast.makeText(context, "corner Dectored:" + cornerIndex, 2000).show();
*/				lastCorner = cornerIndex;
				
			}
			else{
			}
		}else{
			angleChange = 0;
		}
		return null;
	}
	public static synchronized CornerDetector getInstance() {
		if (singleInstance == null){
			singleInstance = new CornerDetector();
		}
		return singleInstance;
	}

}
