package com.ubiloc.ubilocmap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ubilocmap.R;

@SuppressLint("NewApi")
public class FriendFragment extends Fragment {

	public FriendFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View friendFragView = inflater.inflate(R.layout.friend_fragment,
				container);
		return friendFragView;
	}

}
