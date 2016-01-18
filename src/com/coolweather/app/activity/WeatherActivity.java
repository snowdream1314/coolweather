package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.R;
import com.coolweather.app.modle.ChoosedCountryList;
import com.coolweather.app.modle.CoolWeatherDB;
import com.coolweather.app.modle.MyPagerAdapter;
import com.coolweather.app.service.AutoUpdateService;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity {
	
	private LinearLayout weatherInfoLayout, weather_forecast, airQuality;
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
	private TextView tempLowText;
	private TextView tempHighText;
	
	//显示当前日期
//	private TextView currentDateText;
	private TextView currentDate;
	
	//显示更换城市按钮
	private Button switchCity;
	
	//显示更新天气按钮
	private Button refreshWeather;
	
	//天气预报
	private TextView fore_date1,fore_date1_weather,fore_date1_tempLow,fore_date1_tempHigh,fore_date1_fx,fore_date1_fl, 
					 fore_date2,fore_date2_weather,fore_date2_tempLow,fore_date2_tempHigh,fore_date2_fx,fore_date2_fl,
					 fore_date3,fore_date3_weather,fore_date3_tempLow,fore_date3_tempHigh,fore_date3_fx,fore_date3_fl,
					 fore_date4,fore_date4_weather,fore_date4_tempLow,fore_date4_tempHigh,fore_date4_fx,fore_date4_fl;
	
	private SharedPreferences pref;
	private LinearLayout weather_layout;
	
	//是否从ChooseAreaActivity跳转过来
	private boolean isFromChooseAreaActivity;
	
	//是否从ManageCityActivity跳转过来
	private boolean isFromManageCityActivity;
	
	private boolean isFirstStartActivity;
	
	//监听手指滑动屏幕事件参数
//	private float xDown;
//	private float xUp;
//	private float xMove;
//	private int distanceX;
//	private int xSpeed;
//	private static final int XSPEED_MIN = 200;
//	private static final int XDISTANCE_MIN = 150;
//	private VelocityTracker mVelocityTracker;
//	//向左/右滑动次数
//	private int scollLeftNum;
//	private int scollRightNum;
//	private int choosedCountryNum;
	
	//ViewPager
	private ViewPager viewPager;
	private MyPagerAdapter vpAdapter;
	private ArrayList<View> views;
	//选中的城市集合
	private ChoosedCountryList choosedCountry;
	private List<ChoosedCountryList> choosedCountryList = new ArrayList<ChoosedCountryList>();
	private int index;
	
	//数据库
	private CoolWeatherDB coolWeatherDB;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.view_pager);
		
		views = new ArrayList<View>();
//		pref = PreferenceManager.getDefaultSharedPreferences(this);
		
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		choosedCountryList.clear();
		index = 0;
		
		//初始化各控件
//		airQuality = (LinearLayout) findViewById(R.id.air_quality);
		cityNameText = (TextView) findViewById(R.id.city_name);
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		
		choosedCountryList = coolWeatherDB.loadChoosedCountryList();
		if (choosedCountryList.size() != 0) {
			index = choosedCountryList.size() - 1;
			for (int i=0; i<choosedCountryList.size(); i++) {
				initViewPager();
			}
			viewPager.setCurrentItem(index);
		} else {
			String countryName = getIntent().getStringExtra("country_name");
			String countryCode = getIntent().getStringExtra("country_code");
//			isFromChooseAreaActivity = getIntent().getBooleanExtra("from_chooseArea_activity", false);
			//从ChooseAreaActivity跳转过来
			if (!TextUtils.isEmpty(countryCode)) {
				queryWeatherInfo(countryCode);
				pref = getSharedPreferences(countryCode,MODE_PRIVATE);
				choosedCountry = new ChoosedCountryList();
				choosedCountry.setCode(countryCode);
				choosedCountry.setName(countryName);
				choosedCountry.setTempLow(pref.getString("tempLow_0", ""));
				choosedCountry.setTempHigh(pref.getString("tempHigh_0", ""));
				choosedCountry.setWeather(pref.getString("weatherDesp_0", ""));
				coolWeatherDB.saveChoosedCountry(choosedCountry);
				choosedCountryList = coolWeatherDB.loadChoosedCountryList();
				initViewPager();
				viewPager.setCurrentItem(index);
				isFirstStartActivity = true;
			}
		}
		
		//启动后台自动更新天气服务
		Intent intent = new Intent(this, AutoUpdateService.class);
		startService(intent);
		
		//设置手指滑动屏幕监听
