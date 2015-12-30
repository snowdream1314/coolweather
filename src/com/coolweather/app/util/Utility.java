package com.coolweather.app.util;

import java.io.StringReader;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.coolweather.app.modle.CoolWeatherDB;
import com.coolweather.app.modle.Province;

public class Utility {
	
	private static String xmlData;
//	private String itemCode;
	private static List<String> xmlDataList;
	//解析处理服务器返回的数据
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB, String response) {
		parseXMLWithPull(response.toString());
		if (xmlDataList.size() > 0) {
			for (String item : xmlDataList) {
				if (item != null) {
					String itemCode = item.split(" ")[0];
					//只处理国内的
					if (itemCode.startsWith("101")) {
						String itemName = item.split(" ")[3];
						Province province = new Province();
						province.setProvinceCode(itemCode.substring(3, 5));
						province.setProvinceName(itemName);
						//将解析出来的数据存储到Province表
						coolWeatherDB.saveProvince(province);
					} else {
						break;
					}
				}
			}
			return true;
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
			xmlDataList = null;
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
						xmlData = d1 + " " + d2 + " " + d3 + " " + d4;
						if (xmlData != null) {
							xmlDataList.add(xmlData);
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
