package com.ubiloc.overlays;

import java.util.List;

import org.mapsforge.android.maps.Projection;
import org.mapsforge.core.GeoPoint;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

/**
 * 面状多边形图层
 * 
 * @author crazy
 * @Date 2015-5-20
 */
public class PolygonOverlay implements BaseOverlayItem {
	/**
	 * 点的坐标，经纬度构成
	 */
	private List<GeoPoint> mCoords;
	/**
	 * 当前图层的标识
	 */
	private String mkey;

	public PolygonOverlay() {
		mkey = "";
	}

	@Override
	public void draw(Canvas canvas, Projection projection, Paint paint) {
		if (mCoords != null && mCoords.size() > 0) {
			paint.setColor(Color.GREEN);
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			paint.setAlpha(130);
			paint.setAntiAlias(true);// 去除锯齿
			Point firstScreenPoint = new Point();
			Path path = new Path();
			projection.toPixels(mCoords.get(0), firstScreenPoint);
			path.moveTo(firstScreenPoint.x, firstScreenPoint.y);

			for (GeoPoint geoPoint : mCoords) {
				Point screenPoint = new Point();
				projection.toPixels(geoPoint, screenPoint);
				path.lineTo(screenPoint.x, screenPoint.y);
			}
			path.lineTo(firstScreenPoint.x, firstScreenPoint.y);

			canvas.drawPath(path, paint);
		}

	}

	@Override
	public String getKey() {
		return mkey;
	}

	public void setCoords(List<GeoPoint> coords) {
		this.mCoords = coords;
	}

	public void setKey(String key) {
		this.mkey = key;
	}

}
