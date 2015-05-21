package ui.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import bean.UserInfo;

import com.donal.wechat.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import config.CommonValue;

public class FriendMapCardAdaper extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<UserInfo> cards;
	public static final String mytag = "Test";

	static class CellHolder {
		TextView alpha;
		ImageView avatarImageView;
		TextView titleView;
		TextView desView;
	}

	public FriendMapCardAdaper(Context context, List<UserInfo> cards) {
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.cards = cards;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cards.size();

	}

	@Override
	public Object getItem(int arg0) {
		return cards.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CellHolder cell = null;
		if (convertView == null) {
			cell = new CellHolder();
			convertView = inflater.inflate(R.layout.friend_map_card, null);
			cell.avatarImageView = (ImageView) convertView
					.findViewById(R.id.friendhead);
			// cell.avatarImageView.setBackgroundColor(Color.TRANSPARENT);
			convertView.setTag(cell);
			Log.v(mytag, "convertView == null");
		} else {
			cell = (CellHolder) convertView.getTag();
			Log.v(mytag, "convertView != null");
		}
		final UserInfo model = cards.get(position);
		ImageLoader.getInstance().displayImage(
				CommonValue.BASE_URL + model.userHead, cell.avatarImageView,
				CommonValue.DisplayOptions.default_options);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// ((Friend)context).createChat(model.userId+XmppConnectionManager.BASE_XMPP_SERVER_NAME);
				// Log.i(model.userId);
			}
		});
		return convertView;
	}
}
