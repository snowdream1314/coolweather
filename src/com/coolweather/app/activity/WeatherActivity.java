package com.coolweather.app.activity;

import com.coolweather.app.R;
import com.coolweather.app.service.AutoUpdateService;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener{
	
	private LinearLayout weatherInfoLayout, weather_forecast;
	
	//显示城市名
	private TextView cityNameText;
	
	//显示AQI/PM@%指数
	private TextView aqi;
	private TextView pm25;
	
	//显示发布时间
	private TextView publishText;
	
	//显示天气描述信息
	private TextView weatherDespText;
	
	//显示当前温度
	private TextView tempNow;
	
	//显示风向，风力
	private TextView fengXiang;
	private TextView fengLi;
	
	//显示气温1
	private TextView temp1Text;
	
	//显示气温2
	private TextView temp2Text;
	
	//显示当前日期
//	private TextView currentDateText;
	private TextView currentDate;
	
	//显示更换城市按钮
	private Button switchCity;
	
	//显示更新天气按钮
	private Button refreshWeather;
	
	//天气预报
	private TextView fore_date1,fore_date1_weather,fore_date1_temp1,fore_date1_temp2,fore_date1_fx,fore_date1_fl, 
					 fore_date2,fore_date2_weather,fore_date2_temp1,fore_date2_temp2,fore_date2_fx,fore_date2_fl,
					 fore_date3,fore_date3_weather,fore_date3_temp1,fore_date3_temp2,fore_date3_fx,fore_date3_fl,
					 fore_date4,fore_date4_weather,fore_date4_temp1,fore_date4_temp2,fore_date4_fx,fore_date4_fl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		//初始化各控件
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
//		weather_forecast = (LinearLayout) findViewById(R.id.weather_forecast);
		cityNameText = (TextView) findViewById(R.id.city_name);
		aqi = (TextView) findViewById(R.id.aqi);
		pm25 = (TextView) findViewById(R.id.pm25);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		tempNow = (TextView) findViewById(R.id.temp_now);
		fengXiang = (TextView) findViewById(R.id.feng_xiang);
		fengLi = (TextView) findViewById(R.id.feng_li);
		
		fore_date1 = (TextView) findViewById(R.id.fore_date1);
		fore_date1_weather = (TextView) findViewById(R.id.fore_date1_weather);
		fore_date1_temp1 = (TextView) findViewById(R.id.fore_date1_temp1);
		fore_date1_temp2 = (TextView) findViewById(R.id.fore_date1_temp2);
//		fore_date1_fx = (TextView) findViewById(R.id.fore_date1_fx);
		fore_date1_fl = (TextView) findViewById(R.id.fore_date1_fl);
		
		fore_date2 = (TextView) findViewById(R.id.fore_date2);
		fore_date2_weather = (TextView) findViewById(R.id.fore_date2_weather);
		fore_date2_temp1 = (TextView) findViewById(R.id.fore_date2_temp1);
		fore_date2_temp2 = (TextView) findViewById(R.id.fore_date2_temp2);
//		fore_date2_fx = (TextView) findViewById(R.id.fore_date2_fx);
		fore_date2_fl = (TextView) findViewById(R.id.fore_date2_fl);
		
		fore_date3 = (TextView) findViewById(R.id.fore_date3);
		fore_date3_weather = (TextView) findViewById(R.id.fore_date3_weather);
		fore_date3_temp1 = (TextView) findViewById(R.id.fore_date3_temp1);
		fore_date3_temp2 = (TextView) findViewById(R.id.fore_date3_temp2);
//		fore_date3_fx = (TextView) findViewById(R.id.fore_date3_fx);
		fore_date3_fl = (TextView) findViewById(R.id.fore_date3_fl);
		
		fore_date4 = (TextView) findViewById(R.id.fore_date4);
		fore_date4_weather = (TextView) findViewById(R.id.fore_date4_weather);
		fore_date4_temp1 = (TextView) findViewById(R.id.fore_date4_temp1);
		fore_date4_temp2 = (TextView) findViewById(R.id.fore_date4_temp2);
//		fore_date4_fx = (TextView) findViewById(R.id.fore_date4_fx);
		fore_date4_fl = (TextView) findViewById(R.id.fore_date4_fl);
		
//		currentDateText = (TextView) findViewById(R.id.current_date);
		currentDate = (TextView) findViewById(R.id.date);
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		String countryCode = getIntent().getStringExtra("country_code");
		if (!TextUtils.isEmpty(countryCode)) {
			//有县级代号就去查询天气
			publishText.setText("同步中···");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherInfo(countryCode);
		} else {
			//没有县级代号就直接显示本地天气
			showWeather();
		}
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switch_city:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("同步中···");
//			currentDate.setVisibility(View.INVISIBLE);
//			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//			String weatherCode = prefs.getString("weather_code", "");
			String weatherCode = getIntent().getStringExtra("country_code");
			if (!TextUtils.isEmpty(weatherCode)) {
				queryWeatherInfo(weatherCode);
			} else {
				showWeather();
			}
			break;
		default:
			break;
		}
	}
	
	//查询县级代号所对应的天气代号
