package com.coolweather.app.util;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.coolweather.app.modle.City;
import com.coolweather.app.modle.CoolWeatherDB;
import com.coolweather.app.modle.Country;
import com.coolweather.app.modle.Province;
import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Utility {
	
	private static String xmlData;
//	private String itemCode;
//	private static List<String> xmlDataList;
	private static String xmlDatas;
	
	//解析处理服务器返回的省级数据
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response) {
		parseXMLWithPull(response.toString());
		if (xmlDatas.length() > 0) {
			String[] items = xmlDatas.split(";");
			if (items != null && items.length > 0) {
				for (String item : items) {
					String[] array = item.split(",");
					//只处理国内的
					if (array[0].startsWith("101") && array[0].substring(5, 7).equals("01")) {
						if (array[0].endsWith("00")) {
							Province province = new Province();
							province.setProvinceCode(array[0].substring(3, 5));
							province.setProvinceName(array[3]);
							//将解析出来的数据存储到Province表
							coolWeatherDB.saveProvince(province);
						} else if (array[0].endsWith("01")) {
							Province province = new Province();
							province.setProvinceCode(array[0].substring(3, 5));
							province.setProvinceName(array[3]);
							//将解析出来的数据存储到Province表
							coolWeatherDB.saveProvince(province);
						}
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public synchronized static boolean handleCitiesResponse(CoolWeatherDB coolWeatherDB, String response, String provinceCode, int provinceId) {
		parseXMLWithPull(response.toString());
		if (xmlDatas.length() > 0) {
			String[] items = xmlDatas.split(";");
			if (items != null && items.length > 0) {
				for (String item : items) {
					String[] array = item.split(",");
					//只处理国内的
					if (array[0].substring(3).startsWith(provinceCode) && array[0].endsWith("00") && array[0].substring(5, 7).equals("01")) {
						//直辖市
						City city= new City();
						city.setCityCode(array[0]);
						city.setCityNum(array[0].substring(5, 7));
						city.setCityName(array[1]);
						city.setCityPyName(array[2]);
						city.setProvinceId(provinceId);
						//将解析出来的数据存储到City表
						coolWeatherDB.saveCity(city);
					} else if (array[0].substring(3).startsWith(provinceCode) && array[0].endsWith("01")) {
						City city= new City();
						city.setCityCode(array[0]);
						city.setCityNum(array[0].substring(5, 7));
						city.setCityName(array[1]);
						city.setCityPyName(array[2]);
						city.setProvinceId(provinceId);
						//将解析出来的数据存储到City表
						coolWeatherDB.saveCity(city);
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public synchronized static boolean handleCountriesResponse(CoolWeatherDB coolWeatherDB, String response, String cityNum, String provinceCode, int cityId) {
		if (xmlDatas.length() > 0) {
			String[] items = xmlDatas.split(";");
			if (items != null && items.length > 0) {
				for (String item : items) {
					String[] array = item.split(",");
					//只处理国内的
					if (array[0].substring(3).startsWith(provinceCode)) {
						if ( array[0].endsWith("00")) {
							Country country= new Country();
							country.setCountryCode(array[0]);
							country.setCountryName(array[1]);
							country.setCountryPyName(array[2]);
							country.setCityId(cityId);
							//将解析出来的数据存储到Country表
							coolWeatherDB.saveCountry(country);
						} else if ( array[0].substring(5).startsWith(cityNum)){
							Country country= new Country();
							country.setCountryCode(array[0]);
							country.setCountryName(array[1]);
							country.setCountryPyName(array[2]);
							country.setCityId(cityId);
							//将解析出来的数据存储到Country表
							coolWeatherDB.saveCountry(country);
						}
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public static void parseXMLWithPull(String response) {
		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = factory.newPullParser();
			xmlPullParser.setInput(new StringReader(response));
			int eventType = xmlPullParser.getEventType();
			String d1 = "";
			String d2 = "";
			String d3 = "";
			String d4 = "";
			xmlData = null;
			xmlDatas = null;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String nodeName = xmlPullParser.getName();
				switch (eventType) {
				//开始解析节点
				case XmlPullParser.START_TAG: {
					if ("d".equals(nodeName)) {
						d1 = xmlPullParser.getAttributeValue(0);
						d2 = xmlPullParser.getAttributeValue(1);
						d3 = xmlPullParser.getAttributeValue(2);
						d4 = xmlPullParser.getAttributeValue(3);
						xmlData = d1 + "," + d2 + "," + d3 + "," + d4;
						if (xmlData != null && d1.startsWith("101")) {
							xmlDatas = xmlDatas + ";" + xmlData;
						}
					}
					break;
				}
				case XmlPullParser.END_TAG: {
					if ("c".equals(nodeName)) {
//						responseText.setText(texts);
					}
				}
					break;
				default:
					break;
				}
				eventType = xmlPullParser.next();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//解析服务器返回的JSON数据，并存储到本地
//	public static void handleWeatherResponse(Context context, String response) {
//		try {
//			JSONObject jsonObject = new JSONObject(response);
//			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
//			String cityName = weatherInfo.getString("city");
//			String weatherCode = weatherInfo.getString("cityid");
//			String temp1 = weatherInfo.getString("temp1");
//			String temp2 = weatherInfo.getString("temp2");
//			String weatherDesp = weatherInfo.getString("weather");
//			String publishTime = weatherInfo.getString("ptime");
//			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}
	
	//解析服务器返回的JSON数据，并存储到本地
	public static void handleWeatherResponse(Context context, String response) {
		String fengXiang,fengLi,tempHigh,tempLow,weatherDesp,dateNow;
		Gson gson = new Gson();
		JsonBean jsonBean = gson.fromJson(response, JsonBean.class);
		//获取当天天气信息
		try {
			String cityName = jsonBean.getData().getCity();
			String tempNow = jsonBean.getData().getWenDU().concat("°");
			for (int i=0; i<5; i++) {
				fengXiang = jsonBean.getData().getForecast().get(i).getFengXiang();
				fengLi = jsonBean.getData().getForecast().get(i).getFengLi();
				if (fengLi.contains("微风")) {
					fengLi = fengLi.replace("级", "");
				}
				tempHigh = jsonBean.getData().getForecast().get(i).getHigh().replace("高温 ", "");
				tempLow = jsonBean.getData().getForecast().get(i).getLow().replace("低温 ", "");
				weatherDesp = jsonBean.getData().getForecast().get(i).getType();
				dateNow = jsonBean.getData().getForecast().get(i).getDate();
				saveWeatherInfo(context, cityName, tempNow, fengXiang, fengLi, tempHigh, tempLow, weatherDesp, dateNow, i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//获取天气预报
//		try {
//			Gson gson = new Gson();
//			JsonBean jsonBean = gson.fromJson(response, JsonBean.class);
//			String fengXiang = jsonBean.getData().getForecast().get(1).getFengXiang();
//			String fengLi = jsonBean.getData().getForecast().get(1).getFengLi();
//			String tempHigh = jsonBean.getData().getForecast().get(0).getHigh().replace("高温 ", "");
//			String tempLow = jsonBean.getData().getForecast().get(0).getLow().replace("低温 ", "");
//			String weatherDesp = jsonBean.getData().getForecast().get(0).getType();
//			String dateNow = jsonBean.getData().getForecast().get(0).getDate();
//			saveForeWeatherInfo(context, fengXiang, fengLi, tempHigh, tempLow, weatherDesp, dateNow);
//		}catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}
	
	//将服务器返回的所有天气信息存储到SharedPreferences文件中
	public static void saveWeatherInfo(Context context, String cityName, String tempNow, String fengXiang, String fengLi, String tempHigh, String tempLow, String weatherDesp, String dateNow, int i) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
//		editor.putString("weather_code", weatherCode);
		editor.putString("weather_code", "");
		editor.putString("tempNow", tempNow);
		editor.putString("temp1".concat("_" + Integer.toString(i)), tempLow);
		editor.putString("temp2".concat("_" + Integer.toString(i)), tempHigh);
		editor.putString("feng_xiang".concat("_" + Integer.toString(i)), fengXiang);
		editor.putString("feng_li".concat("_" + Integer.toString(i)), fengLi);
		editor.putString("weatherDesp".concat("_" + Integer.toString(i)), weatherDesp);
//		editor.putString("publish_time", publishTime);
		editor.putString("publish_time".concat("_" + Integer.toString(i)), dateNow);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}
}
