package ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import com.donal.wechat.R;
import com.ubiloc.geofencing.GeofenceMsg;

import config.GeofenceMsgManager;

public class GeoMsgListActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SimpleAdapter sAdapter=new SimpleAdapter(this, getGeoMsgData(), R.layout.geomsg, new String[]{"img","time","content"}, new int[]{R.id.msgimg,R.id.msgtime,R.id.msgcontent});
		setListAdapter(sAdapter);
	}

	private List<Map<String, Object>> getGeoMsgData() {
		Map<String, Object> map;
		GeofenceMsg gMsg;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<GeofenceMsg> geomsglist=GeofenceMsgManager.getInstance(this).getAllGeoMsgList();
		Iterator<GeofenceMsg> iterator=geomsglist.iterator();
		while(iterator.hasNext()){
			gMsg=iterator.next();
			map = new HashMap<String, Object>();
			map.put("img", R.drawable.geomsg);
			map.put("time", gMsg.getMsgtime());
			map.put("content", gMsg.getMsgcontent());
			list.add(map);
		}
		return list;
	}
}
