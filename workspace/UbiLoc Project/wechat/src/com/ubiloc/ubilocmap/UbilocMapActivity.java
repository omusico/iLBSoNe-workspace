package com.ubiloc.ubilocmap;

import im.WeChat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.core.model.GeoPoint;

import tools.SysApplication;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.donal.wechat.R;
import com.ubiloc.navigation.OnNavigationListener;
import com.ubiloc.navigation.PdrManager;
import com.ubiloc.overlays.BitmapOverlay;
import com.ubiloc.overlays.BitmapOverlayItem;
import com.ubiloc.overlays.LineOverlay;
import com.ubiloc.overlays.PointOverlay;
import com.ubiloc.overlays.PolygonOverlay;
import com.ubiloc.search.POIDataManager;
import com.ubiloc.search.POISearchActivity;
import com.verticalmenu.VerticalMenu;

import config.WCApplication;

/**
 * 地图模块
 * 
 * @author crazy
 * @Date 2015-5-8
 */
public class UbilocMapActivity extends MapActivity {
	private MapView mMapView;

	protected WCApplication appContext;
	private ListView xlistView;
	private Thread myThread;
	private VerticalMenu verticalMenu;
	private EditText search_input;
	@SuppressLint("HandlerLeak")
	final private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: {
				UbilocMap.init(mMapView, UbilocMapActivity.this);
				initView();
				break;
			}
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appContext = WeChat.returnAppContex();
		setContentView(R.layout.map_activity);
		SysApplication.getInstance().addActivity(this);
		mMapView = (MapView) findViewById(R.id.mapView);
		xlistView = (ListView) findViewById(R.id.xmaplist);
		// myThread = new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// try {
		// Thread.sleep(2000);
		// handler.sendEmptyMessage(1);
		// } catch (InterruptedException e) {
		//
		// e.printStackTrace();
		// }
		//
		// }
		// });
		// myThread.start();
		// FriendHeadList.initHeadList(xlistView, UbilocMapActivity.this,
		// appContext);

