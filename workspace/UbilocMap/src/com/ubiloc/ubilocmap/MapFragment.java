package com.ubiloc.ubilocmap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ubilocmap.R;

@SuppressLint("NewApi")
public class MapFragment extends Fragment {

	public MapFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mapFragView = inflater.inflate(R.layout.map_fragment, container);
		return mapFragView;
	}

}
