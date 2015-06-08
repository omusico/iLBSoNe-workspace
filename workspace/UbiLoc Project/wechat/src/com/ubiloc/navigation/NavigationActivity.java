package com.ubiloc.navigation;

import org.mapsforge.core.model.GeoPoint;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Spinner;

import com.donal.wechat.R;
import com.ubiloc.ubilocmap.UbilocMap;

public class NavigationActivity extends Activity implements OnClickListener {
	/**
	 * 回退键
	 */
	private View back;
	/**
	 * 切换至动态导航
	 */
	private View navigation_user;
	/**
	 * 切换至静态导航
	 */
	private View switch2navigation_poi;
	/**
	 * 路线查询
	 */
	private View switch2navigation_search;

	private Spinner navigation_select_friend;
	/**
	 * POI导航
	 */
	public static final int NAV_POI = 1;
	public static final String KEY = "key";
	/**
	 * 处理线程消息
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NAV_POI: {
				Bundle data = msg.getData();
				GeoPoint centerPoint = (GeoPoint) data.getSerializable(KEY);
				UbilocMap.getInstance().setMapCenter(centerPoint);
				break;
			}
			default:
				break;
			}
		}

	};

	public NavigationActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation_main);
		initView();
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		back = findViewById(R.id.back);
		navigation_user = findViewById(R.id.navigation_user);
		switch2navigation_poi = findViewById(R.id.navigation_poi);
		switch2navigation_search = findViewById(R.id.navigation_search);
		back.setOnClickListener(this);
		navigation_user.setOnClickListener(this);
		switch2navigation_poi.setOnClickListener(this);
		switch2navigation_search.setOnClickListener(this);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back: {// 返回
			this.finish();
			break;
		}
		case R.id.navigation_user: {// 动态导航
			break;
		}
		case R.id.navigation_poi: {// 静态导航
			break;
		}
		case R.id.navigation_search: {// 路线搜索
		// new Thread(new Runnable() {
		// private double lat = -0.000396;
		// private double lon = 109.514149;
		//
		// @Override
		// public void run() {
		// // -0.000715,109.514151
		// // 2： -0.000606,109.514149
		// // 3： -0.000606,109.514162
		// // 4： -0.000531,109.514215
		// // 5： -0.000419,109.514050
		// // 6： -0.000420,109.513585
		// // 7： -0.000394,109.513585
		// // 8： -0.000396,109.513541
		// while (true) {
		// Bundle data = new Bundle();
		// data.putSerializable(KEY, new GeoPoint(lat, lon));
		// Message msg = new Message();
		// msg.what = NAV_POI;
		// msg.setData(data);
		// handler.sendMessageDelayed(msg, 100);
		// // handler.sendMessage(msg);
		// lon += 0.000001;
		// // try {
		// // Thread.sleep(100);
		// // } catch (InterruptedException e) {
		// // e.printStackTrace();
		// // }
		// }
		// }
		// }).run();
			NavigationActivity.this.finish();
			break;
		}
		default:
			break;
		}
	}
}
