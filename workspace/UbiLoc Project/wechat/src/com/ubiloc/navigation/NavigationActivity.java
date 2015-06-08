package com.ubiloc.navigation;

import org.mapsforge.core.model.GeoPoint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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
	private View switch2navigation_user;
	/**
	 * 切换至静态导航
	 */
	private View switch2navigation_poi;
	/**
	 * 路线查询
	 */
	private View navigation_search;

	/**
	 * 选择好友
	 */
	private Spinner navigation_select_friend;
	/**
	 * 动态导航
	 */
	private View navigation_input_user;
	/**
	 * poi静态导航
	 */
	private View navigation_input_poi;

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
	@SuppressLint("CutPasteId")
	private void initView() {
		back = findViewById(R.id.back);
		switch2navigation_user = findViewById(R.id.navigation_user);
		switch2navigation_poi = findViewById(R.id.navigation_poi);
		navigation_search = findViewById(R.id.navigation_search);
		back.setOnClickListener(this);
		switch2navigation_user.setOnClickListener(this);
		switch2navigation_poi.setOnClickListener(this);
		navigation_search.setOnClickListener(this);
		navigation_input_user = findViewById(R.id.nav_input_user);
		navigation_input_poi = findViewById(R.id.nav_input_poi);

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
			navigation_input_poi.setVisibility(View.GONE);
			navigation_input_user.setVisibility(View.VISIBLE);
			pressedUserInput(true);
			pressedPoiInput(false);
			break;
		}
		case R.id.navigation_poi: {// 静态导航
			navigation_input_poi.setVisibility(View.VISIBLE);
			navigation_input_user.setVisibility(View.GONE);
			pressedUserInput(false);
			pressedPoiInput(true);
			break;
		}
		case R.id.navigation_search: {// 路线搜索
			NavigationActivity.this.finish();
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 动态导航界面切换
	 */
	private void pressedUserInput(boolean isPressed) {
		ImageView img = (ImageView) findViewById(R.id.navigation_user_img);
		TextView txt = (TextView) findViewById(R.id.navigation_user_txt);
		int normal_color = this.getResources().getColor(
				R.color.navigation_txt_normal_color);
		int pressed_color = this.getResources().getColor(
				R.color.navigation_txt_pressed_color);
		if (isPressed) {
			img.setBackgroundResource(R.drawable.navigation_user_pressed);
			txt.setTextColor(pressed_color);
		} else {

			img.setBackgroundResource(R.drawable.navigation_user_normal);
			txt.setTextColor(normal_color);
		}
	}

	/**
	 * 静态导航界面切换
	 */
	private void pressedPoiInput(boolean isPressed) {
		ImageView img = (ImageView) findViewById(R.id.navigation_poi_img);
		TextView txt = (TextView) findViewById(R.id.navigation_poi_txt);
		int normal_color = this.getResources().getColor(
				R.color.navigation_txt_normal_color);
		int pressed_color = this.getResources().getColor(
				R.color.navigation_txt_pressed_color);
		if (isPressed) {
			img.setBackgroundResource(R.drawable.navigation_building_pressed);
			txt.setTextColor(pressed_color);
		} else {

			img.setBackgroundResource(R.drawable.navigation_building_normal);
			txt.setTextColor(normal_color);
		}
	}
}
