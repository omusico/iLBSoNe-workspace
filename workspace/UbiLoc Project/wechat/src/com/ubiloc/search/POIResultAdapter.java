package com.ubiloc.search;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.donal.wechat.R;

public class POIResultAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<PoiObject> mPois;
	private OnPoiItemLoacteListener listener;

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
		ImageView poi_img = (ImageView) poi_item.findViewById(R.id.poi_img);
		TextView poi_floor = (TextView) poi_item.findViewById(R.id.poi_floor);
		TextView poi_detial = (TextView) poi_item.findViewById(R.id.poi_detial);
		poi_title.setText(position + 1 + ".  " + cur_poi.getPoi_name());
		poi_floor.setText(cur_poi.getPoi_floor() + "楼");
		poi_detial.setText(cur_poi.getPoi_description());
		int draw_rs = PoiTools.getDrawableByClass(cur_poi.getPoi_class());
		poi_img.setBackgroundResource(draw_rs);
		View locate = poi_item.findViewById(R.id.locate);
		final int temp_position = position;
		locate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (listener != null) {
					listener.OnClick(view, temp_position);
				}
			}
		});
		return poi_item;
	}

	/**
	 * 定位的回调
	 * 
	 * @param listener
	 */
	public void SetOnItemLocateListener(OnPoiItemLoacteListener listener) {
		this.listener = listener;
	}
}
