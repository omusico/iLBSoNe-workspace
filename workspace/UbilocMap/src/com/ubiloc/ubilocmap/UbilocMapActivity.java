package com.ubiloc.ubilocmap;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.mapgenerator.MapGenerator;
import org.mapsforge.core.GeoPoint;

import android.os.Bundle;
import android.view.View;

import com.example.ubilocmap.R;
import com.ubiloc.layer.MapLayersManager;

import eu.geopaparazzi.mapsforge.mapsdirmanager.maps.tiles.GeopackageTileDownloader;
import eu.geopaparazzi.spatialite.database.spatial.SpatialDatabasesManager;
import eu.geopaparazzi.spatialite.database.spatial.core.tables.SpatialRasterTable;

public class UbilocMapActivity extends MapActivity {
	private MapView mMapView;
	private MapGenerator mapGenerator;
	private GeoPoint mapCenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_fragment);
		MapLayersManager.getInstance().initData();
		mMapView = (MapView) findViewById(R.id.mapView);

		mMapView.getOverlays().clear();
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
