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
 * ��ͼ��ʾ���棬Ҳ�������棬�ڴ˽����Ͽ���ʵ����Աλ�� ��ʵʱ��ʾ������·������ʾ�Լ�������ͼ�Ĳ����������Ŵ���С���ƶ��ȡ�
 * 
 * @author �����
 * @version 1.0
 */
public class MapActivity extends Activity {

	/** �㲥�����ߣ�����PDR���񷵻صĺ�λ������ */
	private PdrServiceReceiver pdrReceiver;
	boolean started = false;

	// ��Activity�״δ���ʱ����
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final RelativeLayout rl = new RelativeLayout(this);

		// ������λ���� ע�⣺��AndroidManifest.xml����ע��
		// �ο��������£�
		// <service
		// android:enabled="true"
		// android:name=".PDR.PDRService"
		// android:exported="true" >
		// <intent-filter >
		// <action android:name="com.ubirtls.PDR.pDRService" />
		// <category android:name="android.intent.category.DEFAULT" />
		// </intent-filter>
		// </service>
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
	 * PDR����㲥�����ߣ����պ�λ������ ���PDR�������˲����������꣨positionX��positionY����
	 * 
	 * @author �����
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

	// ��Activity����������������
	public void onStart() {
		super.onStart();

		// this.setTitle(R.string.map);
	}

	// ��Activity��ͣʱ����
	protected void onPause() {
		super.onPause();
	}

	// onStart������
	protected void onResume() {
		// ע��㲥������
		super.onResume();
	}

	// ��Activity ����ʱ���� ����һЩ����رշ���
	protected void onDestroy() {
		super.onDestroy();
		/** �����ϵͳע���Receiver */
		this.unregisterReceiver(pdrReceiver);
		/** �ر�PDR���� */
		Intent intent = new Intent(MapActivity.this, PDRService.class);
		stopService(intent);
		Log.i("����ر�", "MapActivity");
	}

	// @Override
	// public boolean onTrackballEvent(final MotionEvent event) {
	// // return this.mapView.onTrackballEvent(event);
	// }

}
