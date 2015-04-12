package com.ubiloc.ubilocmap;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;

import com.example.ubilocmap.R;

public class UbilocActivity extends ActionBarActivity {
	private TabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ubiloc);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		LayoutInflater inflater = LayoutInflater.from(this);
		View message_tab_view = inflater.inflate(R.layout.message_tab, null);
		View map_tab_view = inflater.inflate(R.layout.map_tab, null);
		View friend_tab_view = inflater.inflate(R.layout.friend_tab, null);
		View me_tab_view = inflater.inflate(R.layout.me_tab, null);
		mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.message_tab))
				.setIndicator(message_tab_view).setContent(R.id.tab_message));

		mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.map_tab))
				.setIndicator(map_tab_view).setContent(R.id.tab_map));

		mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.friend_tab))
				.setIndicator(friend_tab_view).setContent(R.id.tab_friend));

		mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.me_tab))
				.setIndicator(me_tab_view).setContent(R.id.tab_me));

	}

}
