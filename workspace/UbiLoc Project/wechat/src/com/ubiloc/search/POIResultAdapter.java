package com.ubiloc.search;

import java.util.List;

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
	private List<PoiObject> mPois;

	public POIResultAdapter(Context context, List<PoiObject> pois) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		this.mPois = pois;
	}

	@Override
	public int getCount() {
		if (mPois != null)
			return mPois.size();
		else
			return 0;
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
		PoiObject cur_poi = mPois.get(position);
		if (view != null)
			poi_item = view;
		else
			poi_item = mInflater.inflate(R.layout.poi_search_result_item,
					viewGroup, false);
		TextView poi_title = (TextView) poi_item.findViewById(R.id.poi_title);
		TextView poi_floor = (TextView) poi_item.findViewById(R.id.poi_floor);
		TextView poi_detial = (TextView) poi_item.findViewById(R.id.poi_detial);
		poi_title.setText(position + 1 + ".  " + cur_poi.getPoi_name());
		poi_floor.setText(cur_poi.getPoi_floor() + "楼");
		poi_detial.setText(cur_poi.getPoi_description());
		return poi_item;
	}
}
