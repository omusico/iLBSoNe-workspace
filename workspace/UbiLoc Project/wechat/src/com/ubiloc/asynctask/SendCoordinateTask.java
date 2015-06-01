package com.ubiloc.asynctask;

import java.io.IOException;

import ubimessage.MessageITException;
import ubimessage.client.MOMClient;
import android.os.AsyncTask;

public class SendCoordinateTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... params) {
		
		//create a client to send messages
		MOMClient sder=new MOMClient("192.168.1.240",3009,"sender");
		//then connect it to the dispatcher
		try {
			sder.connect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessageITException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//now send multiple messages to receiver with different contents
		ubimessage.message.Message m;
		double X1=7714.40079;
		double Y1=2793.04355;
		double Y2=27679.47689;
		double X2=53628.73605;
		double Y3=24368.70685;
		double X3=55868.6587;
		
		String strX1=String.valueOf(X1);
		String strY1=String.valueOf(Y1);
		String strY2=String.valueOf(Y2);
		String strX2=String.valueOf(X2);
		
		String strY3=String.valueOf(Y3);
		String strX3=String.valueOf(X3);
		
		String type="Geometry";
		String userid="zhangky";
		m=new ubimessage.message.Message();
		m.setRecipient("receiver");
		m.setSender("sender");
		try {
			m.setContent(type+" "+userid+" "+strX1+" "+strY1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sder.sendMessage(m);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		m=new ubimessage.message.Message();
		m.setRecipient("receiver");
		m.setSender("sender");
		try {
			m.setContent(type+" "+userid+" "+strX2+" "+strY2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sder.sendMessage(m);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		m=new ubimessage.message.Message();
		m.setRecipient("receiver");
		m.setSender("sender");
		try {
			m.setContent(type+" "+userid+" "+strX1+" "+strY1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sder.sendMessage(m);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		m=new ubimessage.message.Message();
		m.setRecipient("receiver");
		m.setSender("sender");
		try {
			m.setContent(type+" "+userid+" "+strX3+" "+strY3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sder.sendMessage(m);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			sder.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}

	

}
