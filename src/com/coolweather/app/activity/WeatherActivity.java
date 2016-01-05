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
	
	//��ʾ������
	private TextView cityNameText;
	
	//��ʾ����ʱ��
	private TextView publishText;
	
	//��ʾ����������Ϣ
	private TextView weatherDespText;
	
	//��ʾ��ǰ�¶�
	private TextView tempNow;
	
	//��ʾ���򣬷���
	private TextView fengXiang;
	private TextView fengLi;
	
	//��ʾ����1
	private TextView temp1Text;
	
	//��ʾ����2
	private TextView temp2Text;
	
	//��ʾ��ǰ����
//	private TextView currentDateText;
	private TextView currentDate;
	
	//��ʾ�������а�ť
	private Button switchCity;
	
	//��ʾ����������ť
	private Button refreshWeather;
	
	//����Ԥ��
	private TextView fore_date1,fore_date1_weather,fore_date1_temp1,fore_date1_temp2,fore_date1_fx,fore_date1_fl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		//��ʼ�����ؼ�
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		weather_forecast = (LinearLayout) findViewById(R.id.weather_forecast);
		cityNameText = (TextView) findViewById(R.id.city_name);
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
		fore_date1_fx = (TextView) findViewById(R.id.fore_date1_fx);
		fore_date1_fl = (TextView) findViewById(R.id.fore_date1_fl);
//		currentDateText = (TextView) findViewById(R.id.current_date);
		currentDate = (TextView) findViewById(R.id.date);
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		String countryCode = getIntent().getStringExtra("country_code");
		if (!TextUtils.isEmpty(countryCode)) {
			//���ؼ����ž�ȥ��ѯ����
			publishText.setText("ͬ���С�����");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherInfo(countryCode);
		} else {
			//û���ؼ����ž�ֱ����ʾ��������
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
			publishText.setText("ͬ���С�����");
//			currentDate.setVisibility(View.INVISIBLE);
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			String weatherCode = prefs.getString("weather_code", "");
			if (!TextUtils.isEmpty(weatherCode)) {
				queryWeatherInfo(weatherCode);
//			} else {
//				showWeather();
			}
			break;
		default:
			break;
		}
	}
	
	//��ѯ�ؼ���������Ӧ����������
//	private void queryWeatherCode(String countryCode) {
//		
//	}
	
	//��ѯ������������Ӧ������
	private void queryWeatherInfo(String weatherCode) {
//		String address = "http://www.weather.com.cn/adat/cityinfo/" + weatherCode + ".html";
		String address = "http://wthrcdn.etouch.cn/weather_mini?citykey=" + weatherCode;
		queryFromServer(address, "weatherCode");
	}
	
	//���ݴ���ĵ�ַ������ȥ��������ѯ�������Ż�������Ϣ
	private void queryFromServer(final String address, final String type) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				if ("weatherCode".equals(type)) {
					Utility.handleWeatherResponse(WeatherActivity.this, response);
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
						publishText.setText("ͬ��ʧ��");
					}
				});
			}
		});
	}
	
	//��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ������ʾ������
	private void showWeather() { 
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(pref.getString("city_name", ""));
		tempNow.setText(pref.getString("tempNow", ""));
		temp1Text.setText(pref.getString("temp1_0", ""));
		temp2Text.setText(pref.getString("temp2_0", ""));
		fengXiang.setText(pref.getString("feng_xiang_0", ""));
		fengLi.setText(pref.getString("feng_li_0", ""));
		weatherDespText.setText(pref.getString("weatherDesp_0", ""));
		publishText.setText("����" + pref.getString("publish_time_0", "") + "����");
		
		fore_date1.setText(pref.getString("publish_time_1", ""));
		fore_date1_weather.setText(pref.getString("weatherDesp_1", ""));
		fore_date1_temp1.setText(pref.getString("temp1_1", ""));
		fore_date1_temp2.setText(pref.getString("temp2_1", ""));
		fore_date1_fx.setText(pref.getString("feng_xiang_1", ""));
		fore_date1_fl.setText(pref.getString("feng_li_1", ""));
		
//		currentDateText.setText(pref.getString("current_date", ""));
		currentDate.setText(pref.getString("current_date", ""));
//		currentDate.setVisibility(View.VISIBLE);
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		Intent intent = new Intent(this, AutoUpdateService.class);
		startService(intent);
	}
}
