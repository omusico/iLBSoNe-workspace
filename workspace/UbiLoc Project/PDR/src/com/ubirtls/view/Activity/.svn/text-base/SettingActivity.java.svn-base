/**
 * 
 */
package com.ubirtls.view.Activity;

import java.util.Locale;

import com.ubirtls.Controller;
import com.ubirtls.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

/**
 *应用程序设置界面 继承了PreferenceActivity
 * 
 * @author 胡旭科
 * @version 1.0
 */
public class SettingActivity extends PreferenceActivity implements
OnPreferenceChangeListener, OnPreferenceClickListener {
	/**
	 * 定位开关CheckBoxPreference key
	 */
	private String locationSwitchPrefKey;

	/**
	 * 扫描间隔ListPreference key
	 */
	private String scanIntervalPrefKey;

	/**
	 * 删除地图数据key
	 */
	private String deleteMapDataKey;

	/**
	 * 語言選擇key
	 */
	private String languageKey;
	
	/**
	 * 显示模式key
	 */
	private String showModeKey;
	/**
	 * 通信设置key
	 */
	private String commKey;
	/**
	 * 切换地图key
	 */
	private String changeMapKey;
	/**
	 * 关于key
	 */
	private String aboutKey;
	/**
	 * 定位开关CheckBoxPreference
	 */
	private CheckBoxPreference locationSwitchPref;

	/**
	 * 扫描间隔ListPreference
	 */
	private ListPreference scanIntervalPre;

	/**
	 * 删除地图数据pre
	 */
	private PreferenceScreen deleteMapPre;
	
	/**
	 * 語言選擇pre
	 */
	private ListPreference languagePre;
	/**
	 * 通信设置pre
	 */
	private PreferenceScreen commPre;
	
	/**
	 * 显示模式选择pre
	 */
	private ListPreference showModePre;
	/**
	 * 切换地图pre
	 */
	private ListPreference changeMapPre;

	/**
	 * 关于软件pre
	 */
	private PreferenceScreen aboutPre;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 从xml文件中添加Preference项
		addPreferencesFromResource(R.xml.setting_preference);

		/*初始化key*/
		locationSwitchPrefKey = getResources().getString(
				R.string.setting_location_key);
		scanIntervalPrefKey = getResources().getString(
				R.string.setting_scan_interval_key);
		deleteMapDataKey = getResources().getString(
				R.string.setting_delete_map_key);
		languageKey = getResources().getString(
				R.string.setting_language_key);
		showModeKey = getResources().getString(
				R.string.setting_showmode_key);
		aboutKey = getResources().getString(
				R.string.setting_about_key);
		changeMapKey = getResources().getString(
				R.string.setting_change_map_key);
		commKey = getResources().getString(
				R.string.setting_comm_key);
		
		/*初始化preference*/
		locationSwitchPref = (CheckBoxPreference) findPreference(locationSwitchPrefKey);
		scanIntervalPre = (ListPreference) findPreference(scanIntervalPrefKey);
		deleteMapPre = (PreferenceScreen)findPreference(deleteMapDataKey);
		languagePre = (ListPreference) findPreference(languageKey);
		showModePre = (ListPreference) findPreference(showModeKey);
		aboutPre = (PreferenceScreen)findPreference(aboutKey);
		changeMapPre = (ListPreference) findPreference(changeMapKey);
		commPre = (PreferenceScreen)findPreference(commKey);
		
		/*初始化地图切换的数据*/
		String[] maps = Controller.getInstance().getMapList();
		//地图目录列表不为空
		if(maps != null){
			changeMapPre.setEntries(maps);
			changeMapPre.setEntryValues(maps);
		}

		// 设置Preference监听
		locationSwitchPref.setOnPreferenceChangeListener(this);
		locationSwitchPref.setOnPreferenceClickListener(this);
		scanIntervalPre.setOnPreferenceChangeListener(this);
		scanIntervalPre.setOnPreferenceClickListener(this);
		deleteMapPre.setOnPreferenceClickListener(this);
		languagePre.setOnPreferenceChangeListener(this);
		languagePre.setOnPreferenceClickListener(this);
		showModePre.setOnPreferenceChangeListener(this);
		aboutPre.setOnPreferenceClickListener(this);
		commPre.setOnPreferenceClickListener(this);
		changeMapPre.setOnPreferenceClickListener(this);
		changeMapPre.setOnPreferenceChangeListener(this);
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// 定位开关
		if (preference.getKey() == locationSwitchPrefKey) {
/*			if ((Boolean) newValue)
				Controller.getInstance().startScan();
			else {
				// 在关闭定位时需要等待线程，所以在这里使用了一个进度条对话框
				final ProgressDialog dialog;
				dialog = ProgressDialog.show(SettingActivity.this,
						getResources().getString(
								R.string.register_progress_title),
						getResources().getString(
								R.string.location_off_progress_message), true);
				// 并启动一个线程，线程结束时结束进度条对话框
				new Thread() {
					public void run() {
						Controller.getInstance().closeCommunication();
						Controller.getInstance().stopScan();
						if (dialog.isShowing())
							dialog.dismiss();
					}
				}.start();
			}
*/		}
		// 扫描间隔
		else if (preference.getKey() == scanIntervalPrefKey) {
/*			Controller.getInstance().closeCommunication();
			Controller.getInstance().setScanInterval(
					new Integer((String) newValue) * 1000);
*/		}
		/* 语言选择 */
		else if (preference.getKey() == languageKey) {
			Resources resources = getResources();// 获得res资源对象

			Configuration config = resources.getConfiguration();// 获得设置对象

			DisplayMetrics dm = resources.getDisplayMetrics();// 获得屏幕参数：主要是分辨率，像素等。

			if (Integer.valueOf((String) newValue) == 1) {
				config.locale = Locale.SIMPLIFIED_CHINESE; // 简体中文
				resources.updateConfiguration(config, dm);
			} else if (Integer.valueOf((String) newValue) == 2) {
				config.locale = Locale.TRADITIONAL_CHINESE; // 中文繁体
				resources.updateConfiguration(config, dm);
			} else {
				config.locale = Locale.ENGLISH; // 英语
				resources.updateConfiguration(config, dm);
			}
			/* 刷新屏幕 */
			Intent intent = new Intent();
			intent.setClass(SettingActivity.this, SettingActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			SettingActivity.this.startActivity(intent);
		}
		/* 显示模式 */
		else if (preference.getKey() == showModeKey) {
			setBrightness(90);
		}
		//地图切换
		else if(preference.getKey() == changeMapKey){
			if(!Controller.getInstance().changeMap((String) newValue)){
				Toast.makeText(this, R.string.change_map_fail, 1000).show();
			}
			else
				Toast.makeText(this, R.string.change_map_success, 1000).show();

		}
		return true;
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		// TODO Auto-generated method stub
		//删除地图缓存数据
		if (preference.getKey() == deleteMapDataKey) {
			Dialog dialog = new AlertDialog.Builder(SettingActivity.this).setTitle(getResources().getString(R.string.setting_delete_map_data_title))
			.setMessage(getResources().getString(R.string.setting_delete_map_data_message))
			.setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Controller.getInstance().deleteLocalMap();
				}
			}).setNegativeButton(getResources().getString(R.string.cancel), null).create();
			dialog.show();
		}
		/* 软件关于 */
		else if (preference.getKey() == aboutKey) {
			new AlertDialog.Builder(this).setIcon(R.drawable.logo).setTitle(
					R.string.setting_about_dialog_title).setMessage(
					R.string.setting_about_dialog_message).setPositiveButton(R.string.OK, null)
					.show();
		}
		/*通信设置*/
		else if(preference.getKey() == commKey){
			Intent intent = new Intent();
			intent.setClass(SettingActivity.this, CommSettingActivity.class);
			SettingActivity.this.startActivity(intent);
		}
		/*地图切换*/
		else if(preference.getKey() == changeMapKey){

		}
		return true;
	}
	
	public void setBrightness(int level) { 
		Window window = getWindow(); 
		LayoutParams attributes = window.getAttributes(); 
		float flevel = level; 
		attributes.screenBrightness = flevel / 255f; 
		getWindow().setAttributes(attributes); 
	} 

}
