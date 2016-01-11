package com.coolweather.app.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.modle.CoolWeatherDB;
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
import android.view.MotionEvent;
import android.view.VelocityTracker;
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
	
	//��ʾAQI/PM25ָ��
	private TextView aqi;
	private TextView pm25;
	private TextView quality;
	
	//��ðָ��
//	private TextView suggest;
	private TextView ganMao;
	
	//��ʾ����ʱ��
	private TextView publishText;
	
	//��ʾ����������Ϣ
	private TextView weatherDespText;
	private TextView shidu;
	private TextView sunRise;
	private TextView sunSet;
	
	//��ʾ��ǰ�¶�
	private TextView tempNow;
	
	//��ʾ���򣬷���
	private TextView fengXiang;
	private TextView fengLi;
	
	//��ʾ����
	private TextView temp1Text;
	private TextView temp2Text;
	
	//��ʾ��ǰ����
//	private TextView currentDateText;
	private TextView currentDate;
	
	//��ʾ�������а�ť
	private Button switchCity;
	
	//��ʾ����������ť
	private Button refreshWeather;
	
	//����Ԥ��
	private TextView fore_date1,fore_date1_weather,fore_date1_temp1,fore_date1_temp2,fore_date1_fx,fore_date1_fl, 
					 fore_date2,fore_date2_weather,fore_date2_temp1,fore_date2_temp2,fore_date2_fx,fore_date2_fl,
					 fore_date3,fore_date3_weather,fore_date3_temp1,fore_date3_temp2,fore_date3_fx,fore_date3_fl,
					 fore_date4,fore_date4_weather,fore_date4_temp1,fore_date4_temp2,fore_date4_fx,fore_date4_fl;
	
	private SharedPreferences pref;
	private LinearLayout weather_layout;
	
	//�Ƿ��ChooseAreaActivity��ת����
	private boolean isFromChooseAreaActivity;
	
	//������ָ������Ļ�¼�����
	private float xDown;
	private float xUp;
	private float xMove;
	private int distanceX;
	private int xSpeed;
	private static final int XSPEED_MIN = 200;
	private static final int XDISTANCE_MIN = 150;
	private VelocityTracker mVelocityTracker;
	//����/�һ�������
	private int scollLeftNum;
	private int scollRightNum;
	private int choosedCountryNum;
	
	//ѡ�еĳ��м���
	private List<String> choosedCountryList = new ArrayList<String>();
	private String choosedCountryCode;
	private String choosedCountryName;
//	private Set<String> choosedCountrySet = new LinkedHashSet<String>();
	
	//���ݿ�
	private CoolWeatherDB coolWeatherDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		pref = PreferenceManager.getDefaultSharedPreferences(this);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
