package com.ubiloc.search;

import android.view.View;

/**
 * poi搜索列表中定位按钮点击的响应事件
 * 
 * @author crazy
 * @Date 2015-6-7
 */
public interface OnPoiItemLoacteListener {
	/**
	 * poi搜索列表中定位按钮的点击响应响应
	 * 
	 * @param view
	 * @param postion
	 */
	public void OnClick(View view, int postion);
}
