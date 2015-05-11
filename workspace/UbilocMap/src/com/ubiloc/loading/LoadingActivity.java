package com.ubiloc.loading;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.ubilocmap.R;
import com.ubiloc.ubilocmap.UbilocActivity;

public class LoadingActivity extends Activity {

	public LoadingActivity() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startpage);
		new AsyncTask<String, Integer, byte[]>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected byte[] doInBackground(String... arg0) {
				try {
					Thread.currentThread().sleep(1000);
					Log.i("ubilocmap", "doInBackground");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(byte[] result) {
				super.onPostExecute(result);
				Intent myIntent = new Intent(LoadingActivity.this,
						UbilocActivity.class);
				startActivity(myIntent);
				Log.i("ubilocmap", "onPostExecute");
				LoadingActivity.this.finish();
			}

		}.execute("");
	}

}
