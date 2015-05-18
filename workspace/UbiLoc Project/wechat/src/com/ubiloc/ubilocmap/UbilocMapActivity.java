package com.ubiloc.ubilocmap;

import java.util.ArrayList;
import java.util.List;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.mapgenerator.MapGenerator;
import org.mapsforge.core.GeoPoint;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.capricorn.ArcMenu;
import com.donal.wechat.R;
import com.ubiloc.layer.MapLayersManager;

import eu.geopaparazzi.mapsforge.mapsdirmanager.maps.tiles.GeopackageTileDownloader;
import eu.geopaparazzi.spatialite.database.spatial.SpatialDatabasesManager;
import eu.geopaparazzi.spatialite.database.spatial.core.tables.SpatialRasterTable;

/**
 * ��ͼģ��
 * 
 * @author crazy
 * @Date 2015-5-8
 */
public class UbilocMapActivity extends MapActivity {
	private static final int[] ITEM_DRAWABLES = { R.drawable.composer_camera,
			R.drawable.composer_music, R.drawable.composer_place,
			R.drawable.composer_sleep, R.drawable.composer_thought,
			R.drawable.composer_with };
	private MapView mMapView;
	private MapGenerator mapGenerator;
	private GeoPoint mapCenter;
	private ArrayOverlay overlays;
	private ArcMenu mArcMenu;
	private boolean isMapDataAvailable = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);
		int result = MapLayersManager.getInstance().initData();
		handleResult(result);

		mMapView = (MapView) findViewById(R.id.mapView);
		initMapComponent();

		mArcMenu = (ArcMenu) findViewById(R.id.arc_menu);
		initArcMenu(mArcMenu, ITEM_DRAWABLES);
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

	/**
	 * �жϵ�ͼ�Ƿ�������ʼ�������û�еĻ�������ʾ
	 * 
	 * @param result
	 * 
	 */
	private void handleResult(int result) {
		if (result == MapLayersManager.SUCCESS)
			isMapDataAvailable = true;
		else {
			isMapDataAvailable = false;
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle(R.string.no_map_data).setPositiveButton(
					R.string.ok, new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							dialog.dismiss();
						}

					});
			builder.setNegativeButton(R.string.cancel,
					new AlertDialog.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}
	}

	/**
	 * init the mapView and overlays
	 */
	private void initMapComponent() {
		overlays = new ArrayOverlay(this, mMapView);
		mMapView.getOverlays().clear();
		mMapView.getOverlays().add(overlays);

		mMapView.setVisibility(View.VISIBLE);
		mMapView.setClickable(true);
		mMapView.getMapZoomControls().setShowMapZoomControls(true);
		if (isMapDataAvailable) {
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
	}

	/**
	 * init arcmenu
	 */
	private void initArcMenu(ArcMenu menu, int[] itemDrawables) {
		final int itemCount = itemDrawables.length;
		for (int i = 0; i < itemCount; i++) {
			ImageView item = new ImageView(this);
			item.setImageResource(itemDrawables[i]);

			final int position = i;
			menu.addItem(item, new OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(UbilocMapActivity.this,
							"position:" + position, Toast.LENGTH_SHORT).show();
				}
			});
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
