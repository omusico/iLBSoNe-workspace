package com.ubiloc.ubilocmap;

import im.model.HistoryChatBean;

import java.util.ArrayList;
import java.util.List;

import org.mapsforge.core.model.GeoPoint;

import tools.UIHelper;
import ui.adapter.FriendMapCardAdaper;
import ui.view.RoundImageView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;
import bean.StrangerEntity;
import bean.UserInfo;

import com.donal.wechat.R;
import com.ubiloc.search.PoiObject;

import config.ApiClent;
import config.WCApplication;
import config.ApiClent.ClientCallback;

public class FriendHeadList {
	
	private String Head_TAG="FriendHeadList";
	private static FriendHeadList friendheadlist;
	private ListView xlistView;
	private List<UserInfo> datas;
	private FriendMapCardAdaper mAdapter;
	protected WCApplication appContext;
	private Context context;
	private static int flag=0;//模拟三个好友
	private int currentPage;
	private int lvDataState;
	
	public static void initHeadList(ListView xlistView,Context context,WCApplication appContext){
		friendheadlist=new FriendHeadList(xlistView, context, appContext);
	}
	public FriendHeadList(ListView xlistView, Context context,WCApplication appContext) {
		super();
		this.xlistView = xlistView;
		this.appContext = appContext;
		this.context=context;
		initUI();
		getFriendCardFromCache();
	}
	private void initUI() {
		// xlistView.setOnScrollListener((OnScrollListener) this);滚动listview
		xlistView.setDividerHeight(0);
		datas = new ArrayList<UserInfo>();
		mAdapter =new FriendMapCardAdaper(context, datas);
		xlistView.setAdapter(mAdapter);
		mAdapter.setOnClickListener(onFriendClickListener);
		
	}

	/**
	 * 点击好友图像
	 */
	private OnClickListener onFriendClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.v(Head_TAG, "onclicked");
			flag=flag+1;
			UbilocMap uMap=UbilocMap.getInstance();
			
			
			RoundImageView roundView=(RoundImageView) v.findViewById(R.id.friendhead);
			BitmapDrawable drawable=(BitmapDrawable) roundView.getDrawable();
			Bitmap bitmap=drawable.getBitmap();
			//模拟三个好友位置信息
			if(flag==0){
			GeoPoint gPoint=new GeoPoint(-0.000464,109.514206);
			uMap.addFriendImage(gPoint, bitmap);
			}
			else if(flag==1){
				GeoPoint gPoint=new GeoPoint(-0.000415,109.514127);
				uMap.addFriendImage(gPoint, bitmap);
			}else {
				GeoPoint gPoint=new GeoPoint(-0.000531,109.514215);
				uMap.addFriendImage(gPoint, bitmap);
			}
			
		}
	};
	
	
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
						Toast.makeText(context, message,
								Toast.LENGTH_LONG).show();
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
