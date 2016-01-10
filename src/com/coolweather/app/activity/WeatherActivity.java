package com.coolweather.app.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
	
	//显示AQI/PM25指数
	private TextView aqi;
	private TextView pm25;
	private TextView quality;
	
	//感冒指数
//	private TextView suggest;
	private TextView ganMao;
	
	//显示发布时间
	private TextView publishText;
	
	//显示天气描述信息
	private TextView weatherDespText;
	private TextView shidu;
	private TextView sunRise;
	private TextView sunSet;
	
	//显示当前温度
	private TextView tempNow;
	
	//显示风向，风力
	private TextView fengXiang;
	private TextView fengLi;
	
	//显示气温
	private TextView temp1Text;
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
	
	private SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
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
		shidu = (TextView) findViewById(R.id.shidu_value);
		quality = (TextView) findViewById(R.id.quality);
		sunRise = (TextView) findViewById(R.id.sunrise_time);
		sunSet = (TextView) findViewById(R.id.sunset_time);
		ganMao = (TextView) findViewById(R.id.ganmao);
		
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
//		String countryName = getIntent().getStringExtra("country_name");
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
//			Intent intent = new Intent(this, ChooseAreaActivity.class);
			Intent intent = new Intent(this, ManageCityActivity.class);
//			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("同步中···");
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
	
	//查询天气代号所对应的天气
	private void queryWeatherInfo(String countryCode) {
//		String address = "http://www.weather.com.cn/adat/cityinfo/" + weatherCode + ".html";
		String address1 = "http://wthrcdn.etouch.cn/weather_mini?citykey=" + countryCode;
//		String address = "http://wthrcdn.etouch.cn/WeatherApi?city=" + countryName;
		String address2 = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + countryCode;
		queryFromServer(address1, "weatherCode1");
		queryFromServer(address2, "weatherCode2");
	}
	
	//根据传入的地址和类型去服务器查询天气代号或天气信息
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				if ("weatherCode1".equals(type)) {
					Utility.handleWeatherResponse(WeatherActivity.this, response);
				}
				if ("weatherCode2".equals(type)) {
					Utility.handleWeatherXMLResponse(WeatherActivity.this, response);
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showWeather();
					}
				});	
				
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
//		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		
		cityNameText.setText(pref.getString("city_name", ""));
		aqi.setText(pref.getString("aqi", ""));
		pm25.setText(pref.getString("pm25", ""));
		fengXiang.setText(pref.getString("feng_xiang_0", ""));
		fengLi.setText(pref.getString("feng_li_0", ""));
		tempNow.setText(pref.getString("tempNow", ""));
		shidu.setText(pref.getString("shidu", ""));
		quality.setText(pref.getString("quality", ""));
		sunRise.setText(pref.getString("sunrise_1", ""));
		sunSet.setText(pref.getString("sunset_1", ""));
		ganMao.setText(pref.getString("ganmao", ""));
		weatherDespText.setText(pref.getString("weatherDesp_0", ""));
		publishText.setText("今天" + pref.getString("updatetime", "") + "发布");
		temp1Text.setText(pref.getString("temp1_0", ""));
		temp2Text.setText(pref.getString("temp2_0", ""));
		
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
		
		currentDate.setText(pref.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		Intent intent = new Intent(this, AutoUpdateService.class);
		startService(intent);
	}
	
}
