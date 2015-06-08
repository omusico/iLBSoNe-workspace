package com.ubiloc.search;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.donal.wechat.R;
import com.ubiloc.tools.MORangeRequestTools;
import com.ubiloc.ubilocmap.UbilocMapActivity;

public class UserSearchManager {
	private static UserSearchManager mUserSearchManager;
	private Context mContext;

	private UserSearchManager(Context context) {
		this.mContext = context;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public static void init(Context context) {
		mUserSearchManager = new UserSearchManager(context);
	}

	public static UserSearchManager getInstance() {
		return mUserSearchManager;
	}

	/**
	 * 范围查询用户
	 */
	public void searchUserByDistance() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		LayoutInflater inlfater = LayoutInflater.from(mContext);
		View dialog_content = inlfater.inflate(
				R.layout.search_users_by_distance, null);
		builder.setView(dialog_content);
		final AlertDialog dialog = builder.create();
		dialog.show();
		final EditText distance = (EditText) dialog_content
				.findViewById(R.id.search_input);
		View ok = dialog_content.findViewById(R.id.search_users_ok);
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				String dis = distance.getText().toString();
				if (!dis.equals("")) {

					MORangeRequestTools.RangRequest(mContext,
							UbilocMapActivity.userid, dis);

					dialog.dismiss();
				} else {

					Toast.makeText(mContext, "输入内容不能为空", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		View cancle = dialog_content.findViewById(R.id.search_users_cancle);
		cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});
	}
}
