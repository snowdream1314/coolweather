package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.modle.CityList;
import com.coolweather.app.modle.CityListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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
	}
	
	private void initCityList() {
		Intent intent = new Intent(this, ChooseAreaActivity.class);
		city_name = intent.getStringExtra("country_name");
		city_code = intent.getStringExtra("country_code");
		citylist = new CityList();
		citylist.setName(city_name);
		list.add(citylist);
		adapter.notifyDataSetChanged();
		listView.setSelection(0);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_managecity_activity", true);
			startActivity(intent);
			break;
		case R.id.back:
			break;
		default:
			break;
		}
	}
}
