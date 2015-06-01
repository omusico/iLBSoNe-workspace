package com.ubiloc.geofencing;

import java.io.Serializable;

public class GeofenceMsg implements Serializable, Comparable<GeofenceMsg> {
	private String id;
	private String msgcontent;
	private Integer msgstatus; // 状态 0已读 1未读
	private String msgtime;
	private Integer msgtype;
	private static final long serialVersionUID = 1L;
	public static final int OTHER = 1;
	public static final int SYS_MSG = 2; 
	public static final int GEO_MSG = 3;
	public static final int READ = 0;
	public static final int UNREAD = 1;
	public static final int All = 2;

	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMsgcontent() {
		return msgcontent;
	}

	public void setMsgcontent(String msgcontent) {
		this.msgcontent = msgcontent;
	}

	public Integer getMsgstatus() {
		return msgstatus;
	}

	public void setMsgstatus(Integer msgstatus) {
		this.msgstatus = msgstatus;
	}

	public String getMsgtime() {
		return msgtime;
	}

	public void setMsgtime(String msgtime) {
		this.msgtime = msgtime;
	}

	public Integer getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(Integer msgtype) {
		this.msgtype = msgtype;
	}

	public GeofenceMsg() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int compareTo(GeofenceMsg another) {
		// TODO Auto-generated method stub
		return 0;
	}

}
