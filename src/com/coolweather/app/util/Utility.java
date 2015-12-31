package com.coolweather.app.util;

import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.coolweather.app.modle.City;
import com.coolweather.app.modle.CoolWeatherDB;
//import com.coolweather.app.modle.Country;
import com.coolweather.app.modle.Province;

public class Utility {
	
	private static String xmlData;
//	private String itemCode;
//	private static List<String> xmlDataList;
	private static String xmlDatas;
	
	//解析处理服务器返回的数据
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
					if (array[0].substring(3, 5).equals(provinceCode) && array[0].endsWith("00") && array[0].substring(5, 7).equals("01")) {
						//直辖市
						City city= new City();
						city.setCityCode(array[0]);
						city.setCityName(array[1]);
						city.setCityPyName(array[2]);
						city.setProvinceId(provinceId);
						//将解析出来的数据存储到City表
						coolWeatherDB.saveCity(city);
					} else if (array[0].substring(3, 5).equals(provinceCode) && array[0].endsWith("01")) {
						City city= new City();
						city.setCityCode(array[0]);
						city.setCityName(array[1]);
						city.setCityPyName(array[2]);
						city.setProvinceId(provinceId);
						//将解析出来的数据存储到City表
						coolWeatherDB.saveCity(city);
					} else {
						continue;
					}
				}
				return true;
			}
		}
		return false;
	}
	
//	public synchronized static boolean handleCountriesResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId, int cityId) {
//		if (xmlDatas.length() > 0) {
//			String[] items = xmlDatas.split(";");
//				if (items != null && items.length > 0) {
//					for (String item : items) {
//						String[] array = item.split(",");
//						//只处理国内的
//						if (array[0].endsWith("00")) {
//							continue;
//						}
//						if (array[0].startsWith("101") && array[0].substring(3, 5) == Integer.toString(provinceId) && array[0].substring(5, 7) == Integer.toString(cityId)) {
//							Country country= new Country();
//							country.setCountryCode(array[0].substring(7, 9));
//							country.setCountryName(array[1]);
//							country.setCountryPyName(array[2]);
//							country.setCityId(cityId);
//							//将解析出来的数据存储到City表
//							coolWeatherDB.saveCountry(country);
//						} else {
//							break;
//						}
//					}
//				}
//			return true;
//		}
//		return false;
//	}
	
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
}
