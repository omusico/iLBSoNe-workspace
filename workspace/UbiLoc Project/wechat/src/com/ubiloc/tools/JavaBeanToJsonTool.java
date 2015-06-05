package com.ubiloc.tools;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;


import ubimessage.MessageITException;
import ubimessage.client.MOMClient;
import ubimessage.message.Message;

public class JavaBeanToJsonTool {
	/**
	 * 将java对象List遍历并转换为json格式并通过消息中间件发送
	 * @author wangrui
	 * @param <T>
	 * @param t 需要转化为jsonObject格式的JavaBean对象
	 * @param exfield 希望剔除的Key
	 * @return json对象
	 * @throws MessageITException 
	 * @throws IOException 
	 */
	/*public static <T> JSONObject javaTojsonAndSend(List<T> t,String[] exfield) throws IOException, MessageITException{
		//create a client to send messages
		MOMClient sender=new MOMClient("192.168.1.101",3009,"sender");//配置
		//then connect it to the dispatcher
		
		
		//ConstConfig.sender.connect();
		//now send multiple messages to receiver with different contents
		Message m;
		
		
		JsonConfig config = new JsonConfig();
	    config.setExcludes(exfield);   
		JSONObject jsObject=new JSONObject();
		Object object=new Object();
		//object=t.get(0);
		
		Iterator<T> iter=t.iterator();
		while(iter.hasNext()){
			object=iter.next();
			jsObject = JSONObject.fromObject(object, config);
			m=new Message();
			m.setRecipient("receiver");
			m.setSender("sender");
			m.setContent(jsObject);
			sender.sendMessage(m);
			System.out.println(jsObject);
		}
		//ConstConfig.sender.disconnect();
		return jsObject;
	}*/
}
