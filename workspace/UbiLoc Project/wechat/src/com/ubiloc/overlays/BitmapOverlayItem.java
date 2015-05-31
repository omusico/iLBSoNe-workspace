package com.ubiloc.overlays;

import org.mapsforge.core.model.GeoPoint;

import android.content.Context;
import android.graphics.Bitmap;

import com.ubiloc.tools.BitmapTools;

/**
 * 绘制图形图层时，每一个图斑所需包含的数据
 * 
 * @author crazy
 * @Date 2015-5-22
 */
public class BitmapOverlayItem {
	private Context context;
	/**
	 * 图形图斑的经纬度
	 */
	private GeoPoint mCoord;
	/**
	 * 图片
	 */
	private Bitmap mBitmap;

	public BitmapOverlayItem() {
	}

	/**
	 * 2015-5-22
	 * 
	 * @param coord
	 *            经纬度
	 * @param bitmap
	 *            显示的图片
	 */
	public BitmapOverlayItem(Context context, GeoPoint coord, Bitmap bitmap) {
		this.context = context;
		this.mCoord = coord;
		this.mBitmap = bitmap;
	}

	/**
	 * 2015-5-22
	 * 
	 * @param coord
	 *            经纬度
	 * @param rId
	 *            图片资源的id
	 */
	public BitmapOverlayItem(Context context, GeoPoint coord, int rId) {
		this.context = context;
		this.mCoord = coord;
		if (context != null) {
			this.mBitmap = BitmapTools.getBitmapById(context, rId);
		}
	}

	public GeoPoint getCoord() {
		return mCoord;
	}

	/**
	 * 设置图片图斑对应的经纬度
	 * 
	 * @param mCoord
	 * @return
	 */
	public BitmapOverlayItem setCoord(GeoPoint mCoord) {
		this.mCoord = mCoord;
		return this;
	}

	/**
	 * 返回当前的图片资源
	 * 
	 * @return
	 */
	public Bitmap getBitmap() {
		return mBitmap;
	}

	/**
	 * 设置显示的图片
	 * 
	 * @param mBitmap
	 *            图片资源
	 * @return
	 */
	public BitmapOverlayItem setBitmap(Bitmap mBitmap) {
		this.mBitmap = mBitmap;
		return this;
	}

	/**
	 * 设置显示的图片
	 * 
	 * @param rId
	 *            图片资源对应的id
	 * @return
	 */
	public BitmapOverlayItem setBitmap(int rId) {
		if (context != null) {
			this.mBitmap = BitmapTools.getBitmapById(context, rId);
		}
		return this;
	}

	// private Bitmap getBitmapById(Context context, int rId) {
	// InputStream is = context.getResources().openRawResource(rId);
	// BitmapFactory.Options opts = new BitmapFactory.Options();
	// opts.inSampleSize = 2;
	// Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
	// return bitmap;
	// }
}
