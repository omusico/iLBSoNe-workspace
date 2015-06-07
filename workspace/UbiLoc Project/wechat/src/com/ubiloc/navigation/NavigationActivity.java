package com.ubiloc.search;

import android.app.Activity;
import android.os.Bundle;

import com.donal.wechat.R;

public class NavigationActivity extends Activity {

	public NavigationActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.navigation_main);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
