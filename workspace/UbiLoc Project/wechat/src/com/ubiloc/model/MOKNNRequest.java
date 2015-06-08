package com.ubiloc.model;



public class MOKNNRequest{
	/**
	 * 用户的id
	 */
	private String userID;
	
	/**
	 * k值
	 */
	private String KKey;
	
	/**
	 * 构造函数
	 * @param uid
	 * @param range
	 */
	public MOKNNRequest(String uid, String kkey){
		this.userID = uid;
		this.KKey = kkey;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getKKey() {
		return KKey;
	}

	public void setKKey(String kKey) {
		KKey = kKey;
	}
	
	
}
