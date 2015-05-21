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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.donal.wechat.R;
import com.ubiloc.overlays.LineOverlay;
import com.ubiloc.overlays.PointOverlay;
import com.ubiloc.overlays.PolygonOverlay;

import config.WCApplication;

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
	protected WCApplication appContext;
	private ListView xlistView;
	private Thread myThread;
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
