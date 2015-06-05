package com.ubiloc.search;

import java.util.ArrayList;
import java.util.List;

import org.mapsforge.core.model.GeoPoint;

public class POIDataManager {
	private String data = "";
	private static POIDataManager mPOIDataManager;
	/**
	 * 记录当前的poi信息
	 */
	private List<PoiObject> cur_pois;
	// ===================================================================================
	// POI查询测试数据
	public static final String POI_KEW_WORDS_FIRE_HYDRAN = "poi_key_words_fire_hydran";
	public static final String POI_KEW_WORDS_EXIT = "poi_key_words_exit";
	public static final String POI_KEW_WORDS_STAIR = "poi_key_words_stair";
	public static final String POI_KEW_WORDS_ELEVATOR = "poi_key_words_elevator";
	public static final String POI_KEW_WORDS_WC = "poi_key_words_wc";
	public static final String POI_KEW_WORDS_LAB = "poi_key_words_lab";
	public static final String POI_KEW_WORDS_BOARD_ROOM = "poi_key_words_board_room";

	// ===================================================================================
	private POIDataManager() {
	}

	public static POIDataManager getInstance() {
		if (mPOIDataManager == null)
			mPOIDataManager = new POIDataManager();
		return mPOIDataManager;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

	/**
	 * 内部人员测试版
	 * 
	 * @param keyword
	 * @return
	 */
	public List<PoiObject> getPoiByKeyword_alpha(String keyword) {
		List<PoiObject> pois = new ArrayList<PoiObject>();
		if (keyword.equals(POI_KEW_WORDS_FIRE_HYDRAN)) {
			pois.add(new PoiObject(PoiClass.POI_CLASS_FIRE_HYDRAN, "消防栓-1",
					new GeoPoint(-0.000487, 109.513773), 4, "4楼的消防栓"));
			pois.add(new PoiObject(PoiClass.POI_CLASS_FIRE_HYDRAN, "消防栓-2",
					new GeoPoint(-0.000485, 109.513775), 4, "4楼的消防栓"));
			pois.add(new PoiObject(PoiClass.POI_CLASS_FIRE_HYDRAN, "消防栓-3",
					new GeoPoint(-0.000487, 109.513772), 4, "4楼的消防栓"));
			pois.add(new PoiObject(PoiClass.POI_CLASS_FIRE_HYDRAN, "消防栓-4",
					new GeoPoint(-0.000489, 109.513775), 4, "4楼的消防栓"));
			pois.add(new PoiObject(PoiClass.POI_CLASS_FIRE_HYDRAN, "消防栓-5",
					new GeoPoint(-0.000460, 109.513775), 4, "4楼的消防栓"));
			pois.add(new PoiObject(PoiClass.POI_CLASS_FIRE_HYDRAN, "消防栓-6",
					new GeoPoint(-0.000487, 109.513777), 4, "4楼的消防栓"));
			pois.add(new PoiObject(PoiClass.POI_CLASS_FIRE_HYDRAN, "消防栓-7",
					new GeoPoint(-0.000487, 109.513780), 4, "4楼的消防栓"));
			pois.add(new PoiObject(PoiClass.POI_CLASS_FIRE_HYDRAN, "消防栓-8",
					new GeoPoint(-0.000490, 109.513775), 4, "4楼的消防栓"));
			pois.add(new PoiObject(PoiClass.POI_CLASS_FIRE_HYDRAN, "消防栓-9",
					new GeoPoint(-0.000495, 109.513773), 4, "4楼的消防栓"));
			pois.add(new PoiObject(PoiClass.POI_CLASS_FIRE_HYDRAN, "消防栓-10",
					new GeoPoint(-0.000484, 109.513772), 4, "4楼的消防栓"));
			pois.add(new PoiObject(PoiClass.POI_CLASS_FIRE_HYDRAN, "消防栓-11",
					new GeoPoint(-0.000485, 109.513771), 4, "4楼的消防栓"));
			pois.add(new PoiObject(PoiClass.POI_CLASS_FIRE_HYDRAN, "消防栓-12",
					new GeoPoint(-0.000481, 109.513774), 4, "4楼的消防栓"));
		}
		if (keyword.equals(POI_KEW_WORDS_EXIT)) {

		}
		if (keyword.equals(POI_KEW_WORDS_STAIR)) {

		}
		if (keyword.equals(POI_KEW_WORDS_ELEVATOR)) {

		}
		if (keyword.equals(POI_KEW_WORDS_WC)) {

		}
		if (keyword.equals(POI_KEW_WORDS_LAB)) {

		}
		if (keyword.equals(POI_KEW_WORDS_LAB)) {

		}
		cur_pois = pois;
		return pois;
	}

	/**
	 * 正式版，从后台读取数据，该功能尚未实现
	 * 
	 * @param keyword
	 * @return
	 */
	public List<PoiObject> getPoiByKeyword(String keyword) {

		return null;
	}

	/**
	 * 获取当前的pois
	 * 
	 * @return
	 */
	public List<PoiObject> getCurPoi() {
		return cur_pois;
	}
}
