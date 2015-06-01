package com.ubiloc.asynctask;

import java.io.IOException;

import ubimessage.MessageITException;
import ubimessage.client.MOMClient;
import ubimessage.message.Message;
import ubimessage.message.MessageListener;

import com.ubiloc.geofencing.GeofenceMsg;

import config.GeofenceMsgManager;
import android.os.AsyncTask;

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
					String s = (String) m.getContent();
					String time = (System.currentTimeMillis()/1000)+"";
					GeofenceMsg gMsg=new GeofenceMsg();
					gMsg.setMsgcontent(s);
					gMsg.setMsgstatus(GeofenceMsg.All);//并未设置为UNREAD
					gMsg.setMsgtime(time);
					gMsg.setMsgtype(GeofenceMsg.GEO_MSG);
					gManager.saveGeoMsg(gMsg);
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
