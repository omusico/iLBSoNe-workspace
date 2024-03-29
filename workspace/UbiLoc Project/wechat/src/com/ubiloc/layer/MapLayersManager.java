package com.ubiloc.layer;

import java.io.File;

import android.os.Environment;

import com.ubiloc.component.ComponentUtil;

import eu.geopaparazzi.spatialite.database.spatial.SpatialDatabasesManager;

//import eu.geopaparazzi.spatialite.database.spatial.DataBaseBean;

/**
 * @author wuyf
 */
public class MapLayersManager {
	public static final int SUCCESS = 1;
	public static final int FAILED = 0;
	private static MapLayersManager nowLayers = null;

	// private List<DataBaseBean> beans = new ArrayList<DataBaseBean>();

	private MapLayersManager() {
	}

	/*
	 */
	public static MapLayersManager getInstance() {
		if (nowLayers == null) {
			nowLayers = new MapLayersManager();
		}
		return nowLayers;
	}

	public static void Clear() {
		nowLayers = null;
		SpatialDatabasesManager.reset();
	}

	public int initData() {
		try {
			initAllLayer();
			return SUCCESS;
		} catch (Exception e) {
			return FAILED;
		}

	}

	private void initAllLayer() {

		// DataBaseBean mbtest1 = getDataBaseBean1();
		// beans.add(mbtest1);
		// DataBaseBean mbtest2 = getDataBaseBean2();
		// beans.add(mbtest2);
		// DataBaseBean mbtest3 = getDataBaseBean3();
		// beans.add(mbtest3);
		// 地图4测试半成功
		// DataBaseBean mbtest4 = getDataBaseBean4();
		// beans.add(mbtest4);
		//
		// // 地图5使用真实的室内地图测试
		// DataBaseBean mbtest5 = getDataBaseBean5();
		// beans.add(mbtest5);
		// // 地图6使用真实的室内地图测试
		// DataBaseBean mbtest6 = getDataBaseBean6();
		// beans.add(mbtest6);
		// String path = Environment.getExternalStorageDirectory()
		// .getAbsolutePath()
		// + ComponentUtil.MAP_PATH
		// + "testmap2.mbtiles";

		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + ComponentUtil.MAP_PATH;
		File file = new File(path);
		SpatialDatabasesManager.getInstance().init(null, file);
	}

	// private DataBaseBean getDataBaseBean6() {
	// String centerxy = "109.888,0.2650";
	// String path = Environment.getExternalStorageDirectory()
	// .getAbsolutePath()
	// + ComponentUtil.MAP_PATH
	// + "testmap2.mbtiles";
	//
	// String id = "testmap2";
	// DataBaseBean mbtest1 = new DataBaseBean();
	// mbtest1.setFileAbsolutePath(path);
	// mbtest1.setDataType(ComponentUtil.DATATYPE_MBTILES);
	// mbtest1.setCenterLat(Double.parseDouble(centerxy.split(",")[1]));
	// mbtest1.setCenterLng(Double.parseDouble(centerxy.split(",")[0]));
	// mbtest1.setMaxScale(16);
	// mbtest1.setMinScale(10);
	// mbtest1.setTableName(id);
	// return mbtest1;
	// }
	//
	// private DataBaseBean getDataBaseBean5() {
	// String centerxy = "0.44769999999999754,0.2417";
	// String path = Environment.getExternalStorageDirectory()
	// .getAbsolutePath() + ComponentUtil.MAP_PATH + "MyPro.mbtiles";
	// String id = "MyPro";
	// DataBaseBean mbtest1 = new DataBaseBean();
	// mbtest1.setFileAbsolutePath(path);
	// mbtest1.setDataType(ComponentUtil.DATATYPE_MBTILES);
	// mbtest1.setCenterLat(Double.parseDouble(centerxy.split(",")[1]));
	// mbtest1.setCenterLng(Double.parseDouble(centerxy.split(",")[0]));
	// mbtest1.setMaxScale(17);
	// mbtest1.setMinScale(0);
	// mbtest1.setTableName(id);
	// return mbtest1;
	// }
	//
	// private DataBaseBean getDataBaseBean4() {
	// String centerxy = "1.40625,25.1652";
	// String path = Environment.getExternalStorageDirectory()
	// .getAbsolutePath()
	// + ComponentUtil.MAP_PATH
	// + "test_road.mbtiles";
	// String id = "test_road";
	// DataBaseBean mbtest1 = new DataBaseBean();
	// mbtest1.setFileAbsolutePath(path);
	// mbtest1.setDataType(ComponentUtil.DATATYPE_MBTILES);
	// mbtest1.setCenterLat(Double.parseDouble(centerxy.split(",")[1]));
	// mbtest1.setCenterLng(Double.parseDouble(centerxy.split(",")[0]));
	// mbtest1.setMaxScale(3);
	// mbtest1.setMinScale(0);
	// mbtest1.setTableName(id);
	// return mbtest1;
	// }