		UbilocMap.init(mMapView, UbilocMapActivity.this);
		initView();
	}

	/**
	 * 初始化界面
	 */
	private void initView() {

		verticalMenu = (VerticalMenu) findViewById(R.id.vertical_menu);
		verticalMenu.setControlBackground(R.drawable.menu_control);
		LayoutInflater inflater = LayoutInflater.from(this);
		View item1 = inflater.inflate(R.layout.menu_item, null);
		ImageView item_img1 = (ImageView) item1
				.findViewById(R.id.menu_item_img);
		item_img1.setBackgroundResource(R.drawable.draw_point);
		verticalMenu.addMenuItem(item1);
		item1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 画点,使用测试数据
				PointOverlay overlay = new PointOverlay();
				List<GeoPoint> coords = new ArrayList<GeoPoint>();
				coords.add(new GeoPoint(40.3066720566464, 116.75557503421));
				coords.add(new GeoPoint(40.3064612343892, 116.755124502335));
				coords.add(new GeoPoint(40.4055593616439, 116.766092650115));
				coords.add(new GeoPoint(40.405610614, 116.777595110572));
				overlay.setCoords(coords);
				UbilocMap.getInstance().addOverlay(overlay);
			}
		});

		View item2 = inflater.inflate(R.layout.menu_item, null);
		ImageView item_img2 = (ImageView) item2
				.findViewById(R.id.menu_item_img);
		item_img2.setBackgroundResource(R.drawable.draw_line);
		verticalMenu.addMenuItem(item2);
		item2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 画线，使用测试数据
				LineOverlay overlay = new LineOverlay();
				List<GeoPoint> coords = new ArrayList<GeoPoint>();
				coords.add(new GeoPoint(40.3066720566464, 116.75557503421));
				coords.add(new GeoPoint(40.3064612343892, 116.755124502335));
				coords.add(new GeoPoint(40.4055593616439, 116.766092650115));
				coords.add(new GeoPoint(40.405610614, 116.777595110572));
				overlay.setCoords(coords);
				UbilocMap.getInstance().addOverlay(overlay);
			}
		});

		View item3 = inflater.inflate(R.layout.menu_item, null);
		ImageView item_img3 = (ImageView) item3
				.findViewById(R.id.menu_item_img);
		item_img3.setBackgroundResource(R.drawable.draw_surface);
		verticalMenu.addMenuItem(item3);
		item3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 画面,使用测试数据
				PolygonOverlay overlay = new PolygonOverlay();
				List<GeoPoint> coords = new ArrayList<GeoPoint>();
				coords.add(new GeoPoint(40.3066720566464, 116.75557503421));
				coords.add(new GeoPoint(40.3064612343892, 116.755124502335));
				coords.add(new GeoPoint(40.4055593616439, 116.766092650115));
				coords.add(new GeoPoint(40.405610614, 116.777595110572));
				overlay.setCoords(coords);
				UbilocMap.getInstance().addOverlay(overlay);
			}
		});

		View item4 = inflater.inflate(R.layout.menu_item, null);
		ImageView item_img4 = (ImageView) item4
				.findViewById(R.id.menu_item_img);
		item_img4.setBackgroundResource(R.drawable.draw_bitmap);
		item4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 画图,使用测试数据
				BitmapOverlay overlay = new BitmapOverlay(
						UbilocMapActivity.this);
				List<BitmapOverlayItem> overlayItems = new ArrayList<BitmapOverlayItem>();
				BitmapOverlayItem overlayItem1 = new BitmapOverlayItem(
						UbilocMapActivity.this, new GeoPoint(-0.000487,
								109.513775),
						R.drawable.draw_bitmap_fire_hydrant);
				overlayItems.add(overlayItem1);
				// BitmapOverlayItem overlayItem2 = new BitmapOverlayItem(
				// UbilocMapActivity.this, new GeoPoint(40.3064612343892,
				// 116.755124502335),
				// R.drawable.draw_bitmap_test_img3);
				// overlayItems.add(overlayItem2);
				// BitmapOverlayItem overlayItem3 = new BitmapOverlayItem(
				// UbilocMapActivity.this, new GeoPoint(40.4055593616439,
				// 116.766092650115),
				// R.drawable.draw_bitmap_default);
				// overlayItems.add(overlayItem3);
				// BitmapOverlayItem overlayItem4 = new BitmapOverlayItem(
				// UbilocMapActivity.this, new GeoPoint(40.405610614,
				// 116.777595110572),
				// R.drawable.draw_bitmap_test_img2);
				// overlayItems.add(overlayItem4);
				overlay.setBitmapOverlayItems(overlayItems);
				UbilocMap.getInstance().addOverlay(overlay);
			}
		});
		verticalMenu.addMenuItem(item4);

		View item5 = inflater.inflate(R.layout.menu_item, null);
		ImageView item_img5 = (ImageView) item5
				.findViewById(R.id.menu_item_img);
		item_img5.setBackgroundResource(R.drawable.draw_clear);
		item5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 清除所有图层
				UbilocMap.getInstance().removeAllOverlays();

				// 画点,使用测试数据
				PointOverlay overlay = new PointOverlay();
				List<GeoPoint> coords = new ArrayList<GeoPoint>();
				coords.add(new GeoPoint(-0.000487, 109.513775));
				overlay.setCoords(coords);
				UbilocMap.getInstance().addOverlay(overlay);
				UbilocMap.getInstance().setMapCenter(
						new GeoPoint(-0.000487, 109.513775));
			}
		});
		verticalMenu.addMenuItem(item5);

		final List<GeoPoint> coords = new ArrayList<GeoPoint>();

		View item6 = inflater.inflate(R.layout.menu_item, null);
		ImageView item_img6 = (ImageView) item6
				.findViewById(R.id.menu_item_img);
		item_img6.setBackgroundResource(R.drawable.draw_navigation);
		item6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {// 测试pdr方法

				PdrManager.init(view.getContext());
				PdrManager.getInstance().setOnNavigationListener(
						new OnNavigationListener() {

							@Override
							public void OnPositionChanged(double lat, double lon) {
								try {
									coords.add(new GeoPoint(lon, lat));
									// 清除所有图层
									UbilocMap.getInstance().removeAllOverlays();
									LineOverlay overlay = new LineOverlay();
									overlay.setCoords(coords);
									UbilocMap.getInstance().addOverlay(overlay);
									UbilocMap.getInstance().setMapCenter(
											new GeoPoint(lon, lat));
								} catch (Exception e) {

								}
							}
						});
				PdrManager.getInstance().startPDR();
			}
		});
		verticalMenu.addMenuItem(item6);

		View item7 = inflater.inflate(R.layout.menu_item, null);
		ImageView item_img7 = (ImageView) item7
				.findViewById(R.id.menu_item_img);
		item_img7.setBackgroundResource(R.drawable.draw_save);
		item7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// 保存文件
				String path = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/ubiloc/wgs84.txt";
				File file = new File(path);
				try {
					BufferedWriter writer = new BufferedWriter(new FileWriter(
							file));
					for (GeoPoint coord : coords) {
						writer.write(coord.getLongitude() + " "
								+ coord.getLatitude() + "\n");
					}
					writer.close();

				} catch (Exception e) {

				}
			}
		});
		verticalMenu.addMenuItem(item7);

		search_input = (EditText) findViewById(R.id.search_input);
		search_input.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					Intent poi_intent = new Intent(view.getContext(),
							POISearchActivity.class);
					view.getContext().startActivity(poi_intent);
				}
				return false;
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		PdrManager.getInstance().stopPDR();
	}

	@Override
	protected void onResume() {
		super.onResume();
		String data = POIDataManager.getInstance().getData();
		Toast.makeText(this, data, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(UbilocMapActivity.this).setTitle("确定退出吗?")
				.setNeutralButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SysApplication.getInstance().exit();
						appContext.exit();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).show();
	}

}
