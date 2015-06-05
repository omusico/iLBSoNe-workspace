package com.ubiloc.model;

import java.io.Serializable;

public class MovingObj implements Serializable{
	private int type;//消息类型标示
	private String userid;
	private double lon;
	private double lat;
	
	
	
	public MovingObj(int type,String userid, double lon, double lat) {
		this.type=type;
		this.userid = userid;
		this.lon = lon;
		this.lat = lat;
	}
	public MovingObj() {
		
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
