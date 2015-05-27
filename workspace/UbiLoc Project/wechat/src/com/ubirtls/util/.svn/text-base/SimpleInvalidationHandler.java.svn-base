package com.ubirtls.util;

import android.os.Handler;
import android.os.Message;
import android.view.View;

public class SimpleInvalidationHandler extends Handler {
	private final View view;

	public SimpleInvalidationHandler(final View pView) {
		super();
		view = pView;
	}

	@Override
	public void handleMessage(final Message msg) {
		switch (msg.what) {
		case 1:
			view.invalidate();
			break;
		}
	}
}
