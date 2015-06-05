package service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ubimessage.MessageITException;
import ubimessage.client.MOMClient;
import ubimessage.message.Message;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.ubiloc.model.MovingObj;

public class ConnectAndSendService extends IntentService {

	public static MOMClient sender;
	private static String TAG="TAG_Service";
	private List<MovingObj> listObj;
	//private JSONArray jArray;
	private MovingObj mObj;
	private Message m;
	private static int flag=0;
	public ConnectAndSendService() {
		super("ConnectAndSendService");
		Log.v(TAG,"ConstructorRunning");
		// TODO Auto-generated constructor stub
	}

	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.v(TAG,"onCreate");
		if(flag==0){
			sender=new MOMClient("192.168.1.101",3009,"sender");
		}
		//
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		Log.v(TAG,"onStart");
		super.onStart(intent, startId);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.v(TAG,"onStart");
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.v(TAG,"onStartCommand");
		return super.onStartCommand(intent, flags, startId);
		
	}
	

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.v(TAG,"onHandleIntent");
				listObj=new ArrayList<MovingObj>();
				if(flag==0){
				try {
					sender.connect();
					flag=1;
					Log.v(TAG, "sender connected!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessageITException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}}
				
				listObj=(List<MovingObj>)intent.getSerializableExtra("MovingObjMsg");
				Log.v(TAG,listObj.toString());
				String fiveObj=new Gson().toJson(listObj);
				//下面一步出现问题
				//JSONArray jArray=JSONArray.fromObject(listObj);
				Log.v(TAG,fiveObj);
				m=new Message();
				m.setRecipient("receiver");
				m.setSender("sender");
				//Log.v(TAG,"message");
				try {
					Log.v(TAG, "beforesend");
					m.setContent(fiveObj);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					sender.sendMessage(m);
					Log.v(TAG, "sended");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				/*Iterator<MovingObj> iter=listObj.iterator();
				while(iter.hasNext()){
					Log.v(TAG, "beforesend");
					mObj=iter.next();
					jObject = JSONObject.fromObject(mObj);
					m=new Message();
					m.setRecipient("receiver");
					m.setSender("sender");
					try {
						m.setContent(jObject);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						sender.sendMessage(m);
						Log.v(TAG, "sended");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.v(TAG, m.toString());
				}*/
				//jObject=JSONObject.
		
	}
}
