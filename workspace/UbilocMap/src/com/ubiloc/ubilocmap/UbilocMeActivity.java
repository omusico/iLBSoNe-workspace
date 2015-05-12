package com.ubiloc.ubilocmap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.ubilocmap.R;
import com.ubiloc.me.MePreferenceActivity;

public class UbilocMeActivity extends Activity {
	private View me_preference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.me_activity);
		me_preference = findViewById(R.id.me_preference);
		me_preference.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent mePreference = new Intent(UbilocMeActivity.this,
						MePreferenceActivity.class);
				startActivity(mePreference);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
}
