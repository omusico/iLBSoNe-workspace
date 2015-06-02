package com.ubiloc.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.donal.wechat.R;

public class POIResultAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;

	public POIResultAdapter(Context context) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return 20;
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		View poi_item;
		if (view != null)
			poi_item = view;
		else
			poi_item = mInflater.inflate(R.layout.poi_search_result_item,
					viewGroup, false);
		TextView poi_title = (TextView) poi_item.findViewById(R.id.poi_title);
		TextView poi_floor = (TextView) poi_item.findViewById(R.id.poi_floor);
		TextView poi_detial = (TextView) poi_item.findViewById(R.id.poi_detial);
		poi_title.setText(position + 1 + ".  " + "消防栓-" + position);
		poi_floor.setText(position / 4 + "楼");
		poi_detial.setText("这是个消防栓");
		return poi_item;
	}
}
