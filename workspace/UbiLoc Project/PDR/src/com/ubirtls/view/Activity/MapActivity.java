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
 * ��ͼ��ʾ���棬Ҳ�������棬�ڴ˽����Ͽ���ʵ����Աλ�� ��ʵʱ��ʾ������·������ʾ�Լ�������ͼ�Ĳ����������Ŵ���С���ƶ��ȡ�
 * 
 * @author �����
 * @version 1.0
 */
public class MapActivity extends Activity {

	/** �㲥�����ߣ�����PDR���񷵻صĺ�λ������ */
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

	// ��Activity�״δ���ʱ����
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
						"�������������ƫ��Ϊ��(" + a + ")", Toast.LENGTH_SHORT);
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

			MapXYToWGS84(pre_B, per_L, positionX, positionY, a);

			Log.i("xxxx", String.valueOf(cur_B));
			Log.i("yyyy", String.valueOf(cur_L));
			// Controller.getInstance().notifyLocation(positionX, positionY);
			// Controller.getInstance().sentPDRPosition(positionX, positionY);
			Toast toast = Toast.makeText(getApplicationContext(), "("
					+ positionX + "," + positionY + "����Ϊ��" + cur_B + "γ��Ϊ��"
					+ cur_L + ")", Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	/**
	 * ��γ��
	 * 
	 * @param pre_B
	 *            ��ʼ����
	 * @param pre_L
	 *            ��ʼγ��
	 * @param positionx
	 *            �����X����
	 * @param positiony
	 *            �����Y����
	 * @param a
	 *            �������������ƫ��
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
}