package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.modle.CityList;
import com.coolweather.app.modle.CityListAdapter;

import android.app.Activity;
import android.content.Intent;
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
//	private TextView edit;
	private TextView back;
	private String city_name;
	private String city_code;
	private CityList citylist;
	private ListView listView;
	private CityListAdapter adapter;
	private List<CityList> list = new ArrayList<CityList>();
	private List<String> choosedCountryList = new ArrayList<String>();
	
	//是否从weatherActivity跳转过来
	private boolean isFromWeatherActivity;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.city_management_layout);
		adapter = new CityListAdapter(ManageCityActivity.this, R.layout.citylist_layout, list);
		listView = (ListView) findViewById(R.id.city_list);
		listView.setAdapter(adapter);
		initCityList();
		add = (TextView) findViewById(R.id.add);
		back = (TextView) findViewById(R.id.back);
		add.setOnClickListener(this);
		back.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
				String country_code = list.get(index).getCode();
				Intent intent = new Intent(ManageCityActivity.this, WeatherActivity.class);
				intent.putExtra("country_code", country_code);
				startActivity(intent);
				finish();
			}
		});
		
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
		if (isFromWeatherActivity) {
			initCityList();
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		initCityList();
	}
	
	//更新已选城市列表
	private void initCityList() {
//		Intent intent = new Intent(this, ChooseAreaActivity.class);
		choosedCountryList = (List<String>) this.getIntent().getSerializableExtra("choosedCountryList");
		for (int i=0; i<choosedCountryList.size(); i+=2) {
			city_name = choosedCountryList.get(i);
			city_code = choosedCountryList.get(i+1);
			citylist = new CityList();
			citylist.setName(city_name);
			citylist.setCode(city_code);
			list.add(citylist);
		}
		adapter.notifyDataSetChanged();
//		listView.setSelection(0);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_managecity_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.back:
			break;
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