//		weather_layout.setOnTouchListener(new View.OnTouchListener() {
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
//				createVelocityTracker(event);
//				switch (event.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//					xDown = event.getRawX();
//					break;
//				case MotionEvent.ACTION_MOVE:
//					xMove = event.getRawX();
//					distanceX = (int) (xMove-xDown);//滑动的距离
//					xSpeed = getScrollVelocity();//获取顺时速度
//					break;
//				case MotionEvent.ACTION_UP:
//					xUp = event.getRawX();
//					if (distanceX > XDISTANCE_MIN && xSpeed > XSPEED_MIN) {
//						//向左滑动
//						if (xDown-xUp > 8) {
////							scollLeftNum += 1;
//							choosedCountryName = cityNameText.getText().toString();
////							}
//							int choosedCountryIndex = choosedCountryList.indexOf(choosedCountryName);
//							if (choosedCountryIndex < choosedCountryList.size()) {
//								choosedCountryCode = choosedCountryList.get(choosedCountryIndex + 3);
//								queryWeatherInfo(choosedCountryCode);
//							}
//						}
//						//向右滑动
//						if (xDown-xUp < 8) {
////							scollRightNum +=1;
//							choosedCountryName = cityNameText.getText().toString();
//							int choosedCountryIndex = choosedCountryList.indexOf(choosedCountryName);
//							if (choosedCountryIndex < 0) {
//								choosedCountryCode = choosedCountryList.get(choosedCountryIndex - 1);
//								queryWeatherInfo(choosedCountryCode);
//							}
//						}
//					}
//					recycleVelocityTracker();
//					break;
//				default:
//					break;
//				}
//				return true;
//			}
//			
//			//创建VelocityTracker对象，并将触摸界面的滑动事件加入到VelocityTracker当中。
//			private void createVelocityTracker(MotionEvent event) {
//				if (mVelocityTracker == null) {
//					mVelocityTracker = VelocityTracker.obtain();
//				}
//				mVelocityTracker.addMovement(event);
//			}
//			
//			//回收VelocityTracker对象
//			private void recycleVelocityTracker() {
//				mVelocityTracker.recycle();
//				mVelocityTracker = null;
//			}
//			
//			//获取手指滑动的速度
//			private int getScrollVelocity() {
//				mVelocityTracker.computeCurrentVelocity(1000);//初始化速率单位为秒
//				int velocity = (int) mVelocityTracker.getXVelocity();
//				return Math.abs(velocity);
//			}
//			
//		});
	}
	
	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		View view = LayoutInflater.from(this).inflate(R.layout.weather_layout, null);
		views.add(view);
		viewPager.setAdapter(new PagerAdapter() {
			//viewPager中的组件数量
			@Override
			public int getCount() {
				return views.size();
			}
			//滑动切换时销毁当前组件
			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				((ViewPager) container).removeView(views.get(position));
			}
			//滑动时生成的组件
			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				((ViewPager) container).addView(views.get(position));
				final int view_position = position;
				getPositionViews(position);
				queryWeatherInfo(choosedCountryList.get(position).getCode());
				showWeather(choosedCountryList.get(position).getCode());
				switchCity.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(WeatherActivity.this, ManageCityActivity.class);
						intent.putExtra("from_weather_activity", true);
						startActivity(intent);
						finish();
					}
				});
				refreshWeather.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						publishText.setText("同步中···");
						String weatherCode = choosedCountryList.get(view_position).getCode();
						if (!TextUtils.isEmpty(weatherCode)) {
							queryWeatherInfo(weatherCode);
							showWeather(weatherCode);
						} else {
							showWeather(weatherCode);
						}
					}
				});
				return views.get(position);
			}
			
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}
			
			@Override
			public int getItemPosition(Object object) {
				return POSITION_NONE;
//				return super.getItemPosition(object);
			}
			
		});
