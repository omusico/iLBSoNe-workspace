package com.ubiloc.navigation;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class NavigationFriendComponent {
	private View mRootView;
	/**
	 * 监听用户的输入
	 */
	private OnUserInputListener mInputListener;
	private EditText start_location;
	private Spinner end_location;

	public NavigationFriendComponent(View rootView) {
		this.mRootView = rootView;
		initView();
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		if (mRootView != null) {
			// start_location = mRootView.findViewById(R.id.)
		}
	}

}
