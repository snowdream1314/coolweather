package com.coolweather.app.activity;

//import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

//import org.xmlpull.v1.XmlPullParser;
//import org.xmlpull.v1.XmlPullParserFactory;

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
import android.os.Bundle;
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
	private static String provinceCode;
//	private static String xmlData;
//	private static String xmlDatas;
	
	//省列表
	private List<Province> provinceList;
	
	//市列表
	private List<City> cityList;
	
	//县列表
	private List<Country> countryList;
	
	//选中的省份
	private Province selectedProvince;
	
	//选中的城市
	private City selectedCity;
	
	//当前选中的级别
	private int currentLevel;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index, long arg3) {
				if (currentLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(index);
					queryCities();
				} else if (currentLevel == LEVEL_CITY) {
					selectedCity = cityList.get(index);
					queryCountries();
				}
			}
		});
		queryProvinces();//加载省级数据
	}
	
	//查询全国所有的省，优先从数据库中查询，如果数据库中没有则去服务器查询
	private void queryProvinces() {
		provinceList = coolWeatherDB.loadProvinces();
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("中国");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, "province");
		}
	}
	
	//查询选中的省内所有的市，优先从数据库中查询，如果数据库中没有则去服务器查询
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
//			titleText.setText("中国");
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
	}
		
	//查询选中的市内所有的县，优先从数据库中查询，如果数据库中没有则去服务器查询
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
	
	//根据传入的代号和类型从服务器上查询省市县数据
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
					//通过runOnUiThread方法回主线程处理逻辑
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();
							} else if ("city".equals(type)) {
								queryCities();
//							} else if ("country".equals(type)) {
//								queryCountries();
							}
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				//通过runOnUiThread方法回主线程处理逻辑
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}
	
	//显示进度对话框
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("正在加载···");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	
	//关闭进度对话框
	private void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	
	//捕获back键	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTRY) {
			queryCities();
		} else if (currentLevel == LEVEL_CITY) {
			queryProvinces();
		} else {
			finish();
		}
	}
	
//	public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, String provinceCode, int provinceId) {
//		parseXMLWithPull(response.toString());
//		if (xmlDatas.length() > 0) {
//			String[] items = xmlDatas.split(";");
//			if (items != null && items.length > 0) {
//				for (String item : items) {
//					String[] array = item.split(",");
//					//只处理国内的
//					if (array[0].substring(3, 5).equals(provinceCode) && array[0].endsWith("00") && array[0].substring(5, 7).equals("01")) {
//						//直辖市
//						City city= new City();
//						city.setCityCode(array[0]);
//						city.setCityName(array[1]);
//						city.setCityPyName(array[2]);
//						city.setProvinceId(provinceId);
//						//将解析出来的数据存储到City表
//						coolWeatherDB.saveCity(city);
//					} else if (array[0].substring(3, 5).equals(provinceCode) && array[0].endsWith("01")) {
//						City city= new City();
//						city.setCityCode(array[0]);
//						city.setCityName(array[1]);
//						city.setCityPyName(array[2]);
//						city.setProvinceId(provinceId);
//						//将解析出来的数据存储到City表
//						coolWeatherDB.saveCity(city);
//					}
//				}
//				return true;
//			}
//		}
//		return false;
//	}
//	
//	public static void parseXMLWithPull(String response) {
//		try {
//			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//			XmlPullParser xmlPullParser = factory.newPullParser();
//			xmlPullParser.setInput(new StringReader(response));
//			int eventType = xmlPullParser.getEventType();
//			String d1 = "";
//			String d2 = "";
//			String d3 = "";
//			String d4 = "";
//			xmlData = null;
//			xmlDatas = null;
//			while (eventType != XmlPullParser.END_DOCUMENT) {
//				String nodeName = xmlPullParser.getName();
//				switch (eventType) {
//				//开始解析节点
//				case XmlPullParser.START_TAG: {
//					if ("d".equals(nodeName)) {
//						d1 = xmlPullParser.getAttributeValue(0);
//						d2 = xmlPullParser.getAttributeValue(1);
//						d3 = xmlPullParser.getAttributeValue(2);
//						d4 = xmlPullParser.getAttributeValue(3);
//						xmlData = d1 + "," + d2 + "," + d3 + "," + d4;
//						if (xmlData != null && d1.startsWith("101")) {
//							xmlDatas = xmlDatas + ";" + xmlData;
//						}
//					}
//					break;
//				}
//				case XmlPullParser.END_TAG: {
//					if ("c".equals(nodeName)) {
////						responseText.setText(texts);
//					}
//				}
//					break;
//				default:
//					break;
//				}
//				eventType = xmlPullParser.next();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
