package com.ubiloc.search;

/**
 * POI的类别
 * 
 * @author crazy
 * @Date 2015-6-5
 */
public interface PoiClass {
	/**
	 * 消防栓
	 */
	public static final int POI_CLASS_FIRE_HYDRAN = 1;

	/**
	 * 安全出口
	 */
	public static final int POI_CLASS_EXIT = 2;

	/**
	 * 楼梯
	 */
	public static final int POI_CLASS_STAIR = 3;
	/**
	 * 电梯
	 */
	public static final int POI_CLASS_ELEVATOR = 4;
	/**
	 * 厕所
	 */
	public static final int POI_CLASS_WC = 5;
	/**
	 * 实验室
	 */
	public static final int POI_CLASS_LAB = 6;
	/**
	 * 会议室
	 */
	public static final int POI_CLASS_BOARD_ROOM = 7;
	/**
	 * 通风口
	 */
	public static final int POI_CLASS_AIR_VENT = 8;
}
