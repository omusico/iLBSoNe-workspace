package com.ubiloc.overlays;

import java.util.List;

import org.mapsforge.android.maps.Projection;
import org.mapsforge.core.GeoPoint;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.util.Log;

/**
 * 点状图层，不连线和填充面
 * 
 * @author crazy
 * @Date 2015-5-20
 */
public class PointOverlay implements BaseOverlayItem {
	/**
	 * 点的坐标，经纬度构成
	 */
	private List<GeoPoint> mCoords;
	/**
	 * 当前图层的标识
	 */
	private String mkey;

	public PointOverlay() {
	}

	@Override
	public void draw(Canvas canvas, Projection projection, Paint paint) {

		if (mCoords != null && mCoords.size() > 0) {
			if (mCoords.size() > 1) {
				paint.setColor(Color.RED);
				paint.setAlpha(130);
				if (mCoords.size() == 2) {
					Log.i("nk", "mCoords----->" + mCoords);
					GeoPoint geoPoint = mCoords.get(0);
					Point pp = new Point();
					projection.toPixels(geoPoint, pp);

					GeoPoint geoPoint2 = mCoords.get(1);
					Point pp2 = new Point();
					projection.toPixels(geoPoint2, pp2);
					canvas.drawLine(pp.x, pp.y, pp2.x, pp2.y, paint);

				} else {
					Path path = new Path();
					GeoPoint geoPoint = mCoords.get(0);
					Point pp = new Point();
					projection.toPixels(geoPoint, pp);
					path.moveTo(pp.x, pp.y);
					for (int i = 0; i < mCoords.size(); i++) {
						GeoPoint geoPoint2 = mCoords.get(i);
						Point p = new Point();
						projection.toPixels(geoPoint2, p);
						path.lineTo(p.x, p.y);
					}
					// path.close();
					canvas.drawPath(path, paint);
					PathEffect effect = new DashPathEffect(new float[] { 1, 2,
							4, 8 }, 1);
					paint.setAntiAlias(true);
					paint.setPathEffect(effect);
					GeoPoint geoPoint2 = mCoords.get(mCoords.size() - 1);
					Point pp2 = new Point();
					projection.toPixels(geoPoint2, pp2);
					canvas.drawLine(pp.x, pp.y, pp2.x, pp2.y, paint);
					PathEffect effect1 = new DashPathEffect(
							new float[] { 0, 0 }, 0);

					paint.setPathEffect(effect1);
				}
			} else {
				if (mCoords.size() == 1) {
					GeoPoint geoPoint = mCoords.get(0);
					Point pp = new Point();
					projection.toPixels(geoPoint, pp);
					canvas.drawCircle(pp.x, pp.y, 5, paint);
				}
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
