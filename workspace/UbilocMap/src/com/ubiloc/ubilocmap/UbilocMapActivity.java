package com.ubiloc.ubilocmap;

import java.util.ArrayList;
import java.util.List;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.mapgenerator.MapGenerator;
import org.mapsforge.core.GeoPoint;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.ubilocmap.R;
import com.ubiloc.layer.MapLayersManager;

import eu.geopaparazzi.mapsforge.mapsdirmanager.maps.tiles.GeopackageTileDownloader;
import eu.geopaparazzi.spatialite.database.spatial.SpatialDatabasesManager;
import eu.geopaparazzi.spatialite.database.spatial.core.tables.SpatialRasterTable;

public class UbilocMapActivity extends MapActivity {
	private MapView mMapView;
	private MapGenerator mapGenerator;
	private GeoPoint mapCenter;
	private ArrayOverlay overlays;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_fragment);
		MapLayersManager.getInstance().initData();
		mMapView = (MapView) findViewById(R.id.mapView);

		overlays = new ArrayOverlay(this, mMapView);
		mMapView.getOverlays().clear();
		mMapView.getOverlays().add(overlays);

		mMapView.setVisibility(View.VISIBLE);
		mMapView.setClickable(true);
		mMapView.getMapZoomControls().setShowMapZoomControls(true);
		SpatialRasterTable rasterTable = null;
		String tableName = "dianzi";
		try {
			rasterTable = SpatialDatabasesManager.getInstance()
					.getRasterTableByName(tableName);
			mapGenerator = new GeopackageTileDownloader(rasterTable);
			if (mapGenerator != null) {
				mMapView.setMapGenerator(mapGenerator);
				mapCenter = mapGenerator.getStartPoint();
				int minLevel = mapGenerator.getStartZoomLevel();
				mMapView.getController().setZoom(minLevel);
				mMapView.getController().setCenter(mapCenter);

			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		View test = findViewById(R.id.test);
		test.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				List<GeoPoint> coords = new ArrayList<GeoPoint>();
				double lon = 116.429838;
				double lat = 40.14607;
				GeoPoint temp = new GeoPoint(lat, lon);
				coords.add(temp);
				overlays.setCoords(coords);
				overlays.requestRedraw();
			}
		});

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
