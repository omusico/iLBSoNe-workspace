package com.ubiloc.tools;



public class ConstConfig {
	public static int MSG_OBJ=0;
	
	//PDR位置坐标发送操作
		public static String LOC_SEND_OPERATOR = "0201";
		
		//POI查询请求命令
		public static String POI_QUERY_REQUEST = "0310";
		
		//POI查询成功响应
		public static String POI_QUERY_SUCCESS = "0005";
		
		//POI查询失败响应
		public static String POI_QUERY_FAIL = "0006";
		
		//移动对象进入系统
		public static String MO_LOGIN = "0007";
		
		//移动对象退出系统
		public static String MO_LOGOUT = "0008";
		
		//以位置为目标点的导航查询
		public static String NAVI_M2PLACE = "0009";
		
		//以移动对象为目标点的导航查询
		public static String NAVI_M2M = "0010";
		
		//导航路径结果
		public static String NAVI_PATH = "0011";
		
		//结束移动对象到移动对象的查询
		public static String NAVI_M2M_STOP = "0012";
		
		//范围查询查询请求
		public static String Range_MO_Query = "0013";
		
		//移动对象范围查询结果
		public static String Range_MO_Results = "0014";
		
		//结束移动对象范围查询
		public static String Range_MO_Stop = "0015";
		
		//移动对象k近邻查询请求
		public static String KNN_MO_Query = "0016";
		
		//移动对象k近邻查询结果
		public static String KNN_MO_Results = "0017";
		
		//结束移动对象k近邻查询
		public static String KNN_MO_Stop = "0018";
	
}
