package com.ubiloc.model;

import java.io.Serializable;
import java.sql.Timestamp;


public class MovingObj implements Serializable{
	
	//private int type;//消息类型标示
	private String userid;
	private double lon;
	private double lat;
	private Timestamp time;
	
	
	
	public MovingObj(String userid, double lon, double lat,Timestamp time) {
		//this.type=type;
		this.userid = userid;
		this.lon = lon;
		this.lat = lat;
		this.time=time;
	}
	public MovingObj() {
		
	}
	
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	
}
