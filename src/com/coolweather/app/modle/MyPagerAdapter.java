package com.coolweather.app.modle;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MyPagerAdapter extends PagerAdapter {
	
	private Button switch_city;
	private Button refersh;
	private TextView cityNameText,tempNow;
	private ArrayList<View> views;
	private List<String> datas = new ArrayList<String>();
//	private Activity activity;
	public MyPagerAdapter(ArrayList<View> views,List<String> datas) {
//		this.activity = activity;
		this.views = views;
		this.datas = datas;
	}
//	SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity.);
	//viewPager中的组件数量
	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}
	//滑动切换时销毁当前组件
	@Override
	public void destroyItem(ViewGroup arg0, int position, Object object) {
		((ViewPager) arg0).removeView(views.get(position));
	}
	//滑动时生成的组件
	@Override
	public Object instantiateItem(ViewGroup arg0, int position) {
		((ViewPager) arg0).addView(views.get(position),0);
//		switch_city = (Button) arg0.findViewById(R.id.switch_city);
//		refersh = (Button) arg0.findViewById(R.id.refresh_weather);
		cityNameText = (TextView) arg0.findViewById(R.id.city_name);
		tempNow = (TextView) arg0.findViewById(R.id.temp_now);
//		cityNameText.setText(pref.getString("city_name", ""));
		cityNameText.setText(datas.get(0));
		tempNow.setText(datas.get(1));
//		switch_city.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(activity, ManageCityActivity.class);
//				intent.putExtra("from_weather_activity", true);
//				intent.putExtra("choosedCountryList", (Serializable)choosedCountryList);
//				startActivity(intent);
//			}
//		});
//		refersh.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				publishText.setText("同步中···");
////				String weatherCode = prefs.getString("weather_code", "");
//				String weatherCode = getIntent().getStringExtra("country_code");
//				if (!TextUtils.isEmpty(weatherCode)) {
//					queryWeatherInfo(weatherCode);
//				} else {
//					showWeather();
//				}
//			}
//		});
		return views.get(position);
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public int getItemPosition(Object object) {
		return PagerAdapter.POSITION_NONE;
//		return super.getItemPosition(object);
	}
	
//	@Override
//	public CharSequence getPageTitle(int position) {
//		return titleContainer.get(position);
//	}
}
