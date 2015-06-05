package com.ubiloc.search;

import org.mapsforge.core.model.GeoPoint;

public class PoiObject {
	/**
	 * POI的类别
	 */
	private int poi_class;
	/**
	 * POI的名称
	 */
	private String poi_name;
	/**
	 * POI的坐标（84坐标系）
	 */
	private GeoPoint poi_loc;
	/**
	 * POI的所在楼层
	 */
	private int poi_floor;
	/**
	 * POI信息
	 */
	private String poi_description;

	public String getPoi_description() {
		return poi_description;
	}

	public void setPoi_description(String poi_description) {
		this.poi_description = poi_description;
	}

	public PoiObject() {
	}

	public PoiObject(int poi_class, String poi_name, GeoPoint poi_loc,
			int poi_floor, String poi_description) {
		super();
		this.poi_class = poi_class;
		this.poi_name = poi_name;
		this.poi_loc = poi_loc;
		this.poi_floor = poi_floor;
		this.poi_description = poi_description;
	}

	public int getPoi_class() {
		return poi_class;
	}

	public void setPoi_class(int poi_class) {
		this.poi_class = poi_class;
	}

	public String getPoi_name() {
		return poi_name;
	}

	public void setPoi_name(String poi_name) {
		this.poi_name = poi_name;
	}

	public GeoPoint getPoi_loc() {
		return poi_loc;
	}

	public void setPoi_loc(GeoPoint poi_loc) {
		this.poi_loc = poi_loc;
	}

	public int getPoi_floor() {
		return poi_floor;
	}

	public void setPoi_floor(int poi_floor) {
		this.poi_floor = poi_floor;
	}

}
