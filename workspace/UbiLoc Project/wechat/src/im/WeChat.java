package im;

import im.model.HistoryChatBean;
import im.model.IMMessage;
import im.model.Notice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smackx.OfflineMessageManager;

import tools.Logger;
import ui.GeoMsgListActivity;
import ui.adapter.WeChatAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.donal.wechat.R;
import com.ubiloc.asynctask.ReceiveGeoMsgTask;
import com.ubiloc.tools.MORangeRequestTools;

import config.CommonValue;
import config.MessageManager;
import config.WCApplication;
import config.XmppConnectionManager;

/**
 * wechat 会话界面
 * 
 * @author donal
 * 
 */
public class WeChat extends AWechatActivity implements OnScrollListener,
		OnRefreshListener, OnClickListener {

	private int lvDataState;
	private int currentPage;
	private SwipeRefreshLayout swipeLayout;

	private ListView xlistView;
	private TextView titleBarView;
	private ImageView indicatorImageView;
	private Animation indicatorAnimation;
	private View geoMsgBtn;

	private List<HistoryChatBean> inviteNotices;
	private WeChatAdapter noticeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wechat);
		//登录向LBS服务器发送消息
		MORangeRequestTools.LoginSend(WeChat.this, WCApplication.getInstance().getLoginUid());
		initUI();
		getHistoryChat();
		XMPPConnection connection = XmppConnectionManager.getInstance()
				.getConnection();
		if (!connection.isConnected()) {
			connect2xmpp();
		}

		// 通过异步任务建立对消息中间件消息的监听
		new ReceiveGeoMsgTask().execute();

	}

	@Override
	protected void onResume() {

		// setPaoPao();
		super.onResume();
	}

	// 11111111111
	private void initUI() {
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.xrefresh);
		swipeLayout.setOnRefreshListener(this);
		// 刷新条样式设置
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		titleBarView = (TextView) findViewById(R.id.titleBarView);
		indicatorImageView = (ImageView) findViewById(R.id.xindicator);
		indicatorAnimation = AnimationUtils.loadAnimation(this,
				R.anim.refresh_button_rotation);
		indicatorAnimation.setDuration(500);
		indicatorAnimation.setInterpolator(new Interpolator() {
			private final int frameCount = 10;

			@Override
			public float getInterpolation(float input) {
				return (float) Math.floor(input * frameCount) / frameCount;
			}
		});
		geoMsgBtn = findViewById(R.id.geomsgbtn);
		geoMsgBtn.setOnClickListener(this);
		xlistView = (ListView) findViewById(R.id.xlistview);
		xlistView.setOnScrollListener(this);
		inviteNotices = new ArrayList<HistoryChatBean>();
		inviteNotices = MessageManager.getInstance(context)
				.getRecentContactsWithLastMsg();
		noticeAdapter = new WeChatAdapter(this, inviteNotices);
		xlistView.setAdapter(noticeAdapter);
		noticeAdapter.setOnClickListener(contacterOnClickJ);
		noticeAdapter.setOnLongClickListener(contacterOnLongClickJ);
	}

	public static WCApplication returnAppContex() {
		return appContext;
	}

	private void getHistoryChat() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				noticeAdapter.setNoticeList(inviteNotices);
				noticeAdapter.notifyDataSetChanged();
			}
		};
		ExecutorService singleThreadExecutor = Executors
				.newSingleThreadExecutor();
		singleThreadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				inviteNotices = MessageManager.getInstance(context)
						.getRecentContactsWithLastMsg();
				handler.sendEmptyMessage(1);
			}
		});
	}

	private void connect2xmpp() {
		indicatorImageView.startAnimation(indicatorAnimation);
		indicatorImageView.setVisibility(View.VISIBLE);
		titleBarView.setText("连线中...");
		final Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 1:
					indicatorImageView.setVisibility(View.INVISIBLE);
					indicatorImageView.clearAnimation();
					titleBarView.setText("会话");
					startService();
					break;
				case 2:
					indicatorImageView.setVisibility(View.INVISIBLE);
					indicatorImageView.clearAnimation();
					titleBarView.setText("未连接");
					Exception e = (Exception) msg.obj;
					Logger.i(e);
					break;
				default:
					break;
				}
			};
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				try {
					String password = appContext.getLoginPassword();
					String userId = appContext.getLoginUid();
					XMPPConnection connection = XmppConnectionManager
							.getInstance().getConnection();
					connection.connect();
					connection.login(userId, password, "android");
					// 离线消息处理——wr
					OfflineMessageManager offlineManager = new OfflineMessageManager(
							connection);
					try {
						Iterator<org.jivesoftware.smack.packet.Message> it = offlineManager
								.getMessages();
						while (it.hasNext()) {
							org.jivesoftware.smack.packet.Message message = it
									.next();
							String time = (System.currentTimeMillis() / 1000)
									+ "";
							Notice notice = new Notice("离线消息", Notice.CHAT_MSG,
									message.getBody(), Notice.UNREAD, message
											.getFrom(), time);
							msgReceive(notice);
						}
					} catch (Exception e) {
						// 这部分出现异常报错————wr
						// TODO: handle exception
					}
					offlineManager.deleteMessages();
					// 设置用户状态为上线
					connection
							.sendPacket(new Presence(Presence.Type.available));
					Logger.i("XMPPClient Logged in as " + connection.getUser());
					msg.what = 1;

				} catch (Exception xee) {
					if (xee instanceof XMPPException) {
						XMPPException xe = (XMPPException) xee;
						final XMPPError error = xe.getXMPPError();
						int errorCode = 0;
						if (error != null) {
							errorCode = error.getCode();
						}
						msg.what = errorCode;
						msg.obj = xee;
					}

				}
				handler.sendMessage(msg);
			}
		}).start();
	}

	@Override
	public void onBackPressed() {
		isExit();
	}

	@Override
	protected void msgReceive(final Notice notice) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				noticeAdapter.notifyDataSetChanged();
			}
		};
		ExecutorService singleThreadExecutor = Executors
				.newSingleThreadExecutor();
		singleThreadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				inviteNotices = MessageManager.getInstance(context)
						.getRecentContactsWithLastMsg();
				for (HistoryChatBean ch : inviteNotices) {
					if (ch.getFrom().equals(notice.getFrom())) {
						ch.setContent(notice.getContent());
						ch.setNoticeTime(notice.getNoticeTime());
						Integer x = ch.getNoticeSum() == null ? 0 : ch
								.getNoticeSum();
						ch.setNoticeSum(x);
					}
				}
				// -------------------------通过noticeAdapter更新notice---------------------------
				noticeAdapter.setNoticeList(inviteNotices);
				handler.sendEmptyMessage(0);
			}
		});
	}

	// /**
	// * 上面滚动条上的气泡设置 有新消息来的通知气泡，数量设置,
	// */
	// private void setPaoPao() {
	// if (null != inviteNotices && inviteNotices.size() > 0) {
	// int paoCount = 0;
	// for (HistoryChatBean c : inviteNotices) {
	// Integer countx = c.getNoticeSum();
	// paoCount += (countx == null ? 0 : countx);
	// }
	// if (paoCount == 0) {
	// // noticePaopao.setVisibility(View.GONE);
	// return;
	// }
	// Logger.i(paoCount+"");
	// // noticePaopao.setText(paoCount + "");
	// // noticePaopao.setVisibility(View.VISIBLE);
	// } else {
	// // noticePaopao.setVisibility(View.GONE);
	// }
	// }

	@Override
	protected void handReConnect(boolean isSuccess) {
		if (CommonValue.RECONNECT_STATE_SUCCESS == isSuccess) {
			titleBarView.setText("微信");

		} else if (CommonValue.RECONNECT_STATE_FAIL == isSuccess) {
			titleBarView.setText("未连接");
		}
	}

	/**
	 * 通知点击
	 */
	private OnClickListener contacterOnClickJ = new OnClickListener() {

		@Override
		public void onClick(View v) {
			HistoryChatBean notice = (HistoryChatBean) v.findViewById(R.id.des)
					.getTag();
			createChat(notice.getFrom());
			removeSingelChatPao(notice);
		}
	};

	private void removeSingelChatPao(final HistoryChatBean notice) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				noticeAdapter.notifyDataSetChanged();
			}
		};
		ExecutorService singleThreadExecutor = Executors
				.newSingleThreadExecutor();
		singleThreadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				notice.setNoticeSum(0);
				handler.sendEmptyMessage(0);
			}
		});
	}

	private OnLongClickListener contacterOnLongClickJ = new OnLongClickListener() {

		@Override
		public boolean onLongClick(View v) {
			HistoryChatBean notice = (HistoryChatBean) v.findViewById(R.id.des)
					.getTag();
			showDelChatOptionsDialog(new String[] { "删除对话" }, notice);
			return false;
		}
	};

	public void showDelChatOptionsDialog(final String[] arg,
			final HistoryChatBean notice) {
		new AlertDialog.Builder(context).setTitle(null)
				.setItems(arg, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							inviteNotices.remove(notice);
							noticeAdapter.notifyDataSetChanged();
							MessageManager.getInstance(context)
									.delChatHisWithSb(notice.getFrom());
							break;
						}
					}
				}).show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case CommonValue.REQUEST_OPEN_CHAT:
			// String to = data.getExtras().getString("to");
			// sortChat(to);
			break;

		default:
			break;
		}
	}

	private boolean isExit = false;

	private void sortChat(final String to) {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				noticeAdapter.notifyDataSetChanged();
			}
		};
		ExecutorService singleThreadExecutor = Executors
				.newSingleThreadExecutor();
		singleThreadExecutor.execute(new Runnable() {
			@Override
			public void run() {
				isExit = false;
				List<IMMessage> chats = MessageManager.getInstance(context)
						.getMessageListByFrom(to, 1, 1);
				if (chats.size() < 1) {
					return;
				}
				for (HistoryChatBean ch : inviteNotices) {
					if (ch.getFrom().equals(chats.get(0).getFromSubJid())) {
						ch.setContent(chats.get(0).getContent());
						ch.setNoticeTime(chats.get(0).getTime());
						ch.setNoticeSum(0);
						isExit = true;
					}
				}
				if (!isExit) {
					HistoryChatBean ch = new HistoryChatBean();
					ch.setFrom(chats.get(0).getFromSubJid());
					ch.setContent(chats.get(0).getContent());
					ch.setNoticeSum(0);
					ch.setTo(to);
					ch.setStatus(Notice.READ);
					ch.setNoticeType(Notice.CHAT_MSG);
					ch.setNoticeTime(chats.get(0).getTime());
					inviteNotices.add(ch);
				}
				Collections.sort(inviteNotices);
				handler.sendEmptyMessage(0);
			}
		});
	}

	@Override
	protected void msgSend(String to) {
		sortChat(to);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		swipeLayout.setRefreshing(false);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		// 无业务内容，下拉后立即停止
		swipeLayout.setRefreshing(false);

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.geomsgbtn:
			Intent intent = new Intent(WeChat.this, GeoMsgListActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
