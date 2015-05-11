package com.ubiloc.layer;

import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

import com.ubiloc.component.ComponentUtil;

import eu.geopaparazzi.spatialite.database.spatial.DataBaseBean;
import eu.geopaparazzi.spatialite.database.spatial.SpatialDatabasesManager;

/**
 * @author wuyf 负责管理所有图层的 添加、移除、清空、隐藏
 */
public class MapLayersManager {
	public static final int SUCCESS = 1;
	public static final int FAILED = 0;
	private static MapLayersManager nowLayers = null;
	private List<DataBaseBean> beans = new ArrayList<DataBaseBean>();

	private MapLayersManager() {
	}

	/*
	 * 创建对象
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

	// 根据moduleitem 初始化MapLayerManager
	public int initData() {
		try {
			initAllLayer();
			return SUCCESS;
		} catch (Exception e) {
			return FAILED;
		}

	}

	private void initAllLayer() {
		// AtLayer base_layer = base_layers.get(j);
		String centerxy = "116.429838,40.14607";
		// String minScale = base_layer.getMinScale();
		// String maxScale = base_layer.getMaxScale();
		// if (centerxy != null && !centerxy.equals("") && minScale != null
		// && !minScale.equals("") && maxScale != null
		// && !maxScale.equals("")) {
		// List<AtData> base_datas = base_layer.getDatas();
		// AtData base_data = base_datas.get(n);
		// 文件路径

		String path = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + ComponentUtil.MAP_PATH + "dianzi.sqlitedb";
		// 表名
		String id = "dianzi";

		DataBaseBean mbtest1 = new DataBaseBean();
		mbtest1.setFileAbsolutePath(path);
		mbtest1.setDataType(ComponentUtil.DATATYPE_SQLITEDB);
		mbtest1.setCenterLat(Double.parseDouble(centerxy.split(",")[1]));
		mbtest1.setCenterLng(Double.parseDouble(centerxy.split(",")[0]));
		mbtest1.setMaxScale(17);
		mbtest1.setMinScale(10);
		mbtest1.setTableName(id);
		beans.add(mbtest1);
		// }

		SpatialDatabasesManager.getInstance().initData(null, beans);
	}

	public List<DataBaseBean> getBeans() {
		return beans;
	}

	public void setBeans(List<DataBaseBean> beans) {
		this.beans = beans;
	}
}
