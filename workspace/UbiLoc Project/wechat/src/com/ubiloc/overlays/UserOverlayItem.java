package com.ubiloc.overlays;

import org.mapsforge.core.model.GeoPoint;

import android.content.Context;
import android.graphics.Bitmap;

import com.donal.wechat.R;
import com.ubiloc.tools.BitmapTools;
import com.ubirtls.tools.LocationProjection;

/**
 * 绘制图形图层时，每一个图斑所需包含的数据
 * 
 * @author crazy
 * @Date 2015-5-22
 */
public class UserOverlayItem {
	private double pre_lon = 109.514212;
	private double pre_lat = -0.000533;
	private double pre_x = 81.230795;
	private double pre_y = 21.948902;
	public static final int USER_ICON_COUNT = 5;
	private Context context;
	/**
	 * 图形图斑的经纬度
	 */
	private GeoPoint mCoord;
	/**
	 * 图片
	 */
	private Bitmap mBitmap;

	public UserOverlayItem() {
	}

	/**
	 * 2015-5-22
	 * 
	 * @param coord
	 *            经纬度
	 * @param id
	 *            用户的id
	 */
	public UserOverlayItem(Context context, int id, int x, int y) {
		this.context = context;
		double[] temp = LocationProjection.Local2WGS84(pre_lat, pre_lon, x, y,
				pre_x, pre_y, 0);
		this.mCoord = new GeoPoint(temp[0], temp[1]);
		if (context != null) {
			int user_img_num = id % 6;
			int user_img_id = R.drawable.user_icon_1;
			switch (user_img_num) {
			case 0: {
				user_img_id = R.drawable.user_icon_1;
				break;
			}
			case 1: {
				user_img_id = R.drawable.user_icon_2;
				break;
			}
			case 2: {
				user_img_id = R.drawable.user_icon_3;
				break;
			}
			case 3: {
				user_img_id = R.drawable.user_icon_4;
				break;
			}
			case 4: {
				user_img_id = R.drawable.user_icon_5;
				break;
			}
			case 5: {
				user_img_id = R.drawable.user_icon_1;
				break;
			}
			default:
				break;
			}
			this.mBitmap = BitmapTools.getBitmapById(context, user_img_id);
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
	public UserOverlayItem setCoord(GeoPoint mCoord) {
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

}
