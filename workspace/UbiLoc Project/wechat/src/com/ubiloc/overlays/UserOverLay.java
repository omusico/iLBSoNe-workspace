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
 * 绘制用户图层
 * 
 * @author crazy
 * @Date 2015-5-20
 */
public class UserOverLay implements BaseOverlayItem {
	/**
	 * 图形图斑
	 */
	private List<UserOverlayItem> mOverlayItems;
	/**
	 * 当前图层的标识
	 */
	private String mkey;
	private Context context;

	public UserOverLay(Context context) {
		mkey = "";
	}

	@Override
	public void draw(Canvas canvas, Projection projection, Paint paint) {
		if (mOverlayItems != null) {
			paint.setColor(Color.RED);
			paint.setAlpha(130);
			paint.setAntiAlias(true);// 去除锯齿
			for (UserOverlayItem overlayItem : mOverlayItems) {
				GeoPoint geoPoint = overlayItem.getCoord();
				if (geoPoint != null) {
					Bitmap bitmap = overlayItem.getBitmap();
					if (bitmap == null)
						bitmap = BitmapTools.getBitmapById(context,
								R.drawable.user_icon_1);
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

	public void setBitmapOverlayItems(List<UserOverlayItem> overlayItems) {
		this.mOverlayItems = overlayItems;
	}

	public void setKey(String key) {
		this.mkey = key;
	}
}