//		choosedCountryList = null;
//		choosedCountryNum = choosedCountryList.size();
//		scollLeftNum = 0;
//		scollRightNum = 0;
		
		//��ʼ�����ؼ�
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		weather_layout = (LinearLayout) findViewById(R.id.weather_layout);
		
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
		String countryName = getIntent().getStringExtra("country_name");
		String countryCode = getIntent().getStringExtra("country_code");
		isFromChooseAreaActivity = getIntent().getBooleanExtra("from_chooseArea_activity", false);
		
		if (!TextUtils.isEmpty(countryCode)) {
			//���ؼ����ž�ȥ��ѯ����
			publishText.setText("ͬ���С�����");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherInfo(countryCode);
//			choosedCountryList.add(countryCode);
		} else {
			//û���ؼ����ž�ֱ����ʾ��������
			showWeather();
		}
		
		//��ChooseAreaActivity��ת������Ҫ����choosedCountryList
		if (isFromChooseAreaActivity && !TextUtils.isEmpty(countryCode)) {
			if (choosedCountryList.size() == 0) {
				choosedCountryList.add(countryName);
				choosedCountryList.add(countryCode);
			} else if (!isCountryChoosed(countryCode)) {
				choosedCountryList.add(countryName);
				choosedCountryList.add(countryCode);
			}
		}
		
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		
		//������ָ������Ļ����
		weather_layout.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				createVelocityTracker(event);
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					xDown = event.getRawX();
					break;
				case MotionEvent.ACTION_MOVE:
					xMove = event.getRawX();
					distanceX = (int) (xMove-xDown);//�����ľ���
					xSpeed = getScrollVelocity();//��ȡ˳ʱ�ٶ�
					break;
				case MotionEvent.ACTION_UP:
					xUp = event.getRawX();
					if (distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN) {
						//���󻬶�
						if (xDown-xUp > 8) {
//							scollLeftNum += 1;
							choosedCountryName = cityNameText.getText().toString();
//							int choosedCountryIndex = queryChoosedCountryIndex(choosedCountryName);
//							if (choosedCountryIndex != choosedCountryList.size() && choosedCountryIndex != -1) {
//								choosedCountryCode = choosedCountryList.get(choosedCountryIndex + 1);
//								queryWeatherInfo(choosedCountryCode);
//							}
							int choosedCountryIndex = choosedCountryList.indexOf(choosedCountryName);
							if (choosedCountryIndex < choosedCountryList.size()) {
								choosedCountryCode = choosedCountryList.get(choosedCountryIndex + 3);
								queryWeatherInfo(choosedCountryCode);
							}
						}
						//���һ���
						if (xDown-xUp < 8) {
//							scollRightNum +=1;
							choosedCountryName = cityNameText.getText().toString();
//							int choosedCountryIndex = queryChoosedCountryIndex(choosedCountryName);
//							if (choosedCountryIndex != 0 && choosedCountryIndex != -1) {
//								choosedCountryCode = choosedCountryList.get(choosedCountryIndex - 1);
//								queryWeatherInfo(choosedCountryCode);
//							}
							int choosedCountryIndex = choosedCountryList.indexOf(choosedCountryName);
							if (choosedCountryIndex < 0) {
								choosedCountryCode = choosedCountryList.get(choosedCountryIndex - 1);
								queryWeatherInfo(choosedCountryCode);
							}
						}
					}
					recycleVelocityTracker();
					break;
				default:
					break;
				}
				return true;
			}
			
			//����VelocityTracker���󣬲�����������Ļ����¼����뵽VelocityTracker���С�
			private void createVelocityTracker(MotionEvent event) {
				if (mVelocityTracker == null) {
					mVelocityTracker = VelocityTracker.obtain();
				}
				mVelocityTracker.addMovement(event);
			}
			
			//����VelocityTracker����
			private void recycleVelocityTracker() {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			
			//��ȡ��ָ�������ٶ�
			private int getScrollVelocity() {
				mVelocityTracker.computeCurrentVelocity(1000);//��ʼ�����ʵ�λΪ��
				int velocity = (int) mVelocityTracker.getXVelocity();
				return Math.abs(velocity);
			}
			
		});
	}
	
	//�жϳ����Ƿ��Ѿ�ѡ���
	private boolean isCountryChoosed(String countryCode) {
		for (int i=1; i<choosedCountryList.size(); i+=2){
			String code = choosedCountryList.get(i);
			if (code.equals(countryCode)) {
				return true;
			}
		}
		return false;
	}
	
	//���ݳ������ֲ�ѯchoosedCountryList��ȡ����code
	private String queryChoosedCountryCode(String countryName) {
		for (int i=0; i<choosedCountryList.size(); i++) {
			 if (choosedCountryList.get(i).startsWith(countryName)) {
				 String choosedCountryCode = choosedCountryList.get(i).split("_")[-1];
				 return choosedCountryCode;
			 }
		}
		return null;
	}
	
	private int queryChoosedCountryIndex(String countryName) {
		for (int i=0; i<choosedCountryList.size(); i++) {
			 if (choosedCountryList.get(i).startsWith(countryName)) {
				 return i;
			 }
		}
		return -1;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.switch_city:
//			Intent intent = new Intent(this, ChooseAreaActivity.class);
			Intent intent = new Intent(this, ManageCityActivity.class);
			intent.putExtra("from_weather_activity", true);
			intent.putExtra("choosedCountryList", (Serializable)choosedCountryList);
			startActivity(intent);
//			finish();
			break;
		case R.id.refresh_weather:
			publishText.setText("ͬ���С�����");
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
	
	//��ѯ������������Ӧ������
	private void queryWeatherInfo(String countryCode) {
//		String address = "http://www.weather.com.cn/adat/cityinfo/" + weatherCode + ".html";
		String address1 = "http://wthrcdn.etouch.cn/weather_mini?citykey=" + countryCode;
//		String address = "http://wthrcdn.etouch.cn/WeatherApi?city=" + countryName;
		String address2 = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + countryCode;
		queryFromServer(address1, "weatherCode1");
		queryFromServer(address2, "weatherCode2");
	}
	
	//���ݴ���ĵ�ַ������ȥ��������ѯ�������Ż�������Ϣ
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
						publishText.setText("ͬ��ʧ��");
					}
				});
			}
		});
	}
	
	//��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ������ʾ������
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
		publishText.setText("����" + pref.getString("updatetime", "") + "����");
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
