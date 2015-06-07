package com.ubiloc.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Spinner;

import com.donal.wechat.R;

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
	private View navigation_poi;
	/**
	 * 路线查询
	 */
	private View navigation_search;

	private Spinner navigation_select_friend;

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
		navigation_poi = findViewById(R.id.navigation_poi);
		navigation_search = findViewById(R.id.navigation_search);
		back.setOnClickListener(this);
		navigation_user.setOnClickListener(this);
		navigation_poi.setOnClickListener(this);
		navigation_search.setOnClickListener(this);

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
			break;
		}
		default:
			break;
		}
	}

}
