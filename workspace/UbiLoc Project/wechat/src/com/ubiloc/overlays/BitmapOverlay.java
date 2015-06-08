package com.ubiloc.overlays;

import java.util.List;

import org.mapsforge.android.maps.Projection;
import org.mapsforge.core.model.GeoPoint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.donal.wechat.R;
import com.ubiloc.tools.BitmapTools;

/**
 * 绘制图形图层
 * 
 * @author crazy
 * @Date 2015-5-20
 */
public class BitmapOverlay implements BaseOverlayItem {
	/**
	 * 图形图斑
	 */
	private List<BitmapOverlayItem> mOverlayItems;
	/**
	 * 当前图层的标识
	 */
	private String mkey;
	private Context context;

	public BitmapOverlay(Context context) {
		mkey = "";
	}

	@Override
	public void draw(Canvas canvas, Projection projection, Paint paint) {
		if (mOverlayItems != null) {
			paint.setColor(Color.RED);
			paint.setAlpha(130);
			paint.setAntiAlias(true);// 去除锯齿
			for (BitmapOverlayItem overlayItem : mOverlayItems) {
				GeoPoint geoPoint = overlayItem.getCoord();
				if (geoPoint != null) {
					Bitmap bitmap = overlayItem.getBitmap();
					if (bitmap == null)
						bitmap = BitmapTools.getBitmapById(context,
								R.drawable.draw_bitmap_default);
					Point screenPoint = new Point();
					projection.toPixels(geoPoint, screenPoint);
					canvas.drawBitmap(bitmap, screenPoint.x - bitmap.getWidth()
							/ 2, screenPoint.y - bitmap.getHeight() / 2, paint);
				}
			}
		}
	}

	@Override
	public String getKey() {
		return mkey;
	}

	public void setBitmapOverlayItems(List<BitmapOverlayItem> overlayItems) {
		this.mOverlayItems = overlayItems;
	}

	public void setKey(String key) {
		this.mkey = key;
	}
}
