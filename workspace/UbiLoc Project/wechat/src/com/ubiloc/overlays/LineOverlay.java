package com.ubiloc.overlays;

import org.mapsforge.android.maps.Projection;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 线状多边形图层，不填充
 * 
 * @author crazy
 * @Date 2015-5-20
 */
public class LineOverlay implements BaseOverlayItem {

	public LineOverlay() {
	}

	@Override
	public void draw(Canvas canvas, Projection projection, Paint paint) {

	}

	@Override
	public String getKey() {
		return null;
	}

}
