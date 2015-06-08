package com.ubiloc.model;



public class MORangeStopRequest{
	/**
	 * 用户Id
	 */
	private String userID;
	
	/**
	 * 构造函数
	 * @param uid
	 */
	public MORangeStopRequest(String uid){
		this.userID = uid;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	
}
