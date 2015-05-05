package com.ubiloc.ubilocmap;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;

import com.example.ubilocmap.R;

public class UbilocActivity extends TabActivity {
	private TabHost mTabHost;

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
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
		Intent message_intent = new Intent(this, UbilocMessageActivity.class);
		Intent friend_intent = new Intent(this, UbilocFriendActivity.class);
		Intent map_intent = new Intent(this, UbilocMapActivity.class);
		Intent me_intent = new Intent(this, UbilocMeActivity.class);
		mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.message_tab))
				.setIndicator(message_tab_view).setContent(message_intent));

		mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.friend_tab))
				.setIndicator(friend_tab_view).setContent(friend_intent));

		mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.map_tab))
				.setIndicator(map_tab_view).setContent(map_intent));

		mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.me_tab))
				.setIndicator(me_tab_view).setContent(me_intent));
	}
}
