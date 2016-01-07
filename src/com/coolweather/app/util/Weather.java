package com.coolweather.app.util;

public class Weather {
	private String city;
	private String updatetime;
	private String wendu;
	private String fengli;
	private String shidu;
	private String fengxiang;
	private String sunrise_1;
	private String sunset_1;
	
	private environment environment;
	private yesterday yesterday;
	private forecast forecast;
	private zhishus zhishus;
	
	public environment getEnvironment() {
		return environment;
	}
	
	public yesterday getYesterday() {
		return yesterday;
	}
	
	public forecast getForecast() {
		return forecast;
	}
	
	public zhishus getZhiShus() {
		return zhishus;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getUpdateTime() {
		return updatetime;
	}
	public void setUpdateTime(String updatetime) {
		this.updatetime = updatetime;
	}
	
	public String getWenDu() {
		return wendu;
	}
	public void setWenDu(String wendu) {
		this.wendu = wendu;
	}
	
	public String getFengLi() {
		return fengli;
	}
	public void setFengLi(String fengli) {
		this.fengli = fengli;
	}
	
	public String getShiDu() {
		return shidu;
	}
	public void setShiDu(String shidu) {
		this.shidu = shidu;
	}
	
	public String getFengXiang() {
		return fengxiang;
	}
	public void setFengXiang(String fengxiang) {
		this.fengxiang = fengxiang;
	}
	
	public String getSunRise_1() {
		return sunrise_1;
	}
	public void setSunRise_1(String sunrise_1) {
		this.sunrise_1 = sunrise_1;
	}
	
	public String getSunSet_1() {
		return sunset_1;
	}
	public void setSunSet_1(String sunset_1) {
		this.sunset_1 = sunset_1;
	}
	
	public static class environment {
		private String aqi;
		private String pm25;
		private String suggest;
		private String quality;
		private String MajorPollutants;
		private String pm10;
		
		public String getAqi() {
			return aqi;
		}
		public void setAqi(String aqi) {
			this.aqi = aqi;
		}
		
		public String getPm25() {
			return pm25;
		}
		public void setPm25(String pm25) {
			this.pm25 = pm25;
		}
		
		public String getSuggest() {
			return suggest;
		}
		public void setSuggest(String suggest) {
			this.suggest = suggest;
		}
		
		public String getQuality() {
			return quality;
		}
		public void setQuality(String quality) {
			this.quality = quality;
		}
		
		public String getMajorPollutants() {
			return MajorPollutants;
		}
		public void setMajorPollutants(String MajorPollutants) {
			this.MajorPollutants = MajorPollutants;
		}
		
		public String getPm10() {
			return pm10;
		}
		public void setPm10(String pm10) {
			this.pm10 = pm10;
		}
	}
	
	public static class yesterday {
		private String date_1;
		private String high_1;
		private String low_1;
		
		private day_1 day_1;
		private night_1 night_1;
		
		public String getDate_1() {
			return date_1;
		}
		public void setDate_1(String date_1) {
			this.date_1 = date_1;
		}
		
		public String getHigh_1() {
			return high_1;
		}
		public void setHigh_1(String high_1) {
			this.high_1 = high_1;
		}
		
		public String getLow_1() {
			return low_1;
		}
		public void setLow_1(String low_1) {
			this.low_1 = low_1;
		}
		
		public day_1 getDay_1() {
			return day_1;
		}
		
		public night_1 getNight_1() {
			return night_1;
		}
		public static class day_1 {
			private String type_1;
			private String fx_1;
			private String fl_1;
			
			public String getType_1(){
				return type_1;
			}
			public void setType_1(String type_1) {
				this.type_1 = type_1;
			}
			
			public String getFx_1(){
				return fx_1;
			}
			public void setFx_1(String fx_1) {
				this.fx_1 = fx_1;
			}
			
			public String getFl_1(){
				return fl_1;
			}
			public void setFl_1(String fl_1) {
				this.fl_1 = fl_1;
			}
		}
		
		public static class night_1 {
			private String type_1;
			private String fx_1;
			private String fl_1;
			
			public String getType_1(){
				return type_1;
			}
			public void setType_1(String type_1) {
				this.type_1 = type_1;
			}
			
			public String getFx_1(){
				return fx_1;
			}
			public void setFx_1(String fx_1) {
				this.fx_1 = fx_1;
			}
			
			public String getFl_1(){
				return fl_1;
			}
			public void setFl_1(String fl_1) {
				this.fl_1 = fl_1;
			}
		}
	}
	
	public static class forecast {
		private weather weather;
		
		public weather getWeather() {
			return weather;
		}
		public static class weather {
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
	}
	
	public static class zhishus {
		private zhishu zhishu;
		
		public zhishu getZhiShu () {
			return zhishu;
		}
		
		public static class zhishu {
			private String name;
			private String value;
			private String detail;
			
			public String getName () {
				return name;
			}
			public void setName(String name) {
				this.name =name;
			}
			
			public String getValue () {
				return value;
			}
			public void setValue(String value) {
				this.value =value;
			}
			
			public String getDetail () {
				return detail;
			}
			public void setDetail(String detail) {
				this.detail =detail;
			}
		}
	}
}
