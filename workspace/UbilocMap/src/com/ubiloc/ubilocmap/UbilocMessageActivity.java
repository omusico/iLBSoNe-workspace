package com.ubiloc.ubilocmap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.ubilocmap.R;

public class UbilocMessageActivity extends Activity {
	List<Map<String, Object>> listitem;
	ListView listview;
	SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_activity);
		listitem = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 10; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", "姓名");
			map.put("msg", "[消息消息消息消息消息消息]");
			map.put("img", R.drawable.icon1);
			listitem.add(map);
		}

		listview = (ListView) findViewById(R.id.listview_msg);
		adapter = new SimpleAdapter(this, getData(),
				R.layout.listview_msg_item,
				new String[] { "name", "msg", "img" }, new int[] {
						R.id.lvmsg_name, R.id.lvmsg_msg, R.id.lvmsg_img });
		listview.setAdapter(adapter);

		listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view,
					final int index, long arg3) {

				AlertDialog.Builder builder = new Builder(
						UbilocMessageActivity.this.getWindow().getContext());
				builder.setTitle("这是一个提醒- -");
				builder.setMessage("确认要删除这条记录么？");
				builder.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						deleteData(index);
					}
				});
				builder.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});
				builder.create().show();

				return false;
			}

		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private List<Map<String, Object>> getData() {
		return listitem;
	}

	private void deleteData(int index) {
		listitem.remove(index);
		adapter.notifyDataSetChanged();
		listview.invalidate();
	}
}
