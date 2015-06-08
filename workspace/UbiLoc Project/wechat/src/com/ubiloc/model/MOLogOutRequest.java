package com.ubiloc.model;



public class MOLogOutRequest{
	/**
	 * 登出用户的ID
	 */
	private String userID;
	
	public MOLogOutRequest(String uid){
		this.userID = uid;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	
	
	
}
