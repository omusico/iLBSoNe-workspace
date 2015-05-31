package com.ubirtls.view.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ubirtls.Controller;
import com.ubirtls.R;
import com.ubirtls.util.DatabaseManager;
import com.ubirtls.util.OverlayItem;
import com.ubirtls.util.POI;

import coordinate.TwoDCoordinate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 搜索结果界面，显示所有搜索到的结果。
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class SearchResultActivity extends Activity implements OnClickListener, DatabaseConstants{
	/** 搜索到的结果数量 */
	private int searchResultCount;
	/** 整个界面是一个ListView控件 */
	private ListView listView;
	/** listView对应的内容 */
	private List<Map<String, Object>> content;
	/** 返回按钮，返回上一个界面 */
	private Button backButton;
	/**ButtonBar 包括设置起点 终点等选项*/
	private LinearLayout buttonBar;
	/**当前被选中的Item*/
	private View currentItem = null;
	/**当前被选中的item位置*/
	private int position = -1;
	/**数据库管理类*/
	private DatabaseManager databaseManager = null;
	private ArrayList<POI> pois = null;
	/* 在Activity首次创建时调用 */
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* 自定义标题栏 */
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.search_result_list);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.search_result_titlebar);
		/*创建数据库和POI表格*/
		databaseManager = new DatabaseManager(this);
		databaseManager.createPoiTable();
		/* 初始化UI对象 */
		buttonBar = (LinearLayout)findViewById(R.id.search_result_button_bar);
		((Button)findViewById(R.id.search_result_start_point_button)).setOnClickListener(this);
		((Button)findViewById(R.id.search_result_end_point_button)).setOnClickListener(this);
		((Button)findViewById(R.id.search_result_collect_button)).setOnClickListener(this);
		((Button)findViewById(R.id.search_result_show_on_button)).setOnClickListener(this);
		backButton = (Button) findViewById(R.id.backbutton);
		backButton.setOnClickListener(this);
		TextView searchResultContentView = (TextView) findViewById(R.id.result_size);
		searchResultContentView.setText("7条记录");
		listView = (ListView) findViewById(R.id.search_result_listview);
        /*查询到的结果*/
		String[] names = new String[] { "教一楼", "教二楼", "教三楼", "52#", "14#",
				"学一食堂","北区体育馆"};
		TwoDCoordinate[] coors = new TwoDCoordinate[] {
				new TwoDCoordinate(2192, 2504), new TwoDCoordinate(1327, 3868),
				new TwoDCoordinate(1669, 2905), new TwoDCoordinate(883, 2323),
				new TwoDCoordinate(3822, 2333), new TwoDCoordinate(571, 3327),
				new TwoDCoordinate(2323, 5852)};
		pois = new ArrayList<POI>();
		for(int i =0; i <= 6; i++){
			pois.add(new POI(names[i], coors[i]));
			/*将这些点作为历史点插入数据库*/
			databaseManager.insertHistoricalPoi(pois.get(i));
		}
		/* 图标和字符串标题映射 */
		content = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < 7; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("poi", pois.get(i).getDescription());
			map.put("coor", pois.get(i).getCoordinate().toString());
			contents.add(map);
			}
		/* 将映射添加到Adapter */
		SimpleAdapter adapter = new SimpleAdapter(this,
				(List<Map<String, Object>>) contents,
				R.layout.search_result_listi_tem,
				new String[] { "poi", "coor" }, new int[] {
						R.id.search_result_list_poi,
						R.id.search_result_list_coor });
		listView.setAdapter(adapter);
		
		// 设置监听
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View item,
					int position, long arg3) {
				/*选中某项Item*/
				if(currentItem != null)
					currentItem.setBackgroundColor(Color.TRANSPARENT);
				SearchResultActivity.this.position = position;
				currentItem = item;
				currentItem.setBackgroundColor(Color.BLUE);
				buttonBar.setVisibility(View.VISIBLE);
			}
		});
	}

	/* 在Activity创建后或重启后调用 */
	public void onStart() {
		super.onStart();
	}

	/* 在Activity暂停时调用 */
	protected void onPause() {
		super.onPause();
	}

	/* onStart后会调用 */
	protected void onResume() {
		super.onResume();
	}

	/* 在Activity 销毁时调用 */
	protected void onDestroy() {
		super.onDestroy();
		if (content != null)
			content.clear();
		if(databaseManager != null)
			databaseManager.close();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == backButton.getId()) {
			SearchResultActivity.this.finish();
		}
		//设置起点
		else if(v.getId() == R.id.search_result_start_point_button){
			
		}
		//设置终点
		else if(v.getId() == R.id.search_result_end_point_button){
			
		}
		//收藏点
		else if(v.getId() == R.id.search_result_collect_button){
			databaseManager.insertCollectedPoi(pois.get(position));
			Toast.makeText(this, pois.get(position).description+"已收藏", 1000).show();
		}
		//在地图上显示点
		else if(v.getId() == R.id.search_result_show_on_button){
			/* 标记相应的位置 */
			Controller.getInstance().addMarkerItem(
					new OverlayItem(pois.get(position).getDescription(), pois.get(position).getCoordinate(), null));
			
			// 跳转到MapActivity界面 
			Intent intent = new Intent(SearchResultActivity.this,
					MapActivity.class);
			startActivity(intent);

			SearchResultActivity.this.finish();
			// 跟踪位置 
			Controller.getInstance().followPosition(
					pois.get(position).getCoordinate());
		}
	}
}
