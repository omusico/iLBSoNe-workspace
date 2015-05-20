package com.ubiloc.ubilocmap;

import im.WeChat;

import org.mapsforge.android.maps.MapActivity;
import org.mapsforge.android.maps.MapView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.donal.wechat.R;

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
	private Thread myThread;
	final private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				UbilocMap.init(mMapView, UbilocMapActivity.this);
				break;

			default:
				break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appContext=WeChat.returnAppContex();
		setContentView(R.layout.map_activity);
		mMapView = (MapView) findViewById(R.id.mapView);
		xlistView = (ListView) findViewById(R.id.xmaplist);
		//防止图像不显示
		
		myThread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				//UbilocMap.init(mMapView, UbilocMapActivity.this);
				try {
					Thread.sleep(2000);
					handler.sendEmptyMessage(1);
				} catch (InterruptedException e) {
					
					e.printStackTrace();
				}
				
			}
		});
		myThread.start();
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
	
	/*@Override
	public void onBackPressed() {
		new AlertDialog.Builder(UbilocMapActivity.this).setTitle("确定退出吗?")
				.setNeutralButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						myThread.stop();
						appContext.exit();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).show();
	}*/
	
	
}
