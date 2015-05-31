package com.ubiloc.overlays;

import java.util.List;

import org.mapsforge.android.maps.Projection;
import org.mapsforge.core.model.GeoPoint;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

/**
 * 线状多边形图层，不填充
 * 
 * @author crazy
 * @Date 2015-5-20
 */
public class LineOverlay implements BaseOverlayItem {
	/**
	 * 点的坐标，经纬度构成
	 */
	private List<GeoPoint> mCoords;
	/**
	 * 当前图层的标识
	 */
	private String mkey;

	public LineOverlay() {
		mkey = "";
	}

	@Override
	public void draw(Canvas canvas, Projection projection, Paint paint) {
		if (mCoords != null && mCoords.size() > 0) {
			paint.setColor(Color.GREEN);
			paint.setAlpha(130);
			paint.setAntiAlias(true);// 去除锯齿
			Point lastScreenPoint = new Point();
			projection.toPixels(mCoords.get(0), lastScreenPoint);
			for (GeoPoint geoPoint : mCoords) {
				Point screenPoint = new Point();
				projection.toPixels(geoPoint, screenPoint);
				canvas.drawPoint(screenPoint.x, screenPoint.y, paint);
				canvas.drawLine(lastScreenPoint.x, lastScreenPoint.y,
						screenPoint.x, screenPoint.y, paint);
				lastScreenPoint = screenPoint;
			}
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
