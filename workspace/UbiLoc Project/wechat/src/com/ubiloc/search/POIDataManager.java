package com.ubiloc.search;

public class POIDataManager {
	private String data = "";
	private static POIDataManager mPOIDataManager;

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
}
