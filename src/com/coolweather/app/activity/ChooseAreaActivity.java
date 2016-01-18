package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;


import com.coolweather.app.R;
import com.coolweather.app.modle.City;
import com.coolweather.app.modle.CoolWeatherDB;
import com.coolweather.app.modle.Country;
import com.coolweather.app.modle.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {
	
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTRY = 2;
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	private List<String> dataList = new ArrayList<String>();
	
	//ʡ�б�
	private List<Province> provinceList;
	
	//���б�
	private List<City> cityList;
	
	//���б�
	private List<Country> countryList;
	
	//ѡ�е�ʡ��
	private Province selectedProvince;
	
	//ѡ�еĳ���
	private City selectedCity;
	
	//��ǰѡ�еļ���
	private int currentLevel;
	
	//�Ƿ��weatherActivity��ת����
	private boolean isFromWeatherActivity;
	
	//�Ƿ��ManageCityActivity��ת����
	private boolean isFromManageCityActivity;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isFromWeatherActivity = getIntent().getBooleanExtra("from_weather_activity", false);
		isFromManageCityActivity = getIntent().getBooleanExtra("from_managecity_activity", false);
//		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		//�Ѿ�ѡ���˳����Ҳ��Ǵ�ManageCityActivity��ת�������Ż�ֱ������WeatherActivity
//		if (prefs.getBoolean("city_selected", false) && !isFromWeatherActivity && !isFromManageCityActivity) {
		if (coolWeatherDB.loadChoosedCountryList().size() != 0 && !isFromWeatherActivity && !isFromManageCityActivity) {
			Intent intent = new Intent(this, WeatherActivity.class);
			startActivity(intent);
			finish();
			return;
		}
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(index);
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(index);
					queryCountries();
				} else if (currentLevel == LEVEL_COUNTRY) {
					String countryName = countryList.get(index).getCountryName();
					String countryCode = countryList.get(index).getCountryCode();
					int cityId = countryList.get(index).getCityId();
					Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
//					Intent intent = new Intent(ChooseAreaActivity.this, ManageCityActivity.class);
					intent.putExtra("country_name", countryName);
					intent.putExtra("country_code", countryCode);
					intent.putExtra("from_chooseArea_activity", true);
//					intent.putExtra("city_id", cityId);
					startActivity(intent);
					finish();
				}
			}
		});
		queryProvinces();//����ʡ������
	}
	
	//��ѯȫ�����е�ʡ�����ȴ����ݿ��в�ѯ��������ݿ���û����ȥ��������ѯ
	private void queryProvinces() {
		provinceList = coolWeatherDB.loadProvinces();
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("�й�");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, "province");
		}
	}
	
	//��ѯѡ�е�ʡ�����е��У����ȴ����ݿ��в�ѯ��������ݿ���û����ȥ��������ѯ
	private void queryCities() {
		cityList = coolWeatherDB.loadCities(selectedProvince.getId());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
//			titleText.setText("�й�");
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
	}
		
	//��ѯѡ�е��������е��أ����ȴ����ݿ��в�ѯ��������ݿ���û����ȥ��������ѯ
	private void queryCountries() {
		countryList = coolWeatherDB.loadCountries(selectedCity.getId());
		if (countryList.size() > 0) {
			dataList.clear();
			for (Country country : countryList) {
				dataList.add(country.getCountryName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTRY;
		} else {
			queryFromServer(selectedCity.getCityNum(), "country");
		}
	}	
	
	//���ݴ���Ĵ��ź����ʹӷ������ϲ�ѯʡ��������
	private void queryFromServer(final String code, final String type) {
		String address;
		address = "http://mobile.weather.com.cn/js/citylist.xml";
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(String response) {
				boolean result = false;
//				provinceCode = selectedProvince.getProvinceCode();
//				Utility.parseXMLWithPull(response);
				if ("province".equals(type)) {
					result = Utility.handleProvincesResponse(coolWeatherDB, response);
				} else if ("city".equals(type)) {
					result = Utility.handleCitiesResponse(coolWeatherDB, response, code, selectedProvince.getId());
				} else if ("country".equals(type)) {
					result = Utility.handleCountriesResponse(coolWeatherDB, response, code, selectedProvince.getProvinceCode(), selectedCity.getId());
				}
				
				if (result) {
					//ͨ��runOnUiThread���������̴߳����߼�
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();
							} else if ("city".equals(type)) {
								queryCities();
							} else if ("country".equals(type)) {
								queryCountries();
							}
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				//ͨ��runOnUiThread���������̴߳����߼�
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	
	//��ʾ���ȶԻ���
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ��ء�����");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	//�رս��ȶԻ���
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	
	//����back��	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTRY) {
			queryCities();
		} else if (currentLevel == LEVEL_CITY) {
			queryProvinces();
		} else {
			if (isFromWeatherActivity) {
				Intent intent = new Intent(this, WeatherActivity.class);
				startActivity(intent);
			} else if (isFromManageCityActivity) {
				Intent intent = new Intent(this, ManageCityActivity.class);
				startActivity(intent);
			}
			finish();
		}
	}
	
}
