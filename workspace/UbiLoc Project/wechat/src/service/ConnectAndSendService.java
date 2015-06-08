package service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ubimessage.MessageITException;
import ubimessage.client.MOMClient;
import ubimessage.message.Message;
import ubimessage.message.MessageListener;
import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.ubiloc.model.MOLogInRequest;
import com.ubiloc.model.MORangeRequest;
import com.ubiloc.model.MovingObj;
import com.ubiloc.tools.ConstConfig;
import com.ubiloc.ubilocmap.UbilocMapActivity;

public class ConnectAndSendService extends IntentService {

	public static MOMClient sender;
	//public static String userid;
	private static String TAG="TAG_Service";
	private List<MovingObj> listObj;
	//private JSONArray jArray;
	private MovingObj mObj;
	private MORangeRequest mRequest;
	private MOLogInRequest lRequest;
	private Message m;
	private String senderId;
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
		senderId=UbilocMapActivity.userid;
		Log.v(TAG,"onCreate");
		if(flag==0){
			sender=new MOMClient("192.168.1.240",3009,"sender");
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
				String type=intent.getStringExtra("type");
				
				
				if(type.equals(ConstConfig.LOC_SEND_OPERATOR)){
				listObj=(List<MovingObj>)intent.getSerializableExtra("MovingObjMsg");
				Log.v(TAG,listObj.toString());
				MovingObj endObj=listObj.get(4);
				String fiveObj=new Gson().toJson(endObj);
				Timestamp timestamp = new Timestamp(System.currentTimeMillis()); 
				String mObj=ConstConfig.LOC_SEND_OPERATOR+"#"+fiveObj;
				Log.v(TAG,fiveObj);
				m=new Message();
				m.setRecipient("LBSReceiver");
				m.setSender("sender");
				//Log.v(TAG,"message");
				try {
					Log.v(TAG, "beforesend");
					m.setContent(mObj);
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
				
				}else if(type.equals(ConstConfig.Range_MO_Query)){
					mRequest=(MORangeRequest) intent.getSerializableExtra("RangeQuery");
					String sendObj=new Gson().toJson(mRequest);
					String mObj=ConstConfig.Range_MO_Query+"#"+sendObj;
					m=new Message();
					m.setRecipient("LBSReceiver");
					m.setSender("sender");
					//Log.v(TAG,"message");
					try {
						Log.v(TAG, "beforesend");
						m.setContent(mObj);
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
					
					sender.setMessageListener(new MessageListener() {
						
						@Override
						public void messageReceived(Message m) {
							String refiveobj="";
							try {
								refiveobj = (String) m.getContent();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							String[] marray=refiveobj.split("#");
							if(marray[0].equals(ConstConfig.NAVI_PATH)){
								Log.v(TAG, marray[1]);
							}else if(marray[0].equals(ConstConfig.Range_MO_Results)){
								Log.v(TAG, marray[1]);
							}else if(marray[0].equals(ConstConfig.KNN_MO_Results)){
								Log.v(TAG, marray[1]);
							}
							
							
						}
						
						@Override
						public void exceptionRaised(Exception arg0) {
							// TODO Auto-generated method stub
							
						}
					});
				}
				else if(type.equals(ConstConfig.MO_LOGIN)){
					lRequest=(MOLogInRequest) intent.getSerializableExtra("Login");
					String sendObj=new Gson().toJson(lRequest);
					String mObj=ConstConfig.MO_LOGIN+"#"+sendObj;
					m=new Message();
					m.setRecipient("LBSReceiver");
					m.setSender("sender");
					//Log.v(TAG,"message");
					try {
						Log.v(TAG, "beforesend");
						m.setContent(mObj);
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
				}
	}
}
