package com.ubirtls.view.Activity;

/**
 * 数据库常量类 存储数据库操作的基本常量
 * 
 * @author 胡旭科
 * @version 1.0
 */
public interface DatabaseConstants {
	/** 数据库名 */
	public static final String DATABASE_NAME = "client.db";
	/** 兴趣点表名 */
	public static final String TABLE_POI_NAME = "poi";
	/** 兴趣点表字段 */
	public static final String TABLE_POI_ID = "id";
	public static final String TABLE_POI_Description = "description";
	public static final String TABLE_POI_X = "x";
	public static final String TABLE_POI_Y = "y";
	public static final String TABLE_POI_Z = "z";
	public static final String TABLE_POI_TYPE = "type";
	/** 客户端兴趣点类别有两种收藏点以及历史点 */	
	public static final int TYPE_HISTORICAL_POINT = 1;
	public static final int TYPE_COLLECTED_POINT = 2;

	/** 建立表格的SQL语句 */
	public static final String CREATE_TABLE_POI = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_POI_NAME + "("+TABLE_POI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + TABLE_POI_Description
			+ " TEXT," + TABLE_POI_X + " TEXT," + TABLE_POI_Y
			+ " TEXT," + TABLE_POI_Z + " TEXT," + TABLE_POI_TYPE
			+ " INTEGER)";
}
