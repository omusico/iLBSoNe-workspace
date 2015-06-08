package com.ubiloc.model;



public class MMNaviStopRequest{
	/**
	 * 用户id
	 */
	private String userID;
	
	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	/**
	 * 构造函数
	 * @param uid
	 */
	public MMNaviStopRequest(String uid){
		this.userID = uid;
	}
	
	
}
