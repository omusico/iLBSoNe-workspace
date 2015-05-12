package com.ubiloc.me;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.ubilocmap.R;

public class MePreferenceActivity extends Activity {
	private View back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.me_preference);
		back = findViewById(R.id.me_preference_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MePreferenceActivity.this.finish();
			}
		});
	}

}
