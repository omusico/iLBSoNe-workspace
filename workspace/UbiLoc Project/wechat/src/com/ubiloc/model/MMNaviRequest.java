package com.ubiloc.model;


public class MMNaviRequest{
	/**
	 * 发出查询请求的用户id
	 */
	private String userID;
	
	/**
	 * 目标点用户id
	 */
	private String tuserID;
	
	/**
	 * 构造函数
	 * @param id
	 * @param tid
	 */
	public MMNaviRequest(String id, String tid){
		this.userID = id;
		this.tuserID = tid;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getTuserID() {
		return tuserID;
	}

	public void setTuserID(String tuserID) {
		this.tuserID = tuserID;
	}
	
	
	
	
}
