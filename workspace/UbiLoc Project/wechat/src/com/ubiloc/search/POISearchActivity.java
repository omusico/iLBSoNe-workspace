package com.ubiloc.search;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.donal.wechat.R;

public class POISearchActivity extends Activity {
	private View back;
	private EditText search_input;
	private View search;
	private ListView poi_search_result_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poi_search);
		back = findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				POISearchActivity.this.finish();
			}
		});

		search_input = (EditText) findViewById(R.id.search_input);
		search = findViewById(R.id.list_poi_search);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				String input = search_input.getText().toString();
				List<PoiObject> poiObjects = POIDataManager.getInstance()
						.getPoiByKeyword_alpha(input);
				POIResultAdapter adapter = new POIResultAdapter(
						POISearchActivity.this, poiObjects);
				poi_search_result_list.setAdapter(adapter);
			}
		});

		poi_search_result_list = (ListView) findViewById(R.id.poi_search_result_list);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
