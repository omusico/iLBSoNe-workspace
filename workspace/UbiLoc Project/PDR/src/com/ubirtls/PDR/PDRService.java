/**
 * 
 */
package com.ubirtls.PDR;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

import com.ubirtls.particlefilter.ParticleFilter;
import com.ubirtls.util.MyMath;

import coordinate.TwoDCoordinate;

/**
 * PDR方法关键服务，实时采集样本数据，并计算位置偏移
 * 
 * @author 胡旭科
 * 
 */
public class PDRService extends Service {

	public static final String HIPPO_SERVICE_IDENTIFIER = "pdrservice";

	public ArrayList<double[]> primitiveAccelerometerList = new ArrayList<double[]>();

	public ArrayList<double[]> primitiveOrientationList = new ArrayList<double[]>();
	public ArrayList<double[]> primitiveGyroscopeList = new ArrayList<double[]>();

	private SensorManager mSensorManager;
	// 加速度传感器
	private Sensor mAccelerometer;
	// 方向传感器
	private Sensor mOrientation;
	// 陀螺仪
	private Sensor mGyroscope;
	// 传感器事件监听器
	private OnSensorEventListener mOnSensorEventListener = new OnSensorEventListener();
	/* 捕获步行事件、计算步长 */
	private StepDetector stepDetector = new StepDetector();
	boolean isCalculatingDisplace = false;
	private double firstStepLength = 0;
	private double secondStepLength = 0;
	/** 步行朝向滤波 */
	HeadingKalmanFilter headingFilter = null;
	double gyroHZ = 0.0458;
	private double secondStepHeading = 0;
	private double firstStepHeading = 0;
	boolean isFirst = true;
	private int accIndex = 0;
	private int oriIndex = 0;
	private int gyroIndex = 0;
	private int totalStep = 0;
	private int accSaveSize = 200;

