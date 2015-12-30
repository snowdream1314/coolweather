package com.coolweather.app.modle;

public class Country {
	
	private int id;
	private String countryName;
	private String countryPyName;
	private String countryCode;
	private int cityId;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getCountryName() {
		return countryName;
	}
	
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public String getCountryPyName() {
		return countryPyName;
	}
	
	public void setCountryPyName(String countryPyName) {
		this.countryPyName = countryPyName;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public int getCityId() {
		return cityId;
	}
	
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
}
