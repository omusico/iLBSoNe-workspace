package com.ubiloc.navigation;

import android.view.View;

/**
 * 监听用户的输入
 * 
 * @author crazy
 * @Date 2015-6-6
 */
public interface OnUserInputListener {
	/**
	 * 用户输入
	 * 
	 * @param view
	 * @param input
	 */
	public void OnInput(View view, String input);
}
