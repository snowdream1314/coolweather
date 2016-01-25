package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.modle.ChoosedCountryList;
import com.coolweather.app.modle.ChoosedCountryListAdapter;
import com.coolweather.app.modle.SettingAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

public class SettingActivity extends Activity {
	
	private ListView listView;
	private SettingAdapter adapter;
	private List<ChoosedCountryList> list = new ArrayList<ChoosedCountryList>();
	private ChoosedCountryList setting;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_layout);
		adapter = new SettingAdapter(SettingActivity.this, R.layout.settinglist_layout, list);
		listView = (ListView) findViewById(R.id.setting_list);
		listView.setAdapter(adapter);
		initSettings();
	}
	
	private void initSettings() {
		setting = new ChoosedCountryList();
		setting.setName("推送城市");
		setting.setWeather("杭州");
		list.add(setting);
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onBackPressed() {
			Intent intent = new Intent(SettingActivity.this, ManageCityActivity.class);
			intent.putExtra("from_setting_activity", true);
			startActivity(intent);
			finish();
	}
}
