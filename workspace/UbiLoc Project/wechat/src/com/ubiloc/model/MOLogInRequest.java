package com.ubiloc.model;

import java.io.Serializable;



public class MOLogInRequest implements Serializable{
	/**
	 * 登录用户的ID
	 */
	private String userID;
	
	public MOLogInRequest(String uid){
		this.userID = uid;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
	
	
}
