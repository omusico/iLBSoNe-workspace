package com.ubiloc.search;

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
		search = findViewById(R.id.search);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				String data = search_input.getText().toString();
				POIDataManager.getInstance().setData(data);
			}
		});

		poi_search_result_list = (ListView) findViewById(R.id.poi_search_result_list);
		POIResultAdapter adapter = new POIResultAdapter(this, null);
		poi_search_result_list.setAdapter(adapter);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
