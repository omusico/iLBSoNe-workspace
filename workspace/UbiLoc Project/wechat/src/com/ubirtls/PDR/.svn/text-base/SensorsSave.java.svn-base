package com.ubirtls.PDR;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.util.EncodingUtils;

import coordinate.TwoDCoordinate;

import android.os.Environment;

/***
 * 提取到原始测量值后，经过简单处理，存入文件中
 * 
 * @author Administrator
 * 
 */
public class SensorsSave {

	/**
	 * 存贮未经处理的原始数据进行分析
	 * 
	 * @param measurements 测量值样本信息
	 * @param x 指纹x坐标
	 * @param y 指纹y坐标
	 */
	public static void saveRawDatas(ArrayList<double[]> sensors,int stepLength,String sensor_file) {
		// 创建rssi原始数据存贮文件
		File signalfile = new File(Environment.getExternalStorageDirectory(),
				sensor_file);
		FileOutputStream signalstream = null;
		// 以追加文件的方式打开输出流
		try {
			signalstream = new FileOutputStream(signalfile, true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 原始传感器数据存贮
		String signalBuffer = "";

		for (int i = 0; i < sensors.size(); i++) {
			double []sensor = sensors.get(i);
			signalBuffer += (long)sensor[3] + " " + sensor[0] + " " + sensor[1] + " " + sensor[2];
			signalBuffer += "\r\n";
		}
		signalBuffer += stepLength;
		signalBuffer += "\r\n";
		try {
			signalstream.write(signalBuffer.getBytes());
			signalstream.flush();
			signalstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void saveRawDatas(ArrayList<double[]> sensors,String sensor_file) {
		// 创建rssi原始数据存贮文件
		File signalfile = new File(Environment.getExternalStorageDirectory(),
				sensor_file);
		FileOutputStream signalstream = null;
		// 以追加文件的方式打开输出流
		try {
			signalstream = new FileOutputStream(signalfile, true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 原始传感器数据存贮
		String signalBuffer = "";

		for (int i = 0; i < sensors.size(); i++) {
			double []sensor = sensors.get(i);
			signalBuffer += (long)sensor[3] + " " + sensor[0] + " " + sensor[1] + " " + sensor[2];
			signalBuffer += "\r\n";
		}
		try {
			signalstream.write(signalBuffer.getBytes());
			signalstream.flush();
			signalstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void saveOrientationDatas(ArrayList<double[]> sensors, String ori_sensor_file) {
		// 创建rssi原始数据存贮文件
		File signalfile = new File(Environment.getExternalStorageDirectory(),
				ori_sensor_file);
		FileOutputStream signalstream = null;
		// 以追加文件的方式打开输出流
		try {
			signalstream = new FileOutputStream(signalfile, true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 原始传感器数据存贮
		String signalBuffer = "";
		for (int i = 0; i < sensors.size(); i++) {
			signalBuffer += (long)sensors.get(i)[1] + " " + sensors.get(i)[0] + "\r\n";
		}
		try {
			signalstream.write(signalBuffer.getBytes());
			signalstream.flush();
			signalstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void saveSingalDatas(ArrayList<Double> sensors,String sensor_file) {
		// 创建rssi原始数据存贮文件
		File signalfile = new File(Environment.getExternalStorageDirectory(),
				sensor_file);
		FileOutputStream signalstream = null;
		// 以追加文件的方式打开输出流
		try {
			signalstream = new FileOutputStream(signalfile, true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 原始传感器数据存贮
		String signalBuffer = "";
		for (int i = 0; i < sensors.size(); i++) {
			double sensor = sensors.get(i);
			signalBuffer +=  sensor;
			signalBuffer += "\r\n";
		}
		try {
			signalstream.write(signalBuffer.getBytes());
			signalstream.flush();
			signalstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void savePath(ArrayList<TwoDCoordinate> points,int step, double distance, String path_file) {
		// 创建rssi原始数据存贮文件
		File signalfile = new File(Environment.getExternalStorageDirectory(),
				path_file);
		FileOutputStream signalstream = null;
		// 以追加文件的方式打开输出流
		try {
			signalstream = new FileOutputStream(signalfile, true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 原始传感器数据存贮
		String signalBuffer = "";
		signalBuffer = signalBuffer + points.size();
		signalBuffer += "\r\n";

		signalBuffer = signalBuffer + step;
		signalBuffer += "\r\n";
		signalBuffer = signalBuffer + distance;
		signalBuffer += "\r\n";

		for (int i = 0; i < points.size(); i++) {
			TwoDCoordinate point = points.get(i);
			signalBuffer +=  point.getXCoordinateAsString()+" " + point.getYCoordinateAsString();
			signalBuffer += "\r\n";
		}
		try {
			signalstream.write(signalBuffer.getBytes());
			signalstream.flush();
			signalstream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static ArrayList<Double[]> readAccFile(String acc_file_path){
		File signalfile = new File(acc_file_path);
		FileInputStream signalstream = null;
		String accRaw = " ";
		// 以追加文件的方式打开输出流
		try {
			signalstream = new FileInputStream(signalfile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
            int length = signalstream.available(); 

            byte[] buffer = new byte[length]; 
            signalstream.read(buffer); 
            accRaw = EncodingUtils.getString(buffer, "UTF-8"); 
            signalstream.close(); 
		}
		catch(Exception e){
			
		}
		//解析读出的ACC数据
		String[]accLine = accRaw.split("\r\n");
		int lineLenght = accLine.length;
		ArrayList<Double[]> accData = new ArrayList<Double[]>();
		for(int i = 0 ; i <lineLenght; i++){
			String[] acc = accLine[i].split(" ");
			if(acc.length == 4){
				Double[] accSample = new Double[4];
				accSample[0] = Double.parseDouble(acc[0]);
				accSample[1] = Double.parseDouble(acc[1]);
				accSample[2] = Double.parseDouble(acc[2]);
				accSample[3] = Double.parseDouble(acc[3]);
				accData.add(accSample);
			}
		}
		return accData;
	}
	public static ArrayList<Double[]> readOriOiGyroFile(String file_path){
		File signalfile = new File(file_path);
		FileInputStream signalstream = null;
		String accRaw = " ";
		// 以追加文件的方式打开输出流
		try {
			signalstream = new FileInputStream(signalfile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
            int length = signalstream.available(); 

            byte[] buffer = new byte[length]; 
            signalstream.read(buffer); 
            accRaw = EncodingUtils.getString(buffer, "UTF-8"); 
            signalstream.close(); 
		}
		catch(Exception e){
			
		}
		//解析读出的ACC数据
		String[]accLine = accRaw.split("\r\n");
		int lineLenght = accLine.length;
		ArrayList<Double[]> accData = new ArrayList<Double[]>();
		for(int i = 0 ; i <lineLenght; i++){
			String[] acc = accLine[i].split(" ");
			if(acc.length == 2){
				Double[] accSample = new Double[2];
				accSample[0] = Double.parseDouble(acc[0]);
				accSample[1] = Double.parseDouble(acc[1]);
				accData.add(accSample);
			}
		}
		return accData;
	}
	
}