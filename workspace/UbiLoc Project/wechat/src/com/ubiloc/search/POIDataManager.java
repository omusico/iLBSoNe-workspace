package com.ubiloc.search;

import java.util.ArrayList;
import java.util.List;

import com.ubiloc.simulate.SimulatedDataManager;

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
			pois.addAll(SimulatedDataManager.getInstance().getFireHydran());
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
