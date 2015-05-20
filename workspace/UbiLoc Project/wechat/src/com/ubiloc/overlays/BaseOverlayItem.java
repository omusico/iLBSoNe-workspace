package com.ubiloc.overlays;

import org.mapsforge.android.maps.Projection;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * ubiloc 的图层接口
 * 
 * @author crazy
 * @Date 2015-5-20
 */
public interface BaseOverlayItem {
	/**
	 * 绘制图层的接口
	 * 
	 * @param canvas
	 * @param projection
	 * @param paint
	 */
	public void draw(Canvas canvas, Projection projection, Paint paint);

	/**
	 * 获取当前图层的key值
	 * 
	 * @return
	 */
	public String getKey();

}
