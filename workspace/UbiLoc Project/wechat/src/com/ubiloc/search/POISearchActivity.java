package com.ubiloc.search;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.donal.wechat.R;
import com.ubiloc.ubilocmap.UbilocMap;

public class POISearchActivity extends Activity implements OnClickListener {
	private View back;
	private EditText search_input;
	private View search_clear;
	private View search;
	private ListView poi_search_result_list;
	private long mDataVersion = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poi_search);
		initView();
	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		back = findViewById(R.id.back);
		back.setOnClickListener(this);

		search_input = (EditText) findViewById(R.id.search_input);
		search_clear = findViewById(R.id.search_clear);
		search_clear.setOnClickListener(this);
		search = findViewById(R.id.list_poi_search);
		search.setOnClickListener(this);

		poi_search_result_list = (ListView) findViewById(R.id.poi_search_result_list);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back: {// 返回地图
			POISearchActivity.this.finish();
			break;
		}
		case R.id.search_clear: {// 清除输入内容
			search_input.setText("");
			poi_search_result_list.setAdapter(null);
			POIDataManager.getInstance().clearCurPois();// 同步数据
			break;
		}
		case R.id.list_poi_search: {// 搜索

			String input = search_input.getText().toString();
			final List<PoiObject> poiObjects = POIDataManager.getInstance()
					.getPoiByKeyword_alpha(input);
			Toast.makeText(view.getContext(), "共搜索到" + poiObjects.size() + "个",
					Toast.LENGTH_LONG).show();
			poi_search_result_list.setAdapter(getResultAdapter(poiObjects));

			break;
		}
		default: {

			break;
		}
		}
	}

	@Override
	protected void onResume() {
		synchronizeData();
		super.onResume();

	}

	private void synchronizeData() {
		if (mDataVersion != POIDataManager.getInstance().getDataVersion()) {// 同步数据
			final List<PoiObject> poiObjects = POIDataManager.getInstance()
					.getCurPoi();
			poi_search_result_list.setAdapter(getResultAdapter(poiObjects));
			mDataVersion = POIDataManager.getInstance().getDataVersion();
		}
	}

	/**
	 * 封装POI列表展示需要的Adapter
	 * 
	 * @param poiObjects
	 * @return
	 */
	private POIResultAdapter getResultAdapter(final List<PoiObject> poiObjects) {
		POIResultAdapter adapter = new POIResultAdapter(POISearchActivity.this,
				poiObjects);
		adapter.SetOnItemLocateListener(new OnPoiItemLoacteListener() {

			@Override
			public void OnClick(View view, int postion) {
				// 地图跳转
				UbilocMap.getInstance().setMapCenter(
						poiObjects.get(postion).getPoi_loc());
				UbilocMap.getInstance().setMapLevel(22);
				POISearchActivity.this.finish();
			}
		});

		return adapter;
	}
}
