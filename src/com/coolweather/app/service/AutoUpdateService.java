package com.coolweather.app.service;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.modle.ChoosedCountryList;
import com.coolweather.app.modle.CoolWeatherDB;
import com.coolweather.app.receiver.AutoUpdateReceiver;
import com.coolweather.app.util.ContextUtil;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;

public class AutoUpdateService extends Service {
	
	private CoolWeatherDB coolWeatherDB;
//	private Context context;
	private List<ChoosedCountryList> choosedCountryList = new ArrayList<ChoosedCountryList>();
	private SharedPreferences pref;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public int onStartCommand(final Intent intent, int flags, int startId) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				updateWeather();
			}
		}).start();
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 8 * 60 * 60 * 1000; //8小时毫秒数
		long triggerAtTimes = SystemClock.elapsedRealtime() + anHour;
		Intent i = new Intent(this, AutoUpdateReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTimes, pi);
		return super.onStartCommand(intent, flags, startId);
	}
	
	//更新天气
	private void updateWeather() {
//		context = ContextUtil.getInstance();
		coolWeatherDB = CoolWeatherDB.getInstance(AutoUpdateService.this);
		choosedCountryList = coolWeatherDB.loadChoosedCountryList();
		for (int i=0; i<choosedCountryList.size(); i++) {
			String countryCode = choosedCountryList.get(i).getCode();
			pref = getSharedPreferences(countryCode,MODE_PRIVATE);
			String address1 = "http://wthrcdn.etouch.cn/weather_mini?citykey=" + countryCode;
			String address2 = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + countryCode;
			updateWeatherFromServer(address1, "weatherCode1", pref);
			updateWeatherFromServer(address2, "weatherCode1", pref);
		}
	}
	
	private void updateWeatherFromServer(final String address, final String type, final SharedPreferences pref) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				if ("weatherCode1".equals(type)) {
					Utility.handleWeatherResponse(AutoUpdateService.this, response, pref);
				}
				if ("weatherCode2".equals(type)) {
					Utility.handleWeatherXMLResponse(AutoUpdateService.this, response, pref);
				}
			}
			
			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}
		});
	}
}
