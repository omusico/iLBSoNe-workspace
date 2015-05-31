package com.ubirtls.view.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

	private static final double PI = 3.1415926;

	private EditText editText;
	// private EditText editText2;
	private Button btnStart;

	// private float x;
	// private float y;

	private double a = 0;
	private static double r = 0;

	private static double pre_B = 0;
	private static double per_L = 0;

	private static double cur_B = 0;
	private static double cur_L = 0;

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

		btnStart = (Button) findViewById(R.id.btnStart);
		btnStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				editText = (EditText) findViewById(R.id.edit_tip1);
				// editText2 = (EditText) findViewById(R.id.edit_tip2);
				String ori = editText.getText().toString();
				if (ori != null && !ori.equals(""))
					a = Double.parseDouble(ori);
				// String Y = editText2.getText().toString();
				// x = Float.parseFloat(X);
				// y = Float.parseFloat(Y);
				//
				Toast toast = Toast.makeText(getApplicationContext(),
						"您与正北方向的偏角为：(" + a + ")", Toast.LENGTH_SHORT);
				toast.show();

				Intent intent = new Intent(MapActivity.this, PDRService.class);
				Bundle data = new Bundle();
				data.putCharSequence("Ori", ori + "");
				// data.putCharSequence("Y", y + "");
				intent.putExtras(data);
				startService(intent);

			}
		});

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

			MapXYToWGS84(pre_B, per_L, positionX, positionY, a);

			Log.i("xxxx", String.valueOf(cur_B));
			Log.i("yyyy", String.valueOf(cur_L));
			// Controller.getInstance().notifyLocation(positionX, positionY);
			// Controller.getInstance().sentPDRPosition(positionX, positionY);
			Toast toast = Toast.makeText(getApplicationContext(), "("
					+ positionX + "," + positionY + "经度为：" + cur_B + "纬度为："
					+ cur_L + ")", Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	/**
	 * 经纬度
	 * 
	 * @param pre_B
	 *            初始经度
	 * @param pre_L
	 *            初始纬度
	 * @param positionx
	 *            相对于X坐标
	 * @param positiony
	 *            相对于Y坐标
	 * @param a
	 *            相对于正北方的偏角
	 * @return
	 */
	public static Point MapXYToWGS84(double pre_B, double pre_L,
			double positionx, double positiony, double a) {

		r = Math.pow((positionx * positionx + positiony * positiony), 2);

		cur_L = per_L + (r * Math.sin(a * PI / 180))
				/ (111000 * Math.cos(pre_B * PI / 180));
		cur_B = pre_B + (r * Math.cos(a * PI / 180)) / 111000;

		return new Point();
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
}