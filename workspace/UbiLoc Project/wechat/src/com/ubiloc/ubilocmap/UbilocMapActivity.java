package com.ubiloc.ubilocmap;

import im.WeChat;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;
import org.mapsforge.core.model.GeoPoint;

import service.ConnectAndSendService;
import tools.SysApplication;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.donal.wechat.R;
import com.ubiloc.checkin.CheckinActivity;
import com.ubiloc.model.MovingObj;
import com.ubiloc.navigation.NavigationActivity;
import com.ubiloc.overlays.BitmapOverlay;
import com.ubiloc.overlays.BitmapOverlayItem;
import com.ubiloc.overlays.LineOverlay;
import com.ubiloc.pdr.OnNavigationListener;
import com.ubiloc.pdr.PdrManager;
import com.ubiloc.search.POIDataManager;
import com.ubiloc.search.POISearchActivity;
import com.ubiloc.search.PoiObject;
import com.ubiloc.simulate.SimulatedDataManager;
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
	private static ListView xlistView;
	private Thread myThread;
	private VerticalMenu verticalMenu;
	private EditText search_input;
	private View search_clear;
	private View map_poi_search;
	private View result_to_list;
	private static String userid;

	private static List<MovingObj> mlist;
	private long mDataVersion = 0;
	/**
	 * 初始化界面
	 */
	public static final int INIT_VIEW = 1;
	/**
	 * POI导航
	 */
	public static final int NAV_POI = 2;
	public static final String KEY = "key";
	public static final String OVERLAY_KEY_USER_LOCATION = "user_location";
	public static final String OVERLAY_KEY_END_POINT = "end_point";
	public static final String OVERLAY_KEY_ROUTE = "route";
	@SuppressLint("HandlerLeak")
	final private Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			switch (msg.what) {
			case INIT_VIEW: {
				UbilocMap.init(mMapView, UbilocMapActivity.this);
				initView();
				break;
			}
			case NAV_POI: {
				Bundle data = msg.getData();
				GeoPoint centerPoint = (GeoPoint) data.getSerializable(KEY);
				BitmapOverlay bitmapOverlay = new BitmapOverlay(
						UbilocMapActivity.this);
				List<BitmapOverlayItem> overlayItems = new ArrayList<BitmapOverlayItem>();
				overlayItems.add(new BitmapOverlayItem(UbilocMapActivity.this,
						centerPoint, R.drawable.user_location));
				bitmapOverlay.setBitmapOverlayItems(overlayItems);
				bitmapOverlay.setKey(OVERLAY_KEY_USER_LOCATION);
				UbilocMap.getInstance().addOverlay(bitmapOverlay);
				// UbilocMap.getInstance().setMapCenter(centerPoint);
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
		mlist = new ArrayList<MovingObj>();
		//FriendHeadList.initHeadList(xlistView, UbilocMapActivity.this,appContext);
		userid=WCApplication.getInstance().getLoginUid();

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

		// UbilocMap.init(mMapView, UbilocMapActivity.this);
		// initView();
	}

	/**
	 * 初始化界面
	 */
	private void initView() {

		// =====================================================================
		verticalMenu = (VerticalMenu) findViewById(R.id.vertical_menu);
		verticalMenu.setControlBackground(R.drawable.menu_control);
		initVerticalMenu();

		// =====================================================================
		search_input = (EditText) findViewById(R.id.search_input);
		search_clear = findViewById(R.id.search_clear);
		search_clear.setOnClickListener(new OnClickListener() {// 清除查询结果

					@Override
					public void onClick(View view) {
						search_input.setText("");
						UbilocMap.getInstance().removeAllOverlays();
						// 数据同步
						POIDataManager.getInstance().clearCurPois();
						mDataVersion = POIDataManager.getInstance()
								.getDataVersion();
					}
				});
		result_to_list = findViewById(R.id.result_to_list);
		result_to_list.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent poi_intent = new Intent(view.getContext(),
						POISearchActivity.class);
				view.getContext().startActivity(poi_intent);
			}
		});
		map_poi_search = findViewById(R.id.map_poi_search);
		map_poi_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				String input = search_input.getText().toString();
				List<PoiObject> poiObjects = POIDataManager.getInstance()
						.getPoiByKeyword_alpha(input);

				mDataVersion = POIDataManager.getInstance().getDataVersion();
				// 周,使用下面的方法测试
				// List<PoiObject> poiObjects = POIDataManager.getInstance()
				// .getPoiByKeyword(input);
				Toast.makeText(view.getContext(),
						"共搜索到" + poiObjects.size() + "个", Toast.LENGTH_LONG)
						.show();
				UbilocMap.getInstance().addPois(poiObjects);

			}
		});
	}

	/**
	 * 初始化菜单布局
	 */
	private void initVerticalMenu() {
		LayoutInflater inflater = LayoutInflater.from(this);
		// =================================================================================
		// 定位，pdr方法的入口
		final List<GeoPoint> coords = new ArrayList<GeoPoint>();

		View menu_item_locate = inflater.inflate(R.layout.menu_item_icon_text,
				verticalMenu, false);
		ImageView item_locate_img = (ImageView) menu_item_locate
				.findViewById(R.id.menu_item_img);
		item_locate_img.setBackgroundResource(R.drawable.menu_item_locate);
		TextView item_locate_txt = (TextView) menu_item_locate
				.findViewById(R.id.menu_item_text);
		item_locate_txt.setText(R.string.menu_item_locate);
		verticalMenu.addMenuItem(menu_item_locate);
		menu_item_locate.setOnClickListener(new OnClickListener() {

			// =================================================================================
			// 保存pdr方法计算出的位置
			@Override
			public void onClick(View view) {// 测试pdr方法

				PdrManager.init(view.getContext());

				// ConstConfig.sObjTask=new SendMovingObjTask();
				// new ConnectMsgTask().execute();
				PdrManager.getInstance().setOnNavigationListener(
						new OnNavigationListener() {

							@Override
							public void OnPositionChanged(double lat, double lon) {
								try {
									String[] s = new String[] {
											String.valueOf(lon),
											String.valueOf(lat) };
									Timestamp timestamp = new Timestamp(System.currentTimeMillis()); 
									MovingObj mObj=new MovingObj(userid, lon, lat, timestamp);
									//MovingObj mObj = new MovingObj(userid, lon,lat,timestamp);
									mlist.add(mObj);

									if (mlist.size() >= 5) {
										Intent mintent = new Intent(
												UbilocMapActivity.this,
												ConnectAndSendService.class);
										Bundle bundle = new Bundle();
										bundle.putSerializable("MovingObjMsg",
												(Serializable) mlist);
										mintent.putExtras(bundle);
										startService(mintent);
										mlist.clear();

									}
									// ConstConfig.sObjTask.execute(s);
									// 监听位置改变lon经度，lat纬度
									// userid=WCApplication.getInstance().getLoginUid();
									coords.add(new GeoPoint(lon, lat));
									// 清除所有图层
									UbilocMap.getInstance().removeAllOverlays();
									LineOverlay overlay = new LineOverlay();
									overlay.setCoords(coords);
									UbilocMap.getInstance().addOverlay(overlay);
									UbilocMap.getInstance().setMapCenter(
											new GeoPoint(lon, lat));
								} catch (Exception e) {
									Log.e("error_error", e.toString());
								}
							}
						});
				PdrManager.getInstance().startPDR();
			}
		});
		// =================================================================================
		// 导航功能
		View menu_item_navigation = inflater.inflate(
				R.layout.menu_item_icon_text, verticalMenu, false);
		ImageView item_navigation_img = (ImageView) menu_item_navigation
				.findViewById(R.id.menu_item_img);
		item_navigation_img
				.setBackgroundResource(R.drawable.menu_item_navigation);
		TextView item_navigation_txt = (TextView) menu_item_navigation
				.findViewById(R.id.menu_item_text);
		item_navigation_txt.setText(R.string.menu_item_navigation);
		menu_item_navigation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// Intent nav_intent = new Intent(view.getContext(),
				// NavigationActivity.class);
				// view.getContext().startActivity(nav_intent);
				List<GeoPoint> route1Coner = SimulatedDataManager.getInstance()
						.getRoute1Coner();
				LineOverlay route1Line = new LineOverlay();
				route1Line.setCoords(route1Coner);
				route1Line.setKey(OVERLAY_KEY_ROUTE);
				UbilocMap.getInstance().addOverlay(route1Line);
				GeoPoint endPoint = (GeoPoint) route1Coner.get(route1Coner
						.size() - 1);
				BitmapOverlay bitmapOverlay = new BitmapOverlay(
						UbilocMapActivity.this);
				List<BitmapOverlayItem> overlayItems = new ArrayList<BitmapOverlayItem>();
				overlayItems.add(new BitmapOverlayItem(UbilocMapActivity.this,
						endPoint, R.drawable.nav_end));
				bitmapOverlay.setBitmapOverlayItems(overlayItems);
				bitmapOverlay.setKey(OVERLAY_KEY_END_POINT);
				UbilocMap.getInstance().addOverlay(bitmapOverlay);

				new Thread(new Runnable() {
					private List<GeoPoint> route1 = SimulatedDataManager
							.getInstance().getRoute1();

					@Override
					public void run() {
						for (GeoPoint cur_location : route1) {
							Bundle data = new Bundle();
							data.putSerializable(NavigationActivity.KEY,
									cur_location);
							Message msg = new Message();
							msg.what = NAV_POI;
							msg.setData(data);
							handler.sendMessageDelayed(msg, 10);
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();

			}
		});
		verticalMenu.addMenuItem(menu_item_navigation);
		// =================================================================================
		// 好友位置显示
		View menu_item_friend = inflater.inflate(R.layout.menu_item_icon_text,
				verticalMenu, false);
		ImageView item_friend_img = (ImageView) menu_item_friend
				.findViewById(R.id.menu_item_img);
		item_friend_img.setBackgroundResource(R.drawable.menu_item_friend);
		TextView item_friend_txt = (TextView) menu_item_friend
				.findViewById(R.id.menu_item_text);
		item_friend_txt.setText(R.string.menu_item_friend);
		menu_item_friend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Toast.makeText(view.getContext(), "好友位置显示，该功能尚未实现",
						Toast.LENGTH_SHORT).show();
			}
		});
		verticalMenu.addMenuItem(menu_item_friend);
		// =================================================================================
		// 签到
		View menu_item_checkin = inflater.inflate(R.layout.menu_item_icon_text,
				verticalMenu, false);
		ImageView item_checkin_img = (ImageView) menu_item_checkin
				.findViewById(R.id.menu_item_img);
		item_checkin_img.setBackgroundResource(R.drawable.menu_item_checkin);
		TextView item_checkin_txt = (TextView) menu_item_checkin
				.findViewById(R.id.menu_item_text);
		item_checkin_txt.setText(R.string.menu_item_checkin);
		menu_item_checkin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent checkin_itent = new Intent(view.getContext(),
						CheckinActivity.class);
				view.getContext().startActivity(checkin_itent);
			}
		});
		verticalMenu.addMenuItem(menu_item_checkin);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		PdrManager.getInstance().stopPDR();
	}

	@Override
	protected void onResume() {
		super.onResume();
		synchronizeData();

	}

	/**
	 * 地图端同步数据
	 */
	private void synchronizeData() {
		if (mDataVersion != POIDataManager.getInstance().getDataVersion()) {// 同步数据
			List<PoiObject> poiObjects = POIDataManager.getInstance()
					.getCurPoi();
			mDataVersion = POIDataManager.getInstance().getDataVersion();
			UbilocMap.getInstance().addPois(poiObjects);
			mDataVersion = POIDataManager.getInstance().getDataVersion();
		}

	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(UbilocMapActivity.this).setTitle("确定退出吗?")
				.setNeutralButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SysApplication.getInstance().exit();
						Intent stopIntent=new Intent(UbilocMapActivity.this,
								ConnectAndSendService.class);
						stopService(stopIntent);
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
