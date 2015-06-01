package com.ubiloc.navigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.ubirtls.PDR.PDRService;
import com.ubirtls.tools.LocationProjection;

/**
 * PDR定位方法,单例，需要手动销毁
 * 
 * @author crazy
 * @Date 2015-6-1
 */
public class PdrManager {
	private static Context mContext;
	private static PdrManager mPdrManager;
	/**
	 * 监听位置的变化
	 */
	private OnNavigationListener mOnNavigationListener;
	/** 广播接收者，接收PDR服务返回的航位推算结果 */
	private PdrServiceReceiver pdrReceiver;

	private PdrManager() {
	}

	public static void init(Context context) {
		mContext = context;
	}

	public static PdrManager getInstance() {
		if (mPdrManager == null)
			mPdrManager = new PdrManager();
		return mPdrManager;
	}

	/**
	 * start navagation service
	 */
	public void startPDR() {
		if (mContext != null) {
			Intent intent = new Intent(mContext, PDRService.class);
			Bundle data = new Bundle();
			data.putCharSequence("Ori", 90 + "");
			// data.putCharSequence("Y", y + "");
			intent.putExtras(data);
			mContext.startService(intent);
			try {
				IntentFilter filter;
				filter = new IntentFilter(PDRService.HIPPO_SERVICE_IDENTIFIER);
				this.pdrReceiver = new PdrServiceReceiver();
				mContext.registerReceiver(pdrReceiver, filter);
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
	}

	/**
	 * stop navigation service
	 */
	public void stopPDR() {
		if (mContext != null) {
			/** 解除向系统注册的Receiver */
			mContext.unregisterReceiver(pdrReceiver);
			/** 关闭PDR服务 */
			Intent intent = new Intent(mContext, PDRService.class);
			mContext.stopService(intent);
		}
	}

	/**
	 * set navagation listener
	 * 
	 * @param listener
	 */
	public void setOnNavigationListener(OnNavigationListener listener) {
		this.mOnNavigationListener = listener;
	}

	/**
	 * PDR服务广播接收者，接收航位推算结果 获得PDR、粒子滤波计算结果坐标（positionX，positionY）。
	 * 
	 * @author 胡旭科
	 * 
	 */
	public class PdrServiceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			double positionX = bundle.getDouble("positionX");
			double positionY = bundle.getDouble("positionY");
			LocationProjection.Local2WGS84(0, 0, 0, 0, 0);
			if (mOnNavigationListener != null) {
				mOnNavigationListener.OnPositionChanged(positionX, positionY);
			}
		}
	}
}
