package com.ubiloc.model;



public class MORangeRequest{
	/**
	 * 用户的id
	 */
	private String userID;
	
	/**
	 * 查询的范围
	 */
	private String range;
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getRange() {
		return range;
	}

	public void setRange(String range) {
		this.range = range;
	}

	/**
	 * 构造函数
	 * @param uid
	 * @param range
	 */
	public MORangeRequest(String uid, String range){
		this.userID = uid;
		this.range = range;
	}
	
	
}
