package com.ubiloc.network;

import android.net.Uri;
import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.callback.HttpConnectCallback;

public class UbilocUser {
	public static final int LOGIN_SUCCESS = 1;
	public static final int LOGIN_NO_USER = 0;
	public static final int LOGIN_PASSWORD_ERROR = -1;

	private static UbilocUser user;

	private UbilocUser() {
	}

	public static UbilocUser getInstance() {
		if (user == null) {
			user = new UbilocUser();
		}
		return user;
	}

	public int login(String username, String password) {
		Uri.Builder uriBuilder = new Uri.Builder();
		uriBuilder.appendPath(NetUtil.IP + ":" + NetUtil.LOGIN_URL);
		Uri uri = new Uri.Builder().build();
		AsyncHttpRequest request = new AsyncHttpRequest(uri, "get");
		AsyncHttpClient.getDefaultInstance().execute(request,
				new HttpConnectCallback() {
					@Override
					public void onConnectCompleted(Exception arg0,
							AsyncHttpResponse response) {
						Log.i("abc_ubilocuser", response.message());
					}
				});
		return LOGIN_SUCCESS;
	}
}
