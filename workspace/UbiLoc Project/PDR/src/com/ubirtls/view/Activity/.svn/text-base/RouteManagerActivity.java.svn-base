/**
 * 
 */
package com.ubirtls.view.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ubirtls.Controller;
import com.ubirtls.R;
import com.ubirtls.R.string;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author
 * 
 */
public class RouteManagerActivity extends Activity {
	private ListView listView; // 整个界面是一个ListView控件
	private int[] images;// 图标ID数组
	private String[] titles;// 图标标题

	// 在Activity首次创建时调用
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.route);
		listView = (ListView) findViewById(R.id.route_manager_list);

		/* 初始化图标和字符串资源 */
		images = new int[] { R.drawable.route_manager_start_navi,
				R.drawable.route_manager_scan,
				R.drawable.route_manager_monitor_navi,
				R.drawable.route_manager_range,
				R.drawable.route_manager_delete_path,
				R.drawable.route_manager_delete_tril };
		titles = new String[] {
				getResources().getString(R.string.route_manager_start_navi),
				getResources().getString(R.string.route_manager_scan),
				getResources().getString(string.route_manager_monitor_navi),
				getResources().getString(R.string.route_manager_range),
				getResources().getString(R.string.route_manager_delete_path),
				getResources().getString(R.string.route_manager_delete_track) };
		/* 图标和字符串标题映射 */
		List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 6; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("PIC", images[i]);
			map.put("TITLE", titles[i]);
			contents.add(map);
		}
		/* 将映射添加到Adapter */
		SimpleAdapter adapter = new SimpleAdapter(this,
				(List<Map<String, Object>>) contents, R.layout.route_listitem,
				new String[] { "PIC", "TITLE" }, new int[] {
						R.id.route_listitem_img, R.id.route_listitem_text });
		listView.setAdapter(adapter);
		// 设置listView click监听
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1,
					int index, long arg3) {
				// TODO Auto-generated method stub
				// 清除浏览数据
				if (index == 5) {
					Dialog dialog = new AlertDialog.Builder(
							RouteManagerActivity.this)
							.setTitle(
									getResources().getString(
											R.string.route_clear_track_title))
							.setMessage(
									getResources().getString(
											R.string.route_clear_track_message))
							.setPositiveButton(
									getResources().getString(R.string.OK),
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface arg0, int arg1) {
											// TODO Auto-generated method stub
											Controller.getInstance()
													.clearTrack();
											RouteManagerActivity.this.finish();
										}
									}).setNegativeButton(
									getResources().getString(R.string.cancel),
									null).create();
					dialog.show();
				}
			}

		});
	}

	@Override
	public void onStart() {
		// 在Activity创建后或重启后调用
		super.onStart();
		this.setTitle(R.string.route_manager);
	}

	@Override
	protected void onPause() {// 在Activity暂停时调用
		super.onPause();
	}

	protected void onResume() {
		// onStart后会调用
		super.onResume();
	}

	protected void onDestroy() {// 在Activity 销毁时调用
		super.onDestroy();
	}

}