//	private void queryWeatherCode(String countryCode) {
//		
//	}
	
	//查询天气代号所对应的天气
	private void queryWeatherInfo(String weatherCode) {
//		String address = "http://www.weather.com.cn/adat/cityinfo/" + weatherCode + ".html";
//		String address = "http://wthrcdn.etouch.cn/weather_mini?citykey=" + weatherCode;
		String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + weatherCode;
		queryFromServer(address, "weatherCode");
	}
	
	//根据传入的地址和类型去服务器查询天气代号或天气信息
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				if ("weatherCode".equals(type)) {
//					Utility.handleWeatherResponse(WeatherActivity.this, response);
					Utility.handleWeatherXMLResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showWeather();
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						publishText.setText("同步失败");
					}
				});
			}
		});
	}
	
	//从SharedPreferences文件中读取存储的天气信息，并显示到界面
	private void showWeather() { 
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(pref.getString("city_name", ""));
		aqi.setText(pref.getString("aqi", ""));
		pm25.setText(pref.getString("pm25", ""));
		fengXiang.setText(pref.getString("feng_xiang_0", ""));
		fengLi.setText(pref.getString("feng_li_0", ""));
		tempNow.setText(pref.getString("tempNow", ""));
//		temp1Text.setText(pref.getString("temp1_0", ""));
//		temp2Text.setText(pref.getString("temp2_0", ""));
//		weatherDespText.setText(pref.getString("weatherDesp_0", ""));
//		publishText.setText("今天" + pref.getString("publish_time_0", "") + "发布");
		temp1Text.setText(pref.getString("lowTemp_0", ""));
		temp2Text.setText(pref.getString("highTemp_0", ""));
		weatherDespText.setText(pref.getString("dayType_0", ""));
		publishText.setText("今天" + pref.getString("updatetime", "") + "发布");
		
		fore_date1.setText(pref.getString("publish_time_1", ""));
		fore_date1_weather.setText(pref.getString("weatherDesp_1", ""));
		fore_date1_temp1.setText(pref.getString("temp1_1", ""));
		fore_date1_temp2.setText(pref.getString("temp2_1", ""));
//		fore_date1_fx.setText(pref.getString("feng_xiang_1", ""));
		fore_date1_fl.setText(pref.getString("feng_li_1", ""));
		
		fore_date2.setText(pref.getString("publish_time_2", ""));
		fore_date2_weather.setText(pref.getString("weatherDesp_2", ""));
		fore_date2_temp1.setText(pref.getString("temp1_2", ""));
		fore_date2_temp2.setText(pref.getString("temp2_2", ""));
//		fore_date2_fx.setText(pref.getString("feng_xiang_2", ""));
		fore_date2_fl.setText(pref.getString("feng_li_2", ""));
		
		fore_date3.setText(pref.getString("publish_time_3", ""));
		fore_date3_weather.setText(pref.getString("weatherDesp_3", ""));
		fore_date3_temp1.setText(pref.getString("temp1_3", ""));
		fore_date3_temp2.setText(pref.getString("temp2_3", ""));
//		fore_date3_fx.setText(pref.getString("feng_xiang_3", ""));
		fore_date3_fl.setText(pref.getString("feng_li_3", ""));
		
		fore_date4.setText(pref.getString("publish_time_4", ""));
		fore_date4_weather.setText(pref.getString("weatherDesp_4", ""));
		fore_date4_temp1.setText(pref.getString("temp1_4", ""));
		fore_date4_temp2.setText(pref.getString("temp2_4", ""));
//		fore_date4_fx.setText(pref.getString("feng_xiang_4", ""));
		fore_date4_fl.setText(pref.getString("feng_li_4", ""));
		
//		currentDateText.setText(pref.getString("current_date", ""));
		currentDate.setText(pref.getString("current_date", ""));
//		currentDate.setVisibility(View.VISIBLE);
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		Intent intent = new Intent(this, AutoUpdateService.class);
		startService(intent);
	}
}
