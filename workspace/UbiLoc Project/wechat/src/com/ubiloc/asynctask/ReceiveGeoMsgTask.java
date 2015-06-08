package com.ubiloc.asynctask;

import java.io.IOException;

import ubimessage.MessageITException;
import ubimessage.client.MOMClient;
import ubimessage.message.Message;
import ubimessage.message.MessageListener;
import ui.GeoMsgListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.donal.wechat.R;
import com.ubiloc.geofencing.GeofenceMsg;

import config.GeofenceMsgManager;
import config.WCApplication;

/**
 * 接收dispatcher返回的消息
 * @author wangrui1
 *
 */
public class ReceiveGeoMsgTask extends AsyncTask<Void, Void, Void> {
	public static MOMClient rcpt;

	@Override
	protected Void doInBackground(Void... params) {

		rcpt = new MOMClient("192.168.1.240",3009,
				"receiver");
		rcpt.setMessageListener(new MessageListener() {
			public void messageReceived(Message m) {
				GeofenceMsgManager gManager=GeofenceMsgManager.getInstance();
				try {
					long flag=0;
					String s = (String) m.getContent();
					String time = (System.currentTimeMillis()/1000)+"";
					GeofenceMsg gMsg=new GeofenceMsg();
					gMsg.setMsgcontent(s);
					gMsg.setMsgstatus(GeofenceMsg.All);//并未设置为UNREAD
					gMsg.setMsgtime(time);
					gMsg.setMsgtype(GeofenceMsg.GEO_MSG);
					flag=gManager.saveGeoMsg(gMsg);
					if(flag!=0){
						int icon = R.drawable.geomsg;
				        CharSequence tickerText = "地理围栏服务消息";
				        long when = System.currentTimeMillis();
						NotificationManager notificationManager=WCApplication.getInstance().getNotificationManager();
						Notification notification=new Notification(icon, tickerText, when);
						
						//定义下拉通知栏时要展现的内容信息
				        Context context = WCApplication.getInstance();
				        CharSequence contentTitle = "我的通知栏标展开标题";
				        CharSequence contentText = s;
				        Intent notificationIntent = new Intent(WCApplication.getInstance(), GeoMsgListActivity.class);
				        PendingIntent contentIntent = PendingIntent.getActivity(WCApplication.getInstance(), 0,
				                notificationIntent, 0);
				        notification.setLatestEventInfo(context, contentTitle, contentText,contentIntent);
				         
				        //用mNotificationManager的notify方法通知用户生成标题栏消息通知
				        notificationManager.notify(1, notification);
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
	}

			@Override
			public void exceptionRaised(Exception arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		try {
			rcpt.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessageITException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {//运行在UI线程中
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		//新建异步任务通过通过消息中间件发送坐标信息
		new SendCoordinateTask().execute();
		
	}
	
	
	
}
