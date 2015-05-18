package com.ubiloc.ubilocmap;

import org.mapsforge.android.maps.MapView;
import org.mapsforge.android.maps.overlay.OverlayItem;
import org.mapsforge.android.maps.overlay.OverlayWay;

import android.content.Context;

import com.ubiloc.overlays.BaseOverlay;

public class ArrayOverlay extends BaseOverlay {

	public ArrayOverlay(Context context, MapView mapView) {
		super(context, mapView);
	}

	@Override
	public int waySize() {
		return 0;
	}

	@Override
	public int itemSize() {
		return 0;
	}

	@Override
	protected OverlayWay createWay(int index) {
		return null;
	}

	@Override
	protected OverlayItem createItem(int index) {
		return null;
	}

}
