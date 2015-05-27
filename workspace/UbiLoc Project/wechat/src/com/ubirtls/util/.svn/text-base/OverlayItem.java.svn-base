package com.ubirtls.util;


import android.graphics.Bitmap;

import coordinate.TwoDCoordinate;

/**
 * 在地图上标示的Item
 * @author 胡旭科
 * @version 1.0
 */
public class OverlayItem {
	/**Item描述*/
	private  POI poi = null;
	/**Item的drawable对象*/
	private  Bitmap marker;
	/**
	 * 构造函数
	 * @param description item描述
	 * @param coordinate item坐标
	 * @param marker item图标
	 */
	public OverlayItem(String description, TwoDCoordinate coordinate, Bitmap marker){
		poi = new POI(description, coordinate);
		this.marker = marker;
	}
	/**
	 * 构造函数
	 * @param poi POI对象
	 * @param marker Bitmap对象
	 */
	public OverlayItem(POI poi, Bitmap marker){
		this.poi = poi;
		this.marker = marker;
	}
	/**
	 * 获得item描述
	 * @return 返回description
	 */
	public String getDescription() {
		return poi.description;
	} 
	/**
	 * 获得item坐标
	 * @return 返回coordinate
	 */
	public TwoDCoordinate getCoordinate(){
		return poi.coordinate;
	}
	/**
	 * 获得item图标
	 * @return 返回marker
	 */
	public Bitmap getMarker(){
		return marker;
	}
	/**
	 * 设置item描述
	 * 
	 */
	public void setDescription(String description) {
		poi.setDescription(description);
	} 
	/**
	 * 设置item坐标
	 * 
	 */
	public void setCoordinate(TwoDCoordinate coordinate){
		 poi.setCoordinate(coordinate);
	}
	/**
	 * 设置item图标
	 * 
	 */
	public void setMarker(Bitmap marker){
		this.marker = marker;
	}
	/**重载equals函数*/
	@Override
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(! (o instanceof OverlayItem))
			return false;
		if(((OverlayItem)o).poi.equals(poi))
			return true;
		return false;
	}
}
