package com.ubiloc.ubilocmap;

import java.util.ArrayList;
import java.util.List;

import tools.UIHelper;
import ui.adapter.FriendMapCardAdaper;
import android.content.Context;
import android.widget.ListView;
import android.widget.Toast;
import bean.StrangerEntity;
import bean.UserInfo;
import config.ApiClent;
import config.ApiClent.ClientCallback;
import config.WCApplication;

public class FriendHeadList {

	private static FriendHeadList friendheadlist;
	private ListView xlistView;
	private List<UserInfo> datas;
	private FriendMapCardAdaper mAdapter;
	protected WCApplication appContext;
	private Context context;

	private int currentPage;
	private int lvDataState;

	public static void initHeadList(ListView xlistView, Context context,
			WCApplication appContext) {
		friendheadlist = new FriendHeadList(xlistView, context, appContext);
	}

	public FriendHeadList(ListView xlistView, Context context,
			WCApplication appContext) {
		super();
		this.xlistView = xlistView;
		this.appContext = appContext;
		this.context = context;
		initUI();
		getFriendCardFromCache();
	}

	private void initUI() {
		// xlistView.setOnScrollListener((OnScrollListener) this);滚动listview
		xlistView.setDividerHeight(0);
		datas = new ArrayList<UserInfo>();
		mAdapter = new FriendMapCardAdaper(context, datas);
		xlistView.setAdapter(mAdapter);
	}

	private void getFriendCardFromCache() {
		currentPage = 1;
		findFriend(currentPage, "", UIHelper.LISTVIEW_ACTION_REFRESH);
	}

	private void findFriend(int page, String nickName, final int action) {
		String apiKey = appContext.getLoginApiKey();

		ApiClent.getMyFriend(appContext, apiKey, page + "",
				UIHelper.LISTVIEW_COUNT + "", new ClientCallback() {
					@Override
					public void onSuccess(Object data) {
						StrangerEntity entity = (StrangerEntity) data;
						switch (entity.status) {
						case 1:

							handleFriends(entity, action);

							break;
						default:
							Toast.makeText(context, entity.msg,
									Toast.LENGTH_LONG).show();
							break;
						}
					}

					@Override
					public void onFailure(String message) {
						Toast.makeText(context, message, Toast.LENGTH_LONG)
								.show();
					}

					@Override
					public void onError(Exception e) {
					}
				});
	}

	private void handleFriends(StrangerEntity entity, int action) {
		switch (action) {
		case UIHelper.LISTVIEW_ACTION_INIT:
		case UIHelper.LISTVIEW_ACTION_REFRESH:
			datas.clear();
			datas.addAll(entity.userList);
			break;
		case UIHelper.LISTVIEW_ACTION_SCROLL:
			datas.addAll(entity.userList);
			break;
		}
		if (entity.userList.size() == UIHelper.LISTVIEW_COUNT) {
			lvDataState = UIHelper.LISTVIEW_DATA_MORE;
			mAdapter.notifyDataSetChanged();
			// Log.v(mAdapter.mytag, "notifyDataSetChanged if :");
		} else {
			lvDataState = UIHelper.LISTVIEW_DATA_FULL;
			mAdapter.notifyDataSetChanged();
			// Log.v(mAdapter.mytag, "notifyDataSetChanged else :");
		}
		if (datas.isEmpty()) {
			lvDataState = UIHelper.LISTVIEW_DATA_EMPTY;
		}

		// swipeLayout.setRefreshing(false);
	}
}
