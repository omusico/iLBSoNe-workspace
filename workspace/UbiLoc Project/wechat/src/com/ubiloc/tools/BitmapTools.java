package com.ubiloc.tools;

import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 位图处理工具类，该类所有方法均为静态方法，且该类不可初始化
 * 
 * @author crazy
 * @Date 2015-5-22
 */
public class BitmapTools {

	private BitmapTools() {
	}

	public static Bitmap getBitmapById(Context context, int rId) {
		InputStream is = context.getResources().openRawResource(rId);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inSampleSize = 1;
		Bitmap bitmap = BitmapFactory.decodeStream(is, null, opts);
		return bitmap;
	}

}
