package com.ubiloc.pdr;

/**
 * 定位监听事件
 * 
 * @author crazy
 * @Date 2015-6-1
 */
public interface OnNavigationListener {
	public void OnPositionChanged(double x, double y, double lat, double lon);
}
