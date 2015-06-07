package com.ubiloc.checkin;

import java.util.ArrayList;
import java.util.List;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.core.model.GeoPoint;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.donal.wechat.R;
import com.ubiloc.overlays.PointOverlay;

public class CheckinActivity extends MapActivity implements OnClickListener {
	private View back;
	private MapView mMapView;
	private View checkin_publish;
	private View checkin_locate;

	public CheckinActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.checkin_main);
		initView();

	}

	/**
	 * 初始化布局
	 */
	private void initView() {
		mMapView = (MapView) findViewById(R.id.checkin_mapview);
		CheckinMap.init(mMapView, CheckinActivity.this);
		back = findViewById(R.id.back);
		back.setOnClickListener(this);
		checkin_publish = findViewById(R.id.checkin_publish);
		checkin_publish.setOnClickListener(this);
		checkin_locate = findViewById(R.id.checkin_locate);
		checkin_locate.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.back: {
			CheckinActivity.this.finish();
			break;
		}
		case R.id.checkin_publish: {
			CheckinActivity.this.finish();
			break;
		}
		case R.id.checkin_locate: {
			CheckinMap.getInstance()
					.setMapCenter(new GeoPoint(-0.000487, 109.513775))
					.setMapLevel(21);
			PointOverlay overlay = new PointOverlay();
			List<GeoPoint> coords = new ArrayList<GeoPoint>();
			coords.add(new GeoPoint(-0.000487, 109.513775));
			overlay.setCoords(coords);
			CheckinMap.getInstance().addOverlay(overlay);
			break;
		}
		default:
			break;
		}
	}

}