//		viewPager.setCurrentItem(index);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			//当滑动状态改变时调用
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
          //当前页被滑动时调用
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
          //当新页面被选中时调用
            @Override
            public void onPageSelected(int arg0) {
            }
		});
	}
	
	//获取position位置各个实例
	private void getPositionViews(int position) {
//		cityNameText = (TextView) views.get(position).findViewById(R.id.city_name);
		aqi = (TextView) views.get(position).findViewById(R.id.aqi);
		pm25 = (TextView) views.get(position).findViewById(R.id.pm25);
		publishText = (TextView) views.get(position).findViewById(R.id.publish_text);
		weatherDespText = (TextView) views.get(position).findViewById(R.id.weather_desp);
		tempLowText = (TextView) views.get(position).findViewById(R.id.tempLow);
		tempHighText = (TextView) views.get(position).findViewById(R.id.tempHigh);
		tempNow = (TextView) views.get(position).findViewById(R.id.temp_now);
		fengXiang = (TextView) views.get(position).findViewById(R.id.feng_xiang);
		fengLi = (TextView) views.get(position).findViewById(R.id.feng_li);
		shidu = (TextView) views.get(position).findViewById(R.id.shidu_value);
		quality = (TextView) views.get(position).findViewById(R.id.quality);
		sunRise = (TextView) views.get(position).findViewById(R.id.sunrise_time);
		sunSet = (TextView) views.get(position).findViewById(R.id.sunset_time);
		ganMao = (TextView) views.get(position).findViewById(R.id.ganmao);
		
		fore_date1 = (TextView) views.get(position).findViewById(R.id.fore_date1);
		fore_date1_weather = (TextView) views.get(position).findViewById(R.id.fore_date1_weather);
		fore_date1_tempLow = (TextView) views.get(position).findViewById(R.id.fore_date1_tempLow);
		fore_date1_tempHigh = (TextView) views.get(position).findViewById(R.id.fore_date1_tempHigh);
//		fore_date1_fx = (TextView) findViewById(R.id.fore_date1_fx);
		fore_date1_fl = (TextView) views.get(position).findViewById(R.id.fore_date1_fl);
		
		fore_date2 = (TextView) views.get(position).findViewById(R.id.fore_date2);
		fore_date2_weather = (TextView) views.get(position).findViewById(R.id.fore_date2_weather);
		fore_date2_tempLow = (TextView) views.get(position).findViewById(R.id.fore_date2_tempLow);
		fore_date2_tempHigh = (TextView) views.get(position).findViewById(R.id.fore_date2_tempHigh);
//		fore_date2_fx = (TextView) findViewById(R.id.fore_date2_fx);
		fore_date2_fl = (TextView) views.get(position).findViewById(R.id.fore_date2_fl);
		
		fore_date3 = (TextView) views.get(position).findViewById(R.id.fore_date3);
		fore_date3_weather = (TextView) views.get(position).findViewById(R.id.fore_date3_weather);
		fore_date3_tempLow = (TextView) views.get(position).findViewById(R.id.fore_date3_tempLow);
		fore_date3_tempHigh = (TextView) views.get(position).findViewById(R.id.fore_date3_tempHigh);
//		fore_date3_fx = (TextView) findViewById(R.id.fore_date3_fx);
		fore_date3_fl = (TextView) views.get(position).findViewById(R.id.fore_date3_fl);
		
		fore_date4 = (TextView) views.get(position).findViewById(R.id.fore_date4);
		fore_date4_weather = (TextView) views.get(position).findViewById(R.id.fore_date4_weather);
		fore_date4_tempLow = (TextView) views.get(position).findViewById(R.id.fore_date4_tempLow);
		fore_date4_tempHigh = (TextView) views.get(position).findViewById(R.id.fore_date4_tempHigh);
//		fore_date4_fx = (TextView) findViewById(R.id.fore_date4_fx);
		fore_date4_fl = (TextView) views.get(position).findViewById(R.id.fore_date4_fl);
		
//		currentDateText = (TextView) findViewById(R.id.current_date);
		currentDate = (TextView) views.get(position).findViewById(R.id.date);
//		switchCity = (Button) views.get(position).findViewById(R.id.switch_city);
//		refreshWeather = (Button) views.get(position).findViewById(R.id.refresh_weather);
	}
	
	//判断城市是否已经选择过
	private boolean isCountryChoosed(String countryCode) {
		for (int i=0; i<choosedCountryList.size(); i++){
			String code = choosedCountryList.get(i).getCode();
			if (code.equals(countryCode)) {
				return true;
			}
		}
		return false;
	}
	
	//根据城市名字查询choosedCountryList获取城市code
