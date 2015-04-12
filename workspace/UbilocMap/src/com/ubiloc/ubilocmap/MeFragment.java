package com.ubiloc.ubilocmap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ubilocmap.R;

@SuppressLint("NewApi")
public class MeFragment extends Fragment {

	public MeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View meFragView = inflater.inflate(R.layout.me_fragment, container);
		return meFragView;
	}

}
