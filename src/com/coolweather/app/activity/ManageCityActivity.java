package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.modle.ChoosedCountryList;
import com.coolweather.app.modle.ChoosedCountryListAdapter;
import com.coolweather.app.modle.CoolWeatherDB;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ManageCityActivity extends Activity implements OnClickListener {
	
	private TextView add;
	private TextView edit;
	private TextView back;
	private TextView del;
	
	private String country_name;
	private String country_code;
	private ChoosedCountryList choosedCountry;
	private ListView listView;
	private ChoosedCountryListAdapter adapter;
	private List<ChoosedCountryList> list = new ArrayList<ChoosedCountryList>();
	private List<View> viewsList = new ArrayList<View>();
	private List<ChoosedCountryList> choosedCountryList = new ArrayList<ChoosedCountryList>();
	
	//是否从weatherActivity跳转过来
	private boolean isFromWeatherActivity;
	//是否从ChooseAreaActivity跳转过来
	private boolean isFromChooseAreaActivity;
	
	//数据库
	private CoolWeatherDB coolWeatherDB;
	
	private SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.city_management_layout);
		
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		choosedCountryList.clear();
		
		adapter = new ChoosedCountryListAdapter(ManageCityActivity.this, R.layout.choosedcountrylist_layout, list);
		listView = (ListView) findViewById(R.id.choosedCountry_list);
		listView.setAdapter(adapter);
//		initCityList();
		add = (TextView) findViewById(R.id.add);
		back = (TextView) findViewById(R.id.back);
		edit = (TextView) findViewById(R.id.edit);
		del = (TextView) findViewById(R.id.del);
		add.setOnClickListener(this);
		back.setOnClickListener(this);
		edit.setOnClickListener(this);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
				String country_code = list.get(index).getCode();
				Intent intent = new Intent(ManageCityActivity.this, WeatherActivity.class);
				intent.putExtra("index", index);
				intent.putExtra("from_managecity_activity", true);
				startActivity(intent);
				finish();
			}
		});
		
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
//		isFromChooseAreaActivity = getIntent().getBooleanExtra("from_chooseArea_activity", false);
		if (isFromWeatherActivity) {
			initChoosedCountryList();
		}
	}
	
	//更新已选城市列表
	private void initChoosedCountryList() {
		choosedCountryList = coolWeatherDB.loadChoosedCountryList();
		if (choosedCountryList.size() != 0) {
			for (int i=0; i<choosedCountryList.size(); i++) {
				choosedCountry = new ChoosedCountryList();
				country_code = choosedCountryList.get(i).getCode();
				pref = getSharedPreferences(country_code,MODE_PRIVATE);
				choosedCountry.setName(choosedCountryList.get(i).getName());
				choosedCountry.setCode(country_code);
				choosedCountry.setTempLow(pref.getString("tempLow_0", ""));
				choosedCountry.setTempHigh(pref.getString("tempHigh_0", ""));
				choosedCountry.setWeather(pref.getString("weatherDesp_0", ""));
				list.add(choosedCountry);
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add:
			Intent intent_add = new Intent(this, ChooseAreaActivity.class);
			intent_add.putExtra("from_managecity_activity", true);
			startActivity(intent_add);
			finish();
			break;
		case R.id.back:
			Intent intent_back = new Intent(this, ManageCityActivity.class);
			intent_back.putExtra("from_managecity_activity", true);
			startActivity(intent_back);
			finish();
			break;
		case R.id.edit:
			if (edit.getText() == "编辑") {
				edit.setText("完成");
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
//					String country_code = list.get(index).getCode();
						coolWeatherDB.delChoosedCountry(list.get(index));
						list.remove(index);
						adapter.notifyDataSetChanged();
					}
				});
			} else {
				edit.setText("编辑");
			}
//			runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					del.setVisibility(View.VISIBLE);
//					adapter.notifyDataSetChanged();
//				}
//			});	
		default:
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		if (getIntent().getBooleanExtra("from_weather_activity", false)) {
			Intent intent = new Intent(ManageCityActivity.this, WeatherActivity.class);
			intent.putExtra("from_managecity_activity", true);
			startActivity(intent);
			finish();
		}
	}
}
