package com.ubirtls.view.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

	// 在Activity首次创建时调用
	public void onCreate(final Bundle savedInstanceState) {

		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
		} catch (Exception e) {
			// TODO Auto-generated catch block

			Log.e("ERROR", "ERROR IN CODE: " + e.toString());
			e.printStackTrace();

		}

		Intent intent = new Intent(MapActivity.this, PDRService.class);
		startService(intent);

		try {
			IntentFilter filter;
			filter = new IntentFilter(PDRService.HIPPO_SERVICE_IDENTIFIER);
			this.pdrReceiver = new PdrServiceReceiver();
			registerReceiver(pdrReceiver, filter);
		} catch (Exception e) {
			e.getStackTrace();
		}
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
			// TODO Auto-generated method stub
			Bundle bundle = intent.getExtras();
			double positionX = bundle.getDouble("positionX");
			double positionY = bundle.getDouble("positionY");
			Log.i("xxxx", String.valueOf(positionX));
			Log.i("yyyy", String.valueOf(positionY));
			// Controller.getInstance().notifyLocation(positionX, positionY);
			// Controller.getInstance().sentPDRPosition(positionX, positionY);
			Toast toast = Toast.makeText(getApplicationContext(), "("
					+ positionX + "," + positionY + ")", Toast.LENGTH_SHORT);
			toast.show();
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

	// 在Activity 销毁时调用 进行一些程序关闭服务
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