	// private DataBaseBean getDataBaseBean3() {
	// String centerxy = "16.158142,50.8631775";
	// String path = Environment.getExternalStorageDirectory()
	// .getAbsolutePath()
	// + ComponentUtil.MAP_PATH
	// + "testSqlite.sqlitedb";
	// String id = "testSqlite";
	// DataBaseBean mbtest1 = new DataBaseBean();
	// mbtest1.setFileAbsolutePath(path);
	// mbtest1.setDataType(ComponentUtil.DATATYPE_SQLITEDB);
	// mbtest1.setCenterLat(Double.parseDouble(centerxy.split(",")[1]));
	// mbtest1.setCenterLng(Double.parseDouble(centerxy.split(",")[0]));
	// mbtest1.setMaxScale(20);
	// mbtest1.setMinScale(1);
	// mbtest1.setTableName(id);
	// return mbtest1;
	// }
	//
	// private DataBaseBean getDataBaseBean2() {
	//
	// String centerxy = "116.429838,40.14607";
	// String path = Environment.getExternalStorageDirectory()
	// .getAbsolutePath() + ComponentUtil.MAP_PATH + "dianzi.sqlitedb";
	// String id = "dianzi";
	// DataBaseBean mbtest1 = new DataBaseBean();
	// mbtest1.setFileAbsolutePath(path);
	// mbtest1.setDataType(ComponentUtil.DATATYPE_SQLITEDB);
	// mbtest1.setCenterLat(Double.parseDouble(centerxy.split(",")[1]));
	// mbtest1.setCenterLng(Double.parseDouble(centerxy.split(",")[0]));
	// mbtest1.setMaxScale(20);
	// mbtest1.setMinScale(1);
	// mbtest1.setTableName(id);
	// return mbtest1;
	// }
	//
	// private DataBaseBean getDataBaseBean1() {
	//
	// String centerxy = "0.0003447222,0.000308888";
	// String path = Environment.getExternalStorageDirectory()
	// .getAbsolutePath() + ComponentUtil.MAP_PATH + "gujia.sqlite";
	// String id = "gujia";
	// DataBaseBean mbtest1 = new DataBaseBean();
	// mbtest1.setFileAbsolutePath(path);
	// mbtest1.setDataType(ComponentUtil.DATATYPE_SQLITE);
	// mbtest1.setCenterLat(Double.parseDouble(centerxy.split(",")[1]));
	// mbtest1.setCenterLng(Double.parseDouble(centerxy.split(",")[0]));
	// mbtest1.setMaxScale(20);
	// mbtest1.setMinScale(1);
	// mbtest1.setTableName(id);
	// return mbtest1;
	// }
	//
	// public List<DataBaseBean> getBeans() {
	// return beans;
	// }
	//
	// public void setBeans(List<DataBaseBean> beans) {
	// this.beans = beans;
	// }
}
