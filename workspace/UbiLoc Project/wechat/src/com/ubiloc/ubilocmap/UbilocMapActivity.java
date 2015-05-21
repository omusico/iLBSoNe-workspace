package com.ubiloc.ubilocmap;

import java.util.ArrayList;
import java.util.List;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.core.GeoPoint;

import android.os.Bundle;

import com.donal.wechat.R;
import com.ubiloc.overlays.PointOverlay;

/**
 * 地图模块
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
		PointOverlay overlay = new PointOverlay();
		List<GeoPoint> coords = new ArrayList<GeoPoint>();
		coords.add(new GeoPoint(40.14607, 116.429838));
		coords.add(new GeoPoint(40.14609, 116.429839));
		coords.add(new GeoPoint(40.14620, 116.429845));
		overlay.setCoords(coords);
		UbilocMap.getInstance().addOverlay(overlay);

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