//	private String queryChoosedCountryCode(String countryName) {
//		for (int i=0; i<choosedCountryList.size(); i++) {
//			 if (choosedCountryList.get(i).startsWith(countryName)) {
//				 String choosedCountryCode = choosedCountryList.get(i).split("_")[-1];
//				 return choosedCountryCode;
//			 }
//		}
//		return null;
//	}
	
//	private int queryChoosedCountryIndex(String countryName) {
//		for (int i=0; i<choosedCountryList.size(); i++) {
//			 if (choosedCountryList.get(i).startsWith(countryName)) {
//				 return i;
//			 }
//		}
//		return -1;
//	}
	
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.switch_city:
////			Intent intent = new Intent(this, ChooseAreaActivity.class);
//			Intent intent = new Intent(this, ManageCityActivity.class);
//			intent.putExtra("from_weather_activity", true);
//			intent.putExtra("choosedCountryList", (Serializable)choosedCountryList);
//			startActivity(intent);
////			finish();
//			break;
//		case R.id.refresh_weather:
//			publishText.setText("同步中···");
////			String weatherCode = prefs.getString("weather_code", "");
//			String weatherCode = getIntent().getStringExtra("country_code");
//			if (!TextUtils.isEmpty(weatherCode)) {
//				queryWeatherInfo(weatherCode);
//			} else {
//				showWeather();
//			}
//			break;
//		default:
//			break;
//		}
//	}
	
	//查询天气代号所对应的天气
	private void queryWeatherInfo(String countryCode) {
		pref = getSharedPreferences(countryCode,MODE_PRIVATE);
//		String address = "http://www.weather.com.cn/adat/cityinfo/" + weatherCode + ".html";
		String address1 = "http://wthrcdn.etouch.cn/weather_mini?citykey=" + countryCode;
//		String address = "http://wthrcdn.etouch.cn/WeatherApi?city=" + countryName;
		String address2 = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + countryCode;
		queryFromServer(address1, "weatherCode1", pref);
		queryFromServer(address2, "weatherCode2", pref);
	}
	
	//根据传入的地址和类型去服务器查询天气代号或天气信息
	private void queryFromServer(final String address, final String type, final SharedPreferences pref) {
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				if ("weatherCode1".equals(type)) {
					Utility.handleWeatherResponse(WeatherActivity.this, response, pref);
				}
				if ("weatherCode2".equals(type)) {
					Utility.handleWeatherXMLResponse(WeatherActivity.this, response, pref);
				}
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						showWeather();
//					}
//				});	
				
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
	private void showWeather(String countryCode) { 
//		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		pref = getSharedPreferences(countryCode, MODE_PRIVATE); 
		
		cityNameText.setText(pref.getString("city_name", ""));
		aqi.setText(pref.getString("aqi", ""));
		pm25.setText(pref.getString("pm25", ""));
		fengXiang.setText(pref.getString("feng_xiang_now", ""));
		fengLi.setText(pref.getString("feng_li_now", ""));
		tempNow.setText(pref.getString("tempNow", ""));
		shidu.setText(pref.getString("shidu", ""));
		quality.setText(pref.getString("quality", ""));
		sunRise.setText(pref.getString("sunrise_1", ""));
		sunSet.setText(pref.getString("sunset_1", ""));
		ganMao.setText(pref.getString("ganmao", ""));
		weatherDespText.setText(pref.getString("weatherDesp_0", ""));
		publishText.setText("今天" + pref.getString("updatetime", "") + "发布");
		tempLowText.setText(pref.getString("tempLow_0", ""));
		tempHighText.setText(pref.getString("tempHigh_0", ""));
		if (pref.getString("aqi", "") == "") {
			aqi.setVisibility(View.INVISIBLE);
			pm25.setVisibility(View.INVISIBLE);
			quality.setVisibility(View.INVISIBLE);
		}
		
		fore_date1.setText(pref.getString("publish_time_1", ""));
		fore_date1_weather.setText(pref.getString("weatherDesp_1", ""));
		fore_date1_tempLow.setText(pref.getString("tempLow_1", ""));
		fore_date1_tempHigh.setText(pref.getString("tempHigh_1", ""));
//		fore_date1_fx.setText(pref.getString("feng_xiang_1", ""));
		fore_date1_fl.setText(pref.getString("feng_li_1", ""));
		
		fore_date2.setText(pref.getString("publish_time_2", ""));
		fore_date2_weather.setText(pref.getString("weatherDesp_2", ""));
		fore_date2_tempLow.setText(pref.getString("tempLow_2", ""));
		fore_date2_tempHigh.setText(pref.getString("tempHigh_2", ""));
//		fore_date2_fx.setText(pref.getString("feng_xiang_2", ""));
		fore_date2_fl.setText(pref.getString("feng_li_2", ""));
		
		fore_date3.setText(pref.getString("publish_time_3", ""));
		fore_date3_weather.setText(pref.getString("weatherDesp_3", ""));
		fore_date3_tempLow.setText(pref.getString("tempLow_3", ""));
		fore_date3_tempHigh.setText(pref.getString("tempHigh_3", ""));
//		fore_date3_fx.setText(pref.getString("feng_xiang_3", ""));
		fore_date3_fl.setText(pref.getString("feng_li_3", ""));
		
		fore_date4.setText(pref.getString("publish_time_4", ""));
		fore_date4_weather.setText(pref.getString("weatherDesp_4", ""));
		fore_date4_tempLow.setText(pref.getString("tempLow_4", ""));
		fore_date4_tempHigh.setText(pref.getString("tempHigh_4", ""));
//		fore_date4_fx.setText(pref.getString("feng_xiang_4", ""));
		fore_date4_fl.setText(pref.getString("feng_li_4", ""));
		
		currentDate.setText(pref.getString("current_date", ""));
//		weatherInfoLayout.setVisibility(View.VISIBLE);
//		cityNameText.setVisibility(View.VISIBLE);
//		Intent intent = new Intent(this, AutoUpdateService.class);
//		startService(intent);
//		viewPager.getAdapter().notifyDataSetChanged();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if (!isFirstStartActivity) {
			isFromChooseAreaActivity = getIntent().getBooleanExtra("from_chooseArea_activity", false);
			isFromManageCityActivity = getIntent().getBooleanExtra("from_managecity_activity", false);
			//添加新城市
			if (isFromChooseAreaActivity) {
				//从ChooseAreaActivity跳转过来
				String countryName = getIntent().getStringExtra("country_name");
				String countryCode = getIntent().getStringExtra("country_code");
				if (!isCountryChoosed(countryCode)) {
//					queryWeatherInfo(countryCode);
//					pref = getSharedPreferences(countryCode,MODE_PRIVATE);
					choosedCountry = new ChoosedCountryList();
					choosedCountry.setCode(countryCode);
					choosedCountry.setName(countryName);
//					choosedCountry.setTempLow(pref.getString("tempLow_0", ""));
//					choosedCountry.setTempHigh(pref.getString("tempHigh_0", ""));
//					choosedCountry.setWeather(pref.getString("weatherDesp_0", ""));
					coolWeatherDB.saveChoosedCountry(choosedCountry);
					choosedCountryList = coolWeatherDB.loadChoosedCountryList();
					index = choosedCountryList.size() - 1;
					initViewPager();
					viewPager.setCurrentItem(index);
				}
			} else if (isFromManageCityActivity) {
				//不是添加新城市
//				choosedCountryList = coolWeatherDB.loadChoosedCountryList();
//				if (choosedCountryList.size() != 0) {
//					views.clear();
//					for (int i=0; i<choosedCountryList.size(); i++) {
//						initViewPager();
//					}
//				}
				if (isFromManageCityActivity) {
					index = getIntent().getIntExtra("index", 0);
					viewPager.setCurrentItem(index);
				}
			}
			isFirstStartActivity = false;
		}
	}

}
