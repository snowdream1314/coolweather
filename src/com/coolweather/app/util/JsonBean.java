package com.coolweather.app.util;

import java.util.List;

public class JsonBean {
	public String desc;
	public String status;
	public Data data;
	
	public String getDesc() {
		return desc;
	}
	
	public String getStatus() {
		return status;
	}
	
	public Data getData() {
		return data;
	}
	
	
	public static class Data {
		public String wendu;
		public String ganmao;
		public List<Forecast> forecast;
		public Yesterday yesterday;
		public String aqi;
		public String city;
		
		public String getWenDU() {
			return wendu;
		}
		
		public String getGanMao() {
			return ganmao;
		}
		
		public List<Forecast> getForecast() {
			return forecast;
		}
		
		public Yesterday getYesterday() {
			return yesterday;
		}
		
		public String getAqi() {
			return aqi;
		}
		
		public String getCity() {
			return city;
		}
	}
	
	public static class Forecast {
		public String fengxiang;
		public String fengli;
		public String high;
		public String type;
		public String low;
		public String date;
		
		public String getFengXiang() {
			return fengxiang;
		}
		
		public String getFengLi() {
			return fengli;
		}
		
		public String getHigh() {
			return high;
		}
		
		public String getType() {
			return type;
		}
		
		public String getLow() {
			return low;
		}
		
		public String getDate() {
			return date;
		}
	}
	
	public static class Yesterday {
		public String fl;
		public String fx;
		public String high;
		public String type;
		public String low;
		public String date;
		
		public String getFl() {
			return fl;
		}
		
		public String getFx() {
			return fx;
		}
		
		public String getHigh() {
			return high;
		}
		
		public String getType() {
			return type;
		}
		
		public String getLow() {
			return low;
		}
		
		public String getDate() {
			return date;
		}
	}
	
}
