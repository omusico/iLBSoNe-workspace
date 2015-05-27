package com.ubirtls.view.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.ubirtls.PDR.PDRService;

/**
 * 地图显示界面，也是主界面，在此界面上可以实现人员位置 的实时显示，导航路径的显示以及其他地图的操作，包括放大，缩小，移动等。
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class MapActivity extends Activity {

	/** 广播接收者，接收PDR服务返回的航位推算结果 */
	private PdrServiceReceiver pdrReceiver;
	boolean started = false;

	// 在Activity首次创建时调用
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final RelativeLayout rl = new RelativeLayout(this);

		try {
			IntentFilter filter;
			filter = new IntentFilter(PDRService.HIPPO_SERVICE_IDENTIFIER);
			this.pdrReceiver = new PdrServiceReceiver();
			registerReceiver(pdrReceiver, filter);
		} catch (Exception e) {
			e.getStackTrace();
		}

		this.setContentView(rl);
	}

	/**
	 * PDR服务广播接收者，接收航位推算结果
	 * 
	 * @author 胡旭科
	 * 
	 */
	public class PdrServiceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			Bundle bundle = intent.getExtras();
			double positionX = bundle.getDouble("positionX");
			double positionY = bundle.getDouble("positionY");
			Log.i("xxxx", String.valueOf(positionX));
			Log.i("yyyy", String.valueOf(positionY));
			// Controller.getInstance().notifyLocation(positionX, positionY);
			// Controller.getInstance().sentPDRPosition(positionX, positionY);

		}

	}

	// 在Activity创建后或重启后调用
	public void onStart() {
		super.onStart();

		// this.setTitle(R.string.map);
	}

	// 在Activity暂停时调用
	protected void onPause() {
		super.onPause();
	}

	// onStart后会调用
	protected void onResume() {
		// 注册广播接收器
		super.onResume();
	}

	// 在Activity 销毁时调用 进行一些程序关闭的处理 包括关闭 spotter 连接以及清除一些缓存数据
	protected void onDestroy() {
		super.onDestroy();
		/** 解除向系统注册的Receiver */
		this.unregisterReceiver(pdrReceiver);
		/** 关闭PDR服务 */
		Intent intent = new Intent(MapActivity.this, PDRService.class);
		stopService(intent);
		Log.i("服务关闭", "MapActivity");
	}

	// @Override
	// public boolean onTrackballEvent(final MotionEvent event) {
	// // return this.mapView.onTrackballEvent(event);
	// }

}