	/** 转角捕获相关参数 */
	private int gyroInit = 0;
	private double curX = 0;
	private double curY = 0;
	private double extraGyroChange = 0;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 粒子滤波 初始坐标 */
	ParticleFilter particleFilter = new ParticleFilter(75, 4);

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
		// 注册传感器监听
		mSensorManager.registerListener(mOnSensorEventListener, mAccelerometer,
				SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(mOnSensorEventListener, mOrientation,
				SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(mOnSensorEventListener, mGyroscope,
				SensorManager.SENSOR_DELAY_UI);
		particleFilter.init();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		/** 註銷傳感器監聽 */
		mSensorManager.unregisterListener(mOnSensorEventListener,
				mAccelerometer);
		mSensorManager.unregisterListener(mOnSensorEventListener, mOrientation);
		mSensorManager.unregisterListener(mOnSensorEventListener, mGyroscope);
		/*
		 * SensorsSave.saveRawDatas(primitiveAccelerometerList,totalStep,
		 * "acc.txt" );
		 * SensorsSave.saveOrientationDatas(primitiveOrientationList,
		 * "ori.txt"); SensorsSave.saveOrientationDatas(primitiveGyroscopeList,
		 * "gyro.txt");
		 */
		primitiveAccelerometerList.clear();
		primitiveOrientationList.clear();
		primitiveGyroscopeList.clear();
		particleFilter = null;
	}

	@Override
	public void onRebind(Intent intent) {
		// TODO Auto-generated method stub
		super.onRebind(intent);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.i("服务启动", "PDRService");
	}

	private class OnSensorEventListener implements SensorEventListener {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub

			// 加速度传感器事件
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				double[] accelerometerValues = new double[4];
				// 获得x轴的值
				accelerometerValues[0] = event.values[0];
				// 获得y轴的值
				accelerometerValues[1] = event.values[1];
				// 获得z轴的值
				accelerometerValues[2] = event.values[2];
				accelerometerValues[3] = System.currentTimeMillis();
				primitiveAccelerometerList.add(accelerometerValues);
				/* 用于步捕获所需的加速度样本数目 */
				int minAccNeeds = stepDetector.getStepSampleCount();
				int userfulSize = primitiveAccelerometerList.size() - accIndex;
				/* 目前没有进行航位推算 */
				if ((userfulSize >= minAccNeeds) && !isCalculatingDisplace) {
					isCalculatingDisplace = true;
					int accSize = primitiveAccelerometerList.size();
					int gyroSize = primitiveGyroscopeList.size();
					int oriSize = primitiveOrientationList.size();
					StepDetectResult result = stepDetector.detectStep(MyMath
							.cloneSubList(primitiveAccelerometerList,
									0 + accIndex, minAccNeeds - 1 + accIndex));
					Log.i("pdr", String.valueOf(result.isStep));

					/** 获得两步中对应的方向数据索引 */
					int oriOneStart = accIndex * oriSize / accSize;
					int oriOneEnd = ((result.stepSampleCount - 1) / 2 - 1 + accIndex)
							* oriSize / accSize;
					int oriTwoStart = ((result.stepSampleCount - 1) / 2 + accIndex)
							* oriSize / accSize;
					int oriTwoEnd = (result.stepSampleCount - 1 + accIndex)
							* oriSize / accSize;
					/** 获得两步中对应的陀螺仪数据索引 */
					int gyroOneStart = accIndex * gyroSize / accSize;
					int gyroOneEnd = ((result.stepSampleCount - 1) / 2 - 1 + accIndex)
							* gyroSize / accSize;
					int gyroTwoStart = ((result.stepSampleCount - 1) / 2 + accIndex)
							* gyroSize / accSize;
					int gyroTwoEnd = (result.stepSampleCount - 1 + accIndex)
							* gyroSize / accSize;
					/** 步捕获成功 */
					if (result.isStep) {
						totalStep = totalStep + 2;
						firstStepLength = result.stepOneLength;
						secondStepLength = result.stepTwoLength;
						/** 获得第一步对应的方向以及陀螺仪数据进行卡尔曼滤波 */
						ArrayList<Double> oriX = MyMath.cloneSubListFirst(
								primitiveOrientationList, oriOneStart,
								oriOneEnd);
						ArrayList<Double> gyroZ = MyMath.cloneSubListFirst(
								primitiveGyroscopeList, gyroOneStart,
								gyroOneEnd);
						/** 保存数据 用于分析调试 */
						// 转角捕获
						ArrayList<Double> gyroCorner = MyMath
								.cloneSubListFirst(primitiveGyroscopeList,
										gyroInit, gyroOneEnd);

						double gyroChange = MyMath.sum(gyroCorner) * gyroHZ
								* 180 / Math.PI + extraGyroChange;
						extraGyroChange = 0;
						gyroInit = gyroOneEnd + 1;
						TwoDCoordinate corner = null;/*
													 * CornerDetector.getInstance
													 * ().detectCorner
													 * (gyroChange,
													 * secondStepHeading, curX,
													 * curY,PDRService.this);
													 * //匹配出转角后
													 * ，将目标位置定位到该点，并重新初始粒子滤波
													 * 
													 * /**
													 * 获得gyroHZ,初始化headingFilter
													 */
						if (headingFilter == null) {
							/*
							 * double[] datasLast =
							 * primitiveGyroscopeList.get(gyroSize-1); double[]
							 * datasFirst = primitiveGyroscopeList.get(0);
							 * gyroHZ =
							 * (datasLast[1]-datasFirst[1])/(1000*gyroSize);
							 * Log.i("gyroHZ", String.valueOf(gyroHZ));
							 */double[] pre_ori = primitiveOrientationList
									.get(oriOneStart);
							headingFilter = new HeadingKalmanFilter(pre_ori[0]);
						}

						firstStepHeading = headingFilter.getFilteredHeading(
								oriX, gyroZ);
						oriX = null;
						gyroZ = null;
						/** 发送第一步结果 */
						/** 直接将匹配的转角位置作为目标当前的位置 */
						if (corner != null) {
							particleFilter = new ParticleFilter(
									corner.getXCoordinate(),
									corner.getYCoordinate());
							particleFilter.init();
							curX = corner.getXCoordinate();
							curY = corner.getYCoordinate();
							/*
							 * Toast.makeText(PDRService.this,
							 * "corner Dectored", 2000).show();
							 */Intent resultIntent = new Intent(
									HIPPO_SERVICE_IDENTIFIER);
							resultIntent.putExtra("positionX", curX);
							resultIntent.putExtra("positionY", curY);
							sendBroadcast(resultIntent);
							resultIntent = null;
							corner = null;
						} else {
							particleFilter.sampling(firstStepLength,
									firstStepHeading);
							double[] position = particleFilter.weightComputing(
									0, 0);
							particleFilter.resampling();
							Intent resultIntent = new Intent(
									HIPPO_SERVICE_IDENTIFIER);
							curX = position[0];
							curY = position[1];
							resultIntent.putExtra("positionX", position[0]);
							resultIntent.putExtra("positionY", position[1]);
							sendBroadcast(resultIntent);
							resultIntent = null;
						}
						/*
						 * Intent resultIntent = new Intent(
						 * HIPPO_SERVICE_IDENTIFIER);
						 * resultIntent.putExtra("stepLength", firstStepLength);
						 * resultIntent.putExtra("stepHeading",
						 * firstStepHeading); sendBroadcast(resultIntent);
						 * resultIntent = null;
						 */
						/** 获得第二步对应的方向以及陀螺仪数据进行卡尔曼滤波 */
						oriX = MyMath.cloneSubListFirst(
								primitiveOrientationList, oriTwoStart,
								oriTwoEnd);
						gyroZ = MyMath.cloneSubListFirst(
								primitiveGyroscopeList, gyroTwoStart,
								gyroTwoEnd);

						// 转角捕获
						gyroCorner = MyMath.cloneSubListFirst(
								primitiveGyroscopeList, gyroInit, gyroTwoEnd);
						gyroChange = MyMath.sum(gyroCorner) * gyroHZ * 180
								/ Math.PI + extraGyroChange;
						extraGyroChange = 0;
						gyroInit = gyroTwoEnd + 1;
						corner = null;/*
									 * CornerDetector.getInstance().detectCorner
									 * (gyroChange, firstStepHeading, curX,
									 * curY,PDRService.this);
									 */

						secondStepHeading = headingFilter.getFilteredHeading(
								oriX, gyroZ);
						oriX = null;
						gyroZ = null;
						/** 发送第二步结果 */
						/** 直接将匹配的转角位置作为目标当前的位置 */
						if (corner != null) {
							particleFilter = new ParticleFilter(
									corner.getXCoordinate(),
									corner.getYCoordinate());
							particleFilter.init();
							curX = corner.getXCoordinate();
							curY = corner.getYCoordinate();
							/*
							 * Toast.makeText(PDRService.this,
							 * "corner Dectored", 2000).show();
							 */
							/** 发送结果 */
							Intent resultIntent = new Intent(
									HIPPO_SERVICE_IDENTIFIER);
							resultIntent.putExtra("positionX", curX);
							resultIntent.putExtra("positionY", curY);
							sendBroadcast(resultIntent);
							resultIntent = null;
							corner = null;
						} else {
							particleFilter.sampling(secondStepLength,
									secondStepHeading);
							double[] position = particleFilter.weightComputing(
									0, 0);
							particleFilter.resampling();
							Intent resultIntent = new Intent(
									HIPPO_SERVICE_IDENTIFIER);
							curX = position[0];
							curY = position[1];
							resultIntent.putExtra("positionX", position[0]);
							resultIntent.putExtra("positionY", position[1]);
							sendBroadcast(resultIntent);
							resultIntent = null;
						}
						/*
						 * resultIntent = new Intent(HIPPO_SERVICE_IDENTIFIER);
						 * resultIntent.putExtra("stepLength",
						 * secondStepLength);
						 * resultIntent.putExtra("stepHeading",
						 * secondStepHeading); sendBroadcast(resultIntent);
						 * resultIntent = null;
						 *//** 通知新的航位推算结果产生 */

						// handler.sendEmptyMessage(1);
					}
					/*
					 * accSize = primitiveAccelerometerList.size(); gyroSize =
					 * primitiveGyroscopeList.size(); oriSize =
					 * primitiveOrientationList.size(); Log.i("accLeave",
					 * String.valueOf(accSize)); Log.i("gyroLeave",
					 * String.valueOf(gyroSize)); Log.i("oriLeave",
					 * String.valueOf(oriSize)); Log.i("accDS",
					 * String.valueOf(result.stepSampleCount - 1));
					 *//*
						 * SensorsSave.saveRawDatas(MyMath.cloneSubList(
						 * primitiveAccelerometerList, 0, result.stepSampleCount
						 * - 1), result.stepSampleCount, "acc.txt");
						 */

					/** 移除已经参与计算的acc、ori、gyro数据 */

					/*
					 * for (int i = 0; i <= result.stepSampleCount - 1; i++)
					 * primitiveAccelerometerList.remove(0); for (int i = 0; i
					 * <= oriTwoEnd; i++) primitiveOrientationList.remove(0);
					 * for (int i = 0; i <= gyroTwoEnd; i++)
					 * primitiveGyroscopeList.remove(0);
					 *//*
						 * accSize = primitiveAccelerometerList.size(); gyroSize
						 * = primitiveGyroscopeList.size(); oriSize =
						 * primitiveOrientationList.size(); Log.i("accDelete",
						 * String.valueOf(accSize)); Log.i("gyroDelete",
						 * String.valueOf(gyroSize)); Log.i("oriDelete",
						 * String.valueOf(oriSize));
						 */
					/** 内存中数据达到一定数量即进行清理，并保存至文件 */
					if (accIndex >= accSaveSize) {
						if (gyroInit >= gyroIndex)
							gyroInit -= gyroIndex;
						else {
							ArrayList<Double> gyroZ = MyMath.cloneSubListFirst(
									primitiveGyroscopeList, gyroInit,
									gyroIndex - 1);
							extraGyroChange = MyMath.sum(gyroZ) * gyroHZ * 180
									/ Math.PI;
							gyroInit = 0;
						}

						SensorsSave.saveRawDatas(MyMath.cloneSubList(
								primitiveAccelerometerList, 0, accIndex - 1),
								"acc.txt");
						SensorsSave.saveOrientationDatas(MyMath.cloneSubList(
								primitiveOrientationList, 0, oriIndex - 1),
								"ori.txt");
						SensorsSave.saveOrientationDatas(MyMath.cloneSubList(
								primitiveGyroscopeList, 0, gyroIndex - 1),
								"gyro.txt");
						for (int i = 0; i < accIndex; i++)
							primitiveAccelerometerList.remove(0);
						for (int i = 0; i < oriIndex; i++)
							primitiveOrientationList.remove(0);
						for (int i = 0; i < gyroIndex; i++)
							primitiveGyroscopeList.remove(0);

						accIndex = result.stepSampleCount;
						oriIndex = oriTwoEnd + 1 - oriIndex;
						gyroIndex = gyroTwoEnd + 1 - gyroIndex;
					} else {
						accIndex += result.stepSampleCount;
						oriIndex = oriTwoEnd + 1;
						gyroIndex = gyroTwoEnd + 1;
					}

					minAccNeeds = stepDetector.getStepSampleCount();
					isCalculatingDisplace = false;
				}

			}
			// 方向感器
			else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {

				double[] orientationValues = new double[2];
				orientationValues[0] = event.values[0];
				orientationValues[1] = System.currentTimeMillis();
				primitiveOrientationList.add(orientationValues);

			}
			// 陀螺仪
			else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
				double[] gyroscopeValues = new double[2];
				gyroscopeValues[0] = event.values[2];
				gyroscopeValues[1] = System.currentTimeMillis();
				primitiveGyroscopeList.add(gyroscopeValues);
			}
		}
	}
}
