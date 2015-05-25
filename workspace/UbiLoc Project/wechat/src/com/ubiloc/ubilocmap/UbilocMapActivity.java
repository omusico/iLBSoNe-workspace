package com.ubiloc.ubilocmap;

import im.WeChat;

import java.util.ArrayList;
import java.util.List;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.core.GeoPoint;

import tools.SysApplication;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.donal.wechat.R;
import com.ubiloc.overlays.BitmapOverlay;
import com.ubiloc.overlays.BitmapOverlayItem;
import com.ubiloc.overlays.LineOverlay;
import com.ubiloc.overlays.PointOverlay;
import com.ubiloc.overlays.PolygonOverlay;
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
		myThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					handler.sendEmptyMessage(1);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}

			}
		});
		myThread.start();
		FriendHeadList.initHeadList(xlistView, UbilocMapActivity.this,
				appContext);
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
						UbilocMapActivity.this, new GeoPoint(40.3066720566464,
								116.75557503421),
						R.drawable.draw_bitmap_test_img1);
				overlayItems.add(overlayItem1);
				BitmapOverlayItem overlayItem2 = new BitmapOverlayItem(
						UbilocMapActivity.this, new GeoPoint(40.3064612343892,
								116.755124502335),
						R.drawable.draw_bitmap_test_img3);
				overlayItems.add(overlayItem2);
				BitmapOverlayItem overlayItem3 = new BitmapOverlayItem(
						UbilocMapActivity.this, new GeoPoint(40.4055593616439,
								116.766092650115),
						R.drawable.draw_bitmap_default);
				overlayItems.add(overlayItem3);
				BitmapOverlayItem overlayItem4 = new BitmapOverlayItem(
						UbilocMapActivity.this, new GeoPoint(40.405610614,
								116.777595110572),
						R.drawable.draw_bitmap_test_img2);
				overlayItems.add(overlayItem4);
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
			}
		});
		verticalMenu.addMenuItem(item5);
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
