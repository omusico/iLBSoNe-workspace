package ui.adapter;

import im.model.HistoryChatBean;

import java.util.List;

import com.ubiloc.geofencing.GeofenceMsg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GeoMsgAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private List<GeofenceMsg> geoMsgList;
	private Context context;
	
	public GeoMsgAdapter(Context context,List<GeofenceMsg> geoMsgList) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.geoMsgList=geoMsgList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return geoMsgList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return geoMsgList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
