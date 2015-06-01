package config;

import java.util.List;

import tools.StringUtils;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.ubiloc.geofencing.GeofenceMsg;

import db.DBManager;
import db.SQLiteTemplate;
import db.SQLiteTemplate.RowMapper;

/**
 * 地理围栏推送消息管理
 * @author wangrui1
 *
 */

public class GeofenceMsgManager {
	private static GeofenceMsgManager gMsgManager=null;
	private static DBManager manager=null;
	
	private GeofenceMsgManager(Context context){
		SharedPreferences sharedPre = context.getSharedPreferences(
				CommonValue.LOGIN_SET, Context.MODE_PRIVATE);
		String databaseName = sharedPre.getString(CommonValue.USERID, null);
		manager = DBManager.getInstance(context, databaseName);
	}
	
	public static GeofenceMsgManager getInstance(Context context){
		if(gMsgManager==null){
			gMsgManager=new GeofenceMsgManager(context);
		}
		return gMsgManager;
	}
	
	private GeofenceMsgManager(){
		manager=DBManager.getInstance("geodb");
	}
	
	public static GeofenceMsgManager getInstance(){
		if(gMsgManager==null){
			gMsgManager=new GeofenceMsgManager();
		}
		return gMsgManager;
	}
	
	public static void destroy(){
		gMsgManager=null;
		manager=null;
	}
	/**
	 * 保存地理围栏消息
	 * @param gMsg
	 * @return
	 */
	public long saveGeoMsg(GeofenceMsg gMsg){
		SQLiteTemplate st=SQLiteTemplate.getInstance(manager, false);
		ContentValues cValues=new ContentValues();
		if(StringUtils.notEmpty(gMsg.getMsgcontent())){
			cValues.put("msgcontent", StringUtils.doEmpty(gMsg.getMsgcontent()));
		}
		if(StringUtils.notEmpty(gMsg.getMsgtime())){
			cValues.put("msgtime", StringUtils.doEmpty(gMsg.getMsgtime()));
		}
		cValues.put("msgtype", gMsg.getMsgtype());
		cValues.put("msgstatus", gMsg.getMsgstatus());
		
		
		return st.insert("im_geofence", cValues);
	}
	
	public List<GeofenceMsg> getAllGeoMsgList(){
		SQLiteTemplate st=SQLiteTemplate.getInstance(manager, false);
		List<GeofenceMsg> geolist=st.queryForList(new RowMapper<GeofenceMsg>(){

			@Override
			public GeofenceMsg mapRow(Cursor cursor, int index) {
				GeofenceMsg gMsg=new GeofenceMsg();
				gMsg.setId(cursor.getString(cursor.getColumnIndex("_id")));
				gMsg.setMsgcontent(cursor.getString(cursor.getColumnIndex("msgcontent")));
				gMsg.setMsgstatus(cursor.getInt(cursor.getColumnIndex("msgstatus")));
				gMsg.setMsgtime(cursor.getString(cursor.getColumnIndex("msgtime")));
				gMsg.setMsgtype(cursor.getInt(cursor.getColumnIndex("msgtime")));
				return gMsg;
			}
			
}, "select * from im_geofence where msgstatus=" +GeofenceMsg.All+"", null);
		return geolist;
	}
}
