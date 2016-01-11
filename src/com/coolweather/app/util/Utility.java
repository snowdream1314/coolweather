package com.coolweather.app.util;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
	private static String xmlDatas;
	private static WeatherZhiShu weatherZhiShu;
	private static List<WeatherZhiShu> zhiShus;
	private static Weather weather;
	private static List<Weather> weatherList;
	private static List<String> weatherDatas;
//	private static String updateTime,cityName,shidu,pm25,suggest,quality,fengXiang,fengLi,
//						  tempNow,aqi,sunrise_1,sunset_1,MajorPollutants;
	//天气生活指数类
	public static class WeatherZhiShu {
		private String name;
		private String value;
		private String detail;
		
		public String getName() {
			return name;
		}
		
		public String getValue() {
			return value;
		}
		
		public String getDetail() {
			return detail;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public void setValue(String value) {
			this.value = value;
		}
		
		public void setDetail(String detail) {
			this.detail = detail;
		}
	}
	
	//天气预报类
	public static class Weather {
		private String date;
		private String high;
		private String low;
		private day day;
		private night night;
		
		public String getDate() {
			return date;
		}
		public void setDate(String date) {
			this.date = date;
		}
		
		public String getHigh() {
			return high;
		}
		public void setHigh(String high) {
			this.high = high;
		}
		
		public String getLow() {
			return low;
		}
		public void setLow(String low) {
			this.low = low;
		}
		
		public day getDay() {
			return day;
		}
		
		public night getNight() {
			return night;
		}
		
		public static class day {
			private String type;
			private String fengxiang;
			private String fengli;
			
			public String getType () {
				return type;
			}
			public void setType(String type) {
				this.type =type;
			}
			
			public String getFengXiang () {
				return fengxiang;
			}
			public void setFengXiang(String fengxiang) {
				this.fengxiang =fengxiang;
			}
			
			public String getFengLi () {
				return fengli;
			}
			public void setFengLi(String fengli) {
				this.fengli =fengli;
			}
		}
		
		public static class night {
			private String type;
			private String fengxiang;
			private String fengli;
			
			public String getType () {
				return type;
			}
			public void setType(String type) {
				this.type =type;
			}
			
			public String getFengXiang () {
				return fengxiang;
			}
			public void setFengXiang(String fengxiang) {
				this.fengxiang =fengxiang;
			}
			
			public String getFengLi () {
				return fengli;
			}
			public void setFengLi(String fengli) {
				this.fengli =fengli;
			} 
		}
	}
	
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
	
	//解析省市列表XML信息
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
				case XmlPullParser.START_TAG: 
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
				
				case XmlPullParser.END_TAG: 
					if ("c".equals(nodeName)) {
//						responseText.setText(texts);
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
	
	//解析服务器返回的XML天气数据
	public static void handleWeatherXMLResponse(Context context, String response) {
		try {
			String cityName = "";
			String updateTime="";
			String fengLi="";
			String fengXiang="";
			String tempNow = "";
			String aqi = "";
			String shidu="";
			String sunrise_1="";
			String sunset_1="";
			String pm25="";
			String suggest="";
			String quality="";
			String MajorPollutants="";
			zhiShus = new ArrayList<WeatherZhiShu>(); 
            weatherList = new ArrayList<Weather>();
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = factory.newPullParser();
			xmlPullParser.setInput(new StringReader(response));
			int eventType = xmlPullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String nodeName = xmlPullParser.getName();
				switch (eventType) {
				//开始解析XML节点
				case XmlPullParser.START_DOCUMENT:  
//	                zhiShus = new ArrayList<WeatherZhiShu>(); 
//	                weatherList = new ArrayList<Weather>();
	                weatherDatas = new ArrayList<String>();
	                break;  
				case XmlPullParser.START_TAG: 
					if ("city".equals(nodeName)) {
						cityName = xmlPullParser.nextText();
						weatherDatas.add(cityName);
					}
					if ("updatetime".equals(nodeName)) {
						updateTime = xmlPullParser.nextText();
						weatherDatas.add(updateTime);
					}
					if ("wendu".equals(nodeName)) {
						tempNow = xmlPullParser.nextText();
						weatherDatas.add(tempNow);
					}
					if ("fengli".equals(nodeName)) {
						fengLi = xmlPullParser.nextText();
						weatherDatas.add(fengLi);
					}
					if ("shidu".equals(nodeName)) {
						shidu = xmlPullParser.nextText();
						weatherDatas.add(shidu);
					}
					if ("fengxiang".equals(nodeName)) {
						fengXiang = xmlPullParser.nextText();
						weatherDatas.add(fengXiang);
					}
					if ("sunrise_1".equals(nodeName)) {
						sunrise_1 = xmlPullParser.nextText();
						weatherDatas.add(sunrise_1);
					}
					if ("sunset_1".equals(nodeName)) {
						sunset_1 = xmlPullParser.nextText();
						weatherDatas.add(sunset_1);
					}
					if ("aqi".equals(nodeName)) {
						aqi = xmlPullParser.nextText();
						weatherDatas.add(aqi);
					}
					if ("pm25".equals(nodeName)) {
						pm25 = xmlPullParser.nextText();
						weatherDatas.add(pm25);
					}
					if ("suggest".equals(nodeName)) {
						suggest = xmlPullParser.nextText();
						weatherDatas.add(suggest);
					}
					if ("quality".equals(nodeName)) {
						quality = xmlPullParser.nextText();
						weatherDatas.add(quality);
					}
					if ("MajorPollutants".equals(nodeName)) {
						MajorPollutants = xmlPullParser.nextText();
						weatherDatas.add(MajorPollutants);
					}
					
//					if ("weather".equals(nodeName)) {
//						weather =new Weather();
//					}
//					if ("date".equals(nodeName)) {
//						weather.setDate(xmlPullParser.nextText());
//					}
//					if ("high".equals(nodeName)) {
//						weather.setHigh(xmlPullParser.nextText());
//					}
//					if ("low".equals(nodeName)) {
//						weather.setLow(xmlPullParser.nextText());
//					}
//					if ("type".equals(nodeName)) {
//						weather.getDay().setType(xmlPullParser.nextText());
//					}
//					if ("fengxiang".equals(nodeName)) {
//						weather.getDay().setFengXiang(xmlPullParser.nextText());
//					}
//					if ("fengli".equals(nodeName)) {
//						weather.getDay().setFengLi(xmlPullParser.nextText());
//					}
//					if ("type".equals(nodeName)) {
//						weather.getNight().setType(xmlPullParser.nextText());
//					}
//					if ("fengxiang".equals(nodeName)) {
//						weather.getNight().setFengXiang(xmlPullParser.nextText());
//					}
//					if ("fengli".equals(nodeName)) {
//						weather.getNight().setFengLi(xmlPullParser.nextText());
//					}
					
//					if ("zhishu".equals(nodeName)) {
//						weatherZhiShu = new WeatherZhiShu();
//					}
//					if ("name".equals(nodeName)) {
//						weatherZhiShu.setName(xmlPullParser.nextText());
//					}
//					if ("value".equals(nodeName)) {
//						weatherZhiShu.setValue(xmlPullParser.nextText());
//					}
//					if ("detail".equals(nodeName)) {
//						weatherZhiShu.setDetail(xmlPullParser.nextText());
//					}
					break; 
				
				case XmlPullParser.END_TAG: 
//					if ("weather".equals(nodeName)) {
//						weatherList.add(weather);
//						weather = null;
//					}
//					if ("zhishu".equals(nodeName)) {
//						zhiShus.add(weatherZhiShu);
//						weatherZhiShu = null;
//					}
					if ("environment".equals(nodeName)) {
						//将获得的数据存入SharedPreferences
//						saveWeatherXml(context, cityName, updateTime, tempNow, fengLi, fengXiang, shidu, sunrise_1, 
//								sunset_1, aqi, pm25, suggest, quality, MajorPollutants, weatherList, zhiShus);
						saveWeatherXml(context, weatherDatas, weatherList, zhiShus);
					}
					break;
				
				default:
					break;
				}
				eventType = xmlPullParser.next();
			}
			//DOM法解析多层XML
//			DocumentBuilderFactory factoryDom = DocumentBuilderFactory.newInstance();  //取得DocumentBuilderFactory实例  
//	        DocumentBuilder builder = factoryDom.newDocumentBuilder(); //从factory获取DocumentBuilder实例  
//	        InputStream inStream = new ByteArrayInputStream(response.getBytes());
//	        Document doc = builder.parse(inStream);   //解析输入流 得到Document实例  
//	        Element rootElement = doc.getDocumentElement(); 
//	        NodeList items = rootElement.getElementsByTagName("weather");
//	        for (int i=0; i<items.getLength(); i++) {
//	        	weather =new Weather();
//	        	Element weatherNode = (Element) items.item(i);
//	        	weather.setDate(weatherNode.getFirstChild().getNodeValue());
//	        	NodeList childNodes = weatherNode.getChildNodes();
//	        	for (int j=0; j<childNodes.getLength(); j++) {
//	        		weather.setHigh(childNodes.item(1).getNodeValue());
//	        		weather.setLow(childNodes.item(2).getNodeValue());
//	        		weather.getDay().setType(childNodes.item(3).getChildNodes().item(0).getNodeValue());
//	        		weather.getDay().setFengXiang(childNodes.item(3).getChildNodes().item(1).getNodeValue());
//	        		weather.getDay().setFengLi(childNodes.item(3).getChildNodes().item(2).getNodeValue());
//	        		weather.getNight().setType(childNodes.item(4).getChildNodes().item(0).getNodeValue());
//	        		weather.getNight().setFengXiang(childNodes.item(4).getChildNodes().item(1).getNodeValue());
//	        		weather.getNight().setFengLi(childNodes.item(4).getChildNodes().item(2).getNodeValue());
//	        	}
//	        	weatherList.add(weather);
//	        }
//	        saveWeatherXml(context, weatherDatas, weatherList, zhiShus);
//	        inStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	//将服务器返回的所有XML天气信息存储到SharedPreferences文件中
//	public static void saveWeatherXml(Context context, String cityName, String updateTime, String tempNow, String fengLi, 
//			String fengXiang, String shidu, String sunrise_1, String sunset_1, String aqi, String pm25, String suggest,String quality,
//			String MajorPollutants, List<Weather> weatherList, List<WeatherZhiShu> zhiShus) {
	public static void saveWeatherXml(Context context, List<String> weatherDatas, List<Weather> weatherList, List<WeatherZhiShu> zhiShus) {	
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
//		editor.putString("city_name", weatherDatas.get(0));
		editor.putString("updatetime", weatherDatas.get(1));
//		editor.putString("tempNow", weatherDatas.get(2));
//		editor.putString("feng_li", weatherDatas.get(3));
		editor.putString("shidu", weatherDatas.get(4));
//		editor.putString("feng_xiang", weatherDatas.get(5));
		editor.putString("sunrise_1", weatherDatas.get(6));
		editor.putString("sunset_1", weatherDatas.get(7));
//		editor.putString("aqi", weatherDatas.get(8));
		editor.putString("pm25", weatherDatas.get(9));
		editor.putString("suggest", weatherDatas.get(10));
		editor.putString("quality", weatherDatas.get(11));
		editor.putString("MajorPollutants", weatherDatas.get(12));
		editor.commit();
//		editor.putString("city_name", cityName);
//		editor.putString("updatetime", updateTime);
//		editor.putString("tempNow", tempNow);
//		editor.putString("feng_li", fengLi);
//		editor.putString("feng_xiang", fengXiang);
//		editor.putString("shidu", shidu);
//		editor.putString("sunrise_1", sunrise_1);
//		editor.putString("sunset_1", sunset_1);
//		editor.putString("aqi", aqi);
//		editor.putString("pm25", pm25);
//		editor.putString("suggest", suggest);
//		editor.putString("quality", quality);
//		editor.putString("MajorPollutants", MajorPollutants);
//		for (int i=0; i<5; i++) {
//			editor.putString("date".concat("_" + Integer.toString(i)), weatherList.get(i).getDate());
//			editor.putString("highTemp".concat("_" + Integer.toString(i)), weatherList.get(i).getHigh());
//			editor.putString("lowTemp".concat("_" + Integer.toString(i)), weatherList.get(i).getLow());
//			editor.putString("dayType".concat("_" + Integer.toString(i)), weatherList.get(i).getDay().getType());
//			editor.putString("dayFengXiang".concat("_" + Integer.toString(i)), weatherList.get(i).getDay().getFengXiang());
//			editor.putString("dayFengLi".concat("_" + Integer.toString(i)), weatherList.get(i).getDay().getFengLi());
//			editor.putString("nightType".concat("_" + Integer.toString(i)), weatherList.get(i).getNight().getType());
//			editor.putString("nightFengXiang".concat("_" + Integer.toString(i)), weatherList.get(i).getNight().getFengXiang());
//			editor.putString("nightFengLi".concat("_" + Integer.toString(i)), weatherList.get(i).getNight().getFengLi());
//		}
//		for (int i=0; i<11; i++) {
//			editor.putString("weatherZhiShu_name".concat("_" + Integer.toString(i)), zhiShus.get(i).getName());
//			editor.putString("weatherZhiShu_value".concat("_" + Integer.toString(i)), zhiShus.get(i).getValue());
//			editor.putString("weatherZhiShu_detail".concat("_" + Integer.toString(i)), zhiShus.get(i).getDetail());
//		}
//		editor.commit();
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
		String fengXiang,fengLi,tempHigh,tempLow,weatherDesp,dateNow,cityName,tempNow,aqi,ganmao;
		Gson gson = new Gson();
		JsonBean jsonBean = gson.fromJson(response, JsonBean.class);
		//获取当天天气信息
		try {
			cityName = jsonBean.getData().getCity();
			tempNow = jsonBean.getData().getWenDU().concat("°");
			aqi = jsonBean.getData().getAqi();
			ganmao = jsonBean.getData().getGanMao();
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
				saveWeatherInfo(context, cityName, aqi, ganmao, tempNow, fengXiang, fengLi, tempHigh, tempLow, weatherDesp, dateNow, i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//将服务器返回的所有天气信息存储到SharedPreferences文件中
	public static void saveWeatherInfo(Context context, String cityName, String aqi, String ganmao, String tempNow, String fengXiang, String fengLi, String tempHigh, String tempLow, String weatherDesp, String dateNow, int i) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("aqi", aqi);
		editor.putString("ganmao", ganmao);
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
