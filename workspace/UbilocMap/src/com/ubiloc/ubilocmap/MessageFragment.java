package com.ubiloc.ubilocmap;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ubilocmap.R;
import com.ubiloc.network.UbilocUser;

@SuppressLint("NewApi")
public class MessageFragment extends Fragment {

	public MessageFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View messageFragView = inflater.inflate(R.layout.message_fragment,
				container);
		TextView test = (TextView) messageFragView.findViewById(R.id.test_net);
		test.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				UbilocUser.getInstance().login("", "");
			}
		});
		return messageFragView;
	}

}
