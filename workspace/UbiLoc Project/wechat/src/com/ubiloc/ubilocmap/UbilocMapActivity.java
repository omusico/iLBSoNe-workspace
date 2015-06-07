package com.ubiloc.ubilocmap;

import im.WeChat;

import java.io.Serializable;
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
import com.ubiloc.overlays.LineOverlay;
import com.ubiloc.pdr.OnNavigationListener;
import com.ubiloc.pdr.PdrManager;
import com.ubiloc.search.POIDataManager;
import com.ubiloc.search.POISearchActivity;
import com.ubiloc.search.PoiObject;
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
	private View map_poi_search;
	private View result_to_list;
	// private static String userid;


	private static List<MovingObj> mlist;



	
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

		mlist = new ArrayList<MovingObj>();
		// userid=WCApplication.getInstance().getLoginUid();

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

		//UbilocMap.init(mMapView, UbilocMapActivity.this);
		//initView();
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
			public void onClick(View arg0) {
				String input = search_input.getText().toString();
				List<PoiObject> poiObjects = POIDataManager.getInstance()
						.getPoiByKeyword_alpha(
								POIDataManager.POI_KEW_WORDS_FIRE_HYDRAN);
				// 周,使用下面的方法测试
				// List<PoiObject> poiObjects = POIDataManager.getInstance()
				// .getPoiByKeyword(input);
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

									MovingObj mObj = new MovingObj("ww", lon,
											lat);
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
				Intent nav_intent = new Intent(view.getContext(),
						NavigationActivity.class);
				view.getContext().startActivity(nav_intent);
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
