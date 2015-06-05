package com.ubiloc.asynctask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ubimessage.MessageITException;
import android.os.AsyncTask;

import com.ubiloc.model.MovingObj;
import com.ubiloc.tools.ConstConfig;
import com.ubiloc.tools.JavaBeanToJsonTool;

import config.WCApplication;

public class SendMovingObjTask extends AsyncTask<String, Void, Void> {
	private static String userid;
	private List<MovingObj> mlist;
	@Override
	protected Void doInBackground(String... params) {
		// TODO Auto-generated method stub
		mlist=new ArrayList<MovingObj>();
		userid=WCApplication.getInstance().getLoginUid();
		MovingObj mObj=new MovingObj(0, "ww", Double.parseDouble(params[0]), Double.parseDouble(params[1]));
		mlist.add(mObj);
		/*try {
			JavaBeanToJsonTool.javaTojsonAndSend(mlist, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessageITException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		return null;
	}

}
