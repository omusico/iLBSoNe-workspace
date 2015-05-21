package com.ubiloc.ubilocmap;

import java.util.ArrayList;
import java.util.List;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.core.GeoPoint;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.donal.wechat.R;
import com.ubiloc.overlays.LineOverlay;
import com.ubiloc.overlays.PointOverlay;
import com.ubiloc.overlays.PolygonOverlay;

/**
 * 地图模块
 * 
 * @author crazy
 * @Date 2015-5-8
 */
public class UbilocMapActivity extends MapActivity implements OnClickListener {
	private MapView mMapView;

	private View draw_point;
	private View draw_line;
	private View draw_polygon;
	private View draw_clear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);
		mMapView = (MapView) findViewById(R.id.mapView);
		UbilocMap.init(mMapView, this);
		initView();

	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		draw_point = findViewById(R.id.draw_point);
		draw_line = findViewById(R.id.draw_line);
		draw_polygon = findViewById(R.id.draw_polygon);
		draw_clear = findViewById(R.id.draw_clear);
		draw_point.setOnClickListener(this);
		draw_line.setOnClickListener(this);
		draw_polygon.setOnClickListener(this);
		draw_clear.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.draw_point: {// 画点,使用测试数据
			PointOverlay overlay = new PointOverlay();
			List<GeoPoint> coords = new ArrayList<GeoPoint>();
			coords.add(new GeoPoint(40.3066720566464, 116.75557503421));
			coords.add(new GeoPoint(40.3064612343892, 116.755124502335));
			coords.add(new GeoPoint(40.4055593616439, 116.766092650115));
			coords.add(new GeoPoint(40.405610614, 116.777595110572));
			overlay.setCoords(coords);
			UbilocMap.getInstance().addOverlay(overlay);

			break;
		}
		case R.id.draw_line: {// 画线，使用测试数据
			LineOverlay overlay = new LineOverlay();
			List<GeoPoint> coords = new ArrayList<GeoPoint>();
			coords.add(new GeoPoint(40.3066720566464, 116.75557503421));
			coords.add(new GeoPoint(40.3064612343892, 116.755124502335));
			coords.add(new GeoPoint(40.4055593616439, 116.766092650115));
			coords.add(new GeoPoint(40.405610614, 116.777595110572));
			overlay.setCoords(coords);
			UbilocMap.getInstance().addOverlay(overlay);
			break;
		}
		case R.id.draw_polygon: {// 画面,使用测试数据
			PolygonOverlay overlay = new PolygonOverlay();
			List<GeoPoint> coords = new ArrayList<GeoPoint>();
			coords.add(new GeoPoint(40.3066720566464, 116.75557503421));
			coords.add(new GeoPoint(40.3064612343892, 116.755124502335));
			coords.add(new GeoPoint(40.4055593616439, 116.766092650115));
			coords.add(new GeoPoint(40.405610614, 116.777595110572));
			overlay.setCoords(coords);
			UbilocMap.getInstance().addOverlay(overlay);
			break;
		}
		case R.id.draw_clear: {
			UbilocMap.getInstance().removeAllOverlays();
			break;
		}
		default: {
			break;
		}
		}
	}
}
