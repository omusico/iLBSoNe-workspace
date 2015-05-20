package com.ubiloc.ubilocmap;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;

import android.os.Bundle;

import com.donal.wechat.R;

/**
 * ��ͼģ��
 * 
 * @author crazy
 * @Date 2015-5-8
 */
public class UbilocMapActivity extends MapActivity {
	private MapView mMapView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);
		mMapView = (MapView) findViewById(R.id.mapView);
		UbilocMap.init(mMapView, this);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
