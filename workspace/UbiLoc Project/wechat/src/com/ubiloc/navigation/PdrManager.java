package com.ubiloc.navigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.donal.wechat.R;
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
	public static final String SP_KEY = "shared_preference_key";
	public static final String LON_KEY = "lon";
	public static final String LAT_KEY = "lat";
	public static final String ORI_KEY = "ori";
	/**
	 * 本地化部分数据
	 */
	private SharedPreferences mSharedPreferences;

	/** pre_B初始纬度，pre_L初始经度，Angle与正北的偏角 */
	double pre_B = 0;
	double per_L = 0;
	double Angle = 0;
	double Angle_a = 0;

	private PdrManager() {
		if (mContext != null) {
			mSharedPreferences = mContext.getSharedPreferences(SP_KEY,
					Activity.MODE_PRIVATE);
		}
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
			double[] originalData = getOriginalDataBySP();
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle(R.string.input_pdr_origin_position);
			LayoutInflater inlfater = LayoutInflater.from(mContext);
			View dialog_content = inlfater.inflate(
					R.layout.input_pdr_origin_data, null);
			final EditText input_lon = (EditText) dialog_content
					.findViewById(R.id.input_lon);
			final EditText input_lat = (EditText) dialog_content
					.findViewById(R.id.input_lat);
			final EditText input_ori = (EditText) dialog_content
					.findViewById(R.id.input_ori);

			input_lon.setText(originalData[0] + "");
			input_lat.setText(originalData[1] + "");
			input_ori.setText(originalData[2] + "");
			builder.setView(dialog_content);
			builder.setPositiveButton(R.string.ok,
					new AlertDialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							String str_lon = input_lon.getText().toString();
							String str_lat = input_lat.getText().toString();
							String str_ori = input_ori.getText().toString();
							if (str_lon != null && !str_lon.equals("")
									&& str_lat != null && !str_lat.equals("")
									&& str_ori != null && !str_ori.equals("")) {
								try {

									double lon = Double.parseDouble(str_lon);
									double lat = Double.parseDouble(str_lat);
									double ori = Double.parseDouble(str_ori);
									saveOriginalDataBySP(new double[] { lon,
											lat, ori });

									// 获取初始经度、纬度和正北偏角。
									pre_B = lat;
									per_L = lon;
									// Angle = ori;

									Intent intent = new Intent(mContext,
											PDRService.class);
									Bundle data = new Bundle();
									data.putCharSequence("Ori", Angle + "");
									// data.putCharSequence("Y", y + "");
									intent.putExtras(data);
									mContext.startService(intent);
									try {
										IntentFilter filter;
										filter = new IntentFilter(
												PDRService.HIPPO_SERVICE_IDENTIFIER);
										pdrReceiver = new PdrServiceReceiver();
										mContext.registerReceiver(pdrReceiver,
												filter);
									} catch (Exception e) {
										e.getStackTrace();
									}

								} catch (Exception e) {

								}
							}
							dialog.dismiss();
						}

					});

			builder.setNegativeButton(R.string.cancle,
					new AlertDialog.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							dialog.dismiss();
						}

					});

			builder.create().show();

		}
	}

	/**
	 * 通过SharedPreferences获取上一次的lon.lat,ori
	 * 
	 * @return
	 */
	private double[] getOriginalDataBySP() {
		double[] originalData = new double[3];
		String str_lon = mSharedPreferences.getString(LON_KEY, "");
		String str_lat = mSharedPreferences.getString(LAT_KEY, "");
		String str_ori = mSharedPreferences.getString(ORI_KEY, "");
		if (str_lon != null && !str_lon.equals("") && str_lat != null
				&& !str_lat.equals("") && str_ori != null
				&& !str_ori.equals("")) {
			try {

				originalData[0] = Double.parseDouble(str_lon);
				originalData[1] = Double.parseDouble(str_lat);
				originalData[2] = Double.parseDouble(str_ori);
			} catch (Exception e) {
				e.getStackTrace();
			}
		}
		return originalData;
	}

	/**
	 * 通过SharedPreferences保存lon,lat,ori
	 * 
	 * @param originalData
	 */
	private void saveOriginalDataBySP(double[] originalData) {
		if (mSharedPreferences != null) {
			SharedPreferences.Editor editor = mSharedPreferences.edit();
			editor.putString(LON_KEY, originalData[0] + "");
			editor.putString(LAT_KEY, originalData[1] + "");
			editor.putString(ORI_KEY, originalData[2] + "");
			editor.commit();
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
			double[] temp = LocationProjection.Local2WGS84(pre_B, per_L,
					positionX, positionY, Angle_a);

			Log.i("相对于（0，0）的x坐标：", String.valueOf(positionX));
			Log.i("相对于（0，0）的y坐标：", String.valueOf(positionY));

			Log.i("现在的经度：", String.valueOf(temp[0]));
			Log.i("现在的纬度：", String.valueOf(temp[1]));
			Log.i("正北偏角：", String.valueOf(temp[2]));

			if (mOnNavigationListener != null) {
				mOnNavigationListener.OnPositionChanged(temp[0], temp[1]);
			}
		}
	}
}
