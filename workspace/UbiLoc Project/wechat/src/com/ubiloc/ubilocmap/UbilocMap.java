package com.ubiloc.ubilocmap;

import java.util.List;

import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.mapgenerator.MapGenerator;
import org.mapsforge.core.model.GeoPoint;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.donal.wechat.R;
import com.ubiloc.component.ComponentUtil;
import com.ubiloc.layer.MapLayersManager;
import com.ubiloc.overlays.BaseOverlayItem;

import eu.geopaparazzi.mapsforge.mapsdirmanager.maps.tiles.GeopackageTileDownloader;
import eu.geopaparazzi.spatialite.database.spatial.SpatialDatabasesManager;
import eu.geopaparazzi.spatialite.database.spatial.core.tables.SpatialRasterTable;

/**
 * 地图操作类，将地图进行封装，此类采用单例模式，首先需要使用init进行初始化
 * 
 * @author crazy
 * @Date 2015-5-20
 */
public class UbilocMap {
	private Context context;
	private static UbilocMap mUbilocMap;
	private MapView mMapView;
	private MapGenerator mapGenerator;
	private GeoPoint mapCenter;
	private ArrayOverlay overlays;
	private boolean isMapDataAvailable = false;
	private int maxLevel = 20;
	private int minLevel = 20;
	private int defaultLevel = 20;

	private UbilocMap(MapView mapView, Context context) {
		this.mMapView = mapView;
		this.context = context;
		int result = MapLayersManager.getInstance().initData();
		handleResult(result);
		initMapComponent();
	}

	/**
	 * init UbilocMap
	 * 
	 * @param mapView
	 * @param context
	 * @return
	 */
	public static UbilocMap init(MapView mapView, Context context) {
		if (mUbilocMap == null) {
			mUbilocMap = new UbilocMap(mapView, context);

		}
		return mUbilocMap;
	}

	/**
	 * get the handle of UbilocMap
	 * 
	 * @return
	 */
	public static UbilocMap getInstance() {
		return mUbilocMap;
	}

	public MapView getMapView() {
		return mMapView;
	}

	/**
	 * 判断地图数据是否可用
	 * 
	 * @param result
	 * 
	 */
	private void handleResult(int result) {
		if (result == MapLayersManager.SUCCESS)
			isMapDataAvailable = true;
		else {
			isMapDataAvailable = false;
			AlertDialog.Builder builder = new Builder(context);
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
		overlays = new ArrayOverlay(context, mMapView);
		mMapView.getOverlays().clear();
		mMapView.getOverlays().add(overlays);

		mMapView.setVisibility(View.VISIBLE);
		mMapView.setClickable(true);
		mMapView.getMapZoomControls().setShowMapZoomControls(true);
		if (isMapDataAvailable) {
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ ComponentUtil.MAP_PATH
					+ "testmap2.mbtiles";
			SpatialRasterTable rasterTable = null;
			String tableName = "abc";
			try {
				rasterTable = SpatialDatabasesManager.getInstance()
						.getRasterTableByName(path, tableName);
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

		// 没有办法，只能先使用业务图层当做底图使用了，测试版：
		// =========================================================================================
		// String absPath = "/ubiloc/map/gujia.sqlite";
		// overlays.addBaseOverlay(absPath);
		// overlays.requestRedraw();
		// =========================================================================================
	}

	/**
	 * add an overlay
	 * 
	 * @return
	 */
	public UbilocMap addOverlay(BaseOverlayItem overlayItem) {
		overlays.addOverlayItem(overlayItem);
		overlays.requestRedraw();
		return this;
	}

	/**
	 * add overlays
	 * 
	 * @return
	 */
	public UbilocMap addOverlays(List<BaseOverlayItem> overlayItems) {
		for (BaseOverlayItem overlayItem : overlayItems) {
			overlays.addOverlayItem(overlayItem);
		}
		overlays.requestRedraw();
		return this;
	}

	/**
	 * remove an overlay
	 * 
	 * @return
	 */
	public UbilocMap removeOverlay(BaseOverlayItem overlayItem) {
		overlays.removeOverlayItem(overlayItem);
		overlays.requestRedraw();
		return this;
	}

	/**
	 * 　remove all overlays
	 * 
	 * @return
	 */
	public UbilocMap removeAllOverlays() {
		overlays.removeAllOverlayItems();
		overlays.requestRedraw();
		return this;
	}

	/**
	 * set the center of mapview
	 * 
	 * @param center
	 * @return
	 */
	public UbilocMap setMapCenter(GeoPoint center) {
		mMapView.getController().setCenter(center);
		return this;
	}

	/**
	 * set the level of mapview
	 * 
	 * @param level
	 * @return
	 */
	public UbilocMap setMapLevel(int level) {
		if (level <= maxLevel && level >= minLevel) {
			mMapView.getController().setZoom(level);
		} else {
			Toast.makeText(null, R.string.map_level_beyond_constraints,
					Toast.LENGTH_SHORT).show();
		}
		return this;
	}

	/**
	 * reset the overlay,mapcenter,maplevel
	 * 
	 * @return
	 */
	public UbilocMap resetMapview() {
		overlays = new ArrayOverlay(context, mMapView);
		mMapView.getOverlays().clear();
		mMapView.getOverlays().add(overlays);

		mMapView.getController().setCenter(mapCenter);

		mMapView.getController().setZoom(defaultLevel);
		return this;
	}

}
