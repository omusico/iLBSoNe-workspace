package com.ubiloc.ubilocmap;

import java.io.IOException;

import im.WeChat;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;

import tools.SysApplication;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.Toast;

import com.donal.wechat.R;
import com.ubirtls.PDR.PDRService;

import config.WCApplication;

/**
 * ��ͼģ��
 * 
 * @author crazy
 * @Date 2015-5-8
 */
public class UbilocMapActivity extends MapActivity {
	private MapView mMapView;
	protected WCApplication appContext;
	private ListView xlistView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appContext=WeChat.returnAppContex();
		setContentView(R.layout.map_activity);
		SysApplication.getInstance().addActivity(this);
		mMapView = (MapView) findViewById(R.id.mapView);
		xlistView = (ListView) findViewById(R.id.xmaplist);
		//防止图像不显示
		UbilocMap.init(mMapView, UbilocMapActivity.this);
		FriendHeadList.initHeadList(xlistView, UbilocMapActivity.this, appContext);
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
						SysApplication.getInstance().exit();//必须的一步，用于关闭UbiLocMapActivity
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
