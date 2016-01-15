package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.modle.ChoosedCountryList;
import com.coolweather.app.modle.ChoosedCountryListAdapter;

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
	private ChoosedCountryList choosedCountryList;
	private ListView listView;
	private ChoosedCountryListAdapter adapter;
	private List<ChoosedCountryList> list = new ArrayList<ChoosedCountryList>();
//	private List<String> choosedCountryList = new ArrayList<String>();
	private List<View> viewsList = new ArrayList<View>();
	//是否从weatherActivity跳转过来
	private boolean isFromWeatherActivity;
	//是否从ChooseAreaActivity跳转过来
	private boolean isFromChooseAreaActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.city_management_layout);
		adapter = new ChoosedCountryListAdapter(ManageCityActivity.this, R.layout.choosedcountrylist_layout, list);
		listView = (ListView) findViewById(R.id.choosedCountry_list);
		listView.setAdapter(adapter);
//		initCityList();
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
		
//		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
		isFromChooseAreaActivity = getIntent().getBooleanExtra("from_chooseArea_activity", false);
		if (isFromChooseAreaActivity) {
			initChoosedCountryList();
		}
	}
	
//	@Override
//	protected void onStart() {
//		super.onStart();
//		initChoosedCountryList();
//	}
	
	//更新已选城市列表
	private void initChoosedCountryList() {
//		Intent intent = new Intent(this, ChooseAreaActivity.class);
//		choosedCountryList = (List<String>) this.getIntent().getSerializableExtra("choosedCountryList");
//		viewsList = (List<View>) this.getIntent().getSerializableExtra("viewsList");
		
//		for (int i=0; i<choosedCountryList.size(); i++) {
//			city_name = choosedCountryList.get(i);
//			city_code = choosedCountryList.get(i);
		city_name = getIntent().getStringExtra("country_name");
		city_code = getIntent().getStringExtra("country_code");
		choosedCountryList = new ChoosedCountryList();
		choosedCountryList.setName(city_name);
		choosedCountryList.setCode(city_code);
		list.add(choosedCountryList);
//		}
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
