package com.ubiloc.checkin;

import java.util.ArrayList;
import java.util.List;

import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.mapgenerator.MapGenerator;
import org.mapsforge.core.model.GeoPoint;

import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.donal.wechat.R;
import com.ubiloc.component.ComponentUtil;
import com.ubiloc.overlays.BaseOverlayItem;
import com.ubiloc.overlays.BitmapOverlay;
import com.ubiloc.overlays.BitmapOverlayItem;
import com.ubiloc.search.PoiObject;
import com.ubiloc.search.PoiTools;
import com.ubiloc.ubilocmap.ArrayOverlay;

import eu.geopaparazzi.mapsforge.mapsdirmanager.maps.tiles.GeopackageTileDownloader;
import eu.geopaparazzi.spatialite.database.spatial.SpatialDatabasesManager;
import eu.geopaparazzi.spatialite.database.spatial.core.tables.SpatialRasterTable;

public class CheckinMap {
	private Context mContext;
	private static CheckinMap mCheckinMap;
	private MapView mMapView;
	private MapGenerator mapGenerator;
	private GeoPoint mapCenter;
	private ArrayOverlay overlays;
	private boolean isMapDataAvailable = true;
	private int maxLevel = 22;
	private int minLevel = 20;
	private int defaultLevel = 20;

	private CheckinMap(MapView mapView, Context context) {
		this.mMapView = mapView;
		this.mContext = context;
		initMapComponent();
	}

	/**
	 * 初始化
	 * 
	 * @param mapView
	 * @param context
	 */
	public static void init(MapView mapView, Context context) {
		mCheckinMap = new CheckinMap(mapView, context);
	}

	public static CheckinMap getInstance() {
		return mCheckinMap;
	}

	/**
	 * init the mapView and overlays
	 */
	private void initMapComponent() {
		overlays = new ArrayOverlay(mContext, mMapView);
		mMapView.getOverlays().clear();
		mMapView.getOverlays().add(overlays);

		mMapView.setVisibility(View.VISIBLE);
		mMapView.setClickable(true);
		mMapView.getMapZoomControls().setShowMapZoomControls(true);
		if (isMapDataAvailable) {
			String path = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ ComponentUtil.MAP_PATH
					+ "floorNew_4_20150606.mbtiles";
			// + "floor_4_20150606.mbtiles";
			// + "floor_4.mbtiles";
			// + "floor_4_20150531.mbtiles";
			SpatialRasterTable rasterTable = null;
			String tableName = "iMB";
			try {
				rasterTable = SpatialDatabasesManager.getInstance()
						.getRasterTableByName(path, tableName);
				mapGenerator = new GeopackageTileDownloader(rasterTable);
				if (mapGenerator != null) {
					mMapView.setMapGenerator(mapGenerator);
					mapCenter = mapGenerator.getStartPoint();
					int minLevel = mapGenerator.getStartZoomLevel();
					mMapView.getController().setZoom(20);
					mMapView.getController().setCenter(
							new GeoPoint(-0.000487, 109.513775));

				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public MapView getMapView() {
		return mMapView;
	}

	/**
	 * add an overlay
	 * 
	 * @return
	 */
	public CheckinMap addOverlay(BaseOverlayItem overlayItem) {
		overlays.addOverlayItem(overlayItem);
		overlays.requestRedraw();
		return this;
	}

	/**
	 * add overlays
	 * 
	 * @return
	 */
	public CheckinMap addOverlays(List<BaseOverlayItem> overlayItems) {
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
	public CheckinMap removeOverlay(BaseOverlayItem overlayItem) {
		overlays.removeOverlayItem(overlayItem);
		overlays.requestRedraw();
		return this;
	}

	/**
	 * 　remove all overlays
	 * 
	 * @return
	 */
	public CheckinMap removeAllOverlays() {
		overlays.removeAllOverlayItems();
		overlays.requestRedraw();
		mMapView.getOverlays().clear();
		overlays = new ArrayOverlay(mContext, mMapView);
		mMapView.getOverlays().add(overlays);
		return this;
	}

	/**
	 * set the center of mapview
	 * 
	 * @param center
	 * @return
	 */
	public CheckinMap setMapCenter(GeoPoint center) {
		mMapView.getController().setCenter(center);
		return this;
	}

	/**
	 * set the level of mapview
	 * 
	 * @param level
	 * @return
	 */
	public CheckinMap setMapLevel(int level) {
		if (level <= maxLevel && level >= minLevel) {
			mMapView.getController().setZoom(level);
		} else {
			Toast.makeText(mContext, R.string.map_level_beyond_constraints,
					Toast.LENGTH_SHORT).show();
		}
		return this;
	}

	/**
	 * reset the overlay,mapcenter,maplevel
	 * 
	 * @return
	 */
	public CheckinMap resetMapview() {
		overlays = new ArrayOverlay(mContext, mMapView);
		mMapView.getOverlays().clear();
		mMapView.getOverlays().add(overlays);

		mMapView.getController().setCenter(mapCenter);

		mMapView.getController().setZoom(defaultLevel);
		return this;
	}

	public void addPois(List<PoiObject> pois) {

		// 画图,使用测试数据
		BitmapOverlay overlay = new BitmapOverlay(mContext);
		List<BitmapOverlayItem> overlayItems = new ArrayList<BitmapOverlayItem>();
		for (PoiObject poi : pois) {
			int draw_rs = PoiTools.getDrawableByClass(poi.getPoi_class());
			BitmapOverlayItem overlayItem1 = new BitmapOverlayItem(mContext,
					poi.getPoi_loc(), draw_rs);
			overlayItems.add(overlayItem1);
			overlay.setBitmapOverlayItems(overlayItems);
		}
		addOverlay(overlay);

	}
}
