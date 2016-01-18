package com.coolweather.app.modle;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.db.CoolWeatherOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {
	
	//���ݿ���
	public static final String DB_NAME = "cool_weather";
	
	//���ݿ�汾
	public static final int VERSION = 1;
	
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;
	
	//���췽��˽�л�
	private CoolWeatherDB(Context context) {
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	//��ȡCoolWeatherDB��ʵ��,synchronized �ؼ�������������ͷ������ߴ�������,��������һ����������һ��������ʱ��ͬһʱ�����ֻ��һ���߳�ִ����δ���.
	public synchronized static CoolWeatherDB getInstance(Context context) {
		if (coolWeatherDB == null) {
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	//��Provinceʵ���洢�����ݿ�
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
//			Cursor cursor = db.query("Province", null, "province_name=?", new String[] {province.getProvinceName()}, null, null, null);
//			if (cursor.getCount() == 0) {
			db.insert("Province", null, values);
//			}
		}
	}
	
	//�����ݿ��ȡȫ������ʡ����Ϣ
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	//��Cityʵ���洢�����ݿ�
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_pyname",city.getCityPyName());
			values.put("city_code", city.getCityCode());
			values.put("city_num", city.getCityNum());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}
	
	//�����ݿ��ȡĳʡ�µ����г�����Ϣ
	public List<City> loadCities(int provinceId) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id= ?", new String[] {Integer.toString(provinceId)}, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setCityNum(cursor.getString(cursor.getColumnIndex("city_num")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setProvinceId(provinceId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	//��Countryʵ���洢�����ݿ�
	public void saveCountry(Country country) {
		if (country != null) {
			ContentValues values = new ContentValues();
			values.put("country_name", country.getCountryName());
			values.put("country_pyname", country.getCountryPyName());
			values.put("country_code", country.getCountryCode());
			values.put("city_id", country.getCityId());
			db.insert("Country", null, values);
		}
	}
	
	//�����ݿ��ȡĳ�����µ���������Ϣ
	public List<Country> loadCountries(int cityId) {
		List<Country> list = new ArrayList<Country>();
		Cursor cursor = db.query("Country", null, "city_id= ?", new String[] {String.valueOf(cityId)}, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Country country = new Country();
				country.setId(cursor.getInt(cursor.getColumnIndex("id")));
				country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
				country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
				country.setCityId(cityId);
				list.add(country);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	//��ChoosedCountryʵ���洢�����ݿ�
	public void saveChoosedCountry(ChoosedCountryList choosedCountryList) {
		if (choosedCountryList != null) {
			ContentValues values = new ContentValues();
			values.put("choosedcountry_name", choosedCountryList.getName());
			values.put("choosedcountry_code", choosedCountryList.getCode());
			values.put("choosedcountry_tempLow", choosedCountryList.getTempLow());
			values.put("choosedcountry_tempHigh", choosedCountryList.getTempHigh());
			values.put("choosedcountry_weather", choosedCountryList.getWeather());
			values.put("choosedcountry_imageID", choosedCountryList.getImageId());
			db.insert("ChoosedCountry", null, values);
		}
	}
	
	//�����ݿ��ȡChoosedCountry���е�����
	public List<ChoosedCountryList> loadChoosedCountryList() {
		List<ChoosedCountryList> list = new ArrayList<ChoosedCountryList>();
		Cursor cursor = db.query("ChoosedCountry", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				ChoosedCountryList ChoosedCountryList = new ChoosedCountryList();
				ChoosedCountryList.setCode(cursor.getString(cursor.getColumnIndex("choosedcountry_code")));
				ChoosedCountryList.setName(cursor.getString(cursor.getColumnIndex("choosedcountry_name")));
				ChoosedCountryList.setTempLow(cursor.getString(cursor.getColumnIndex("choosedcountry_tempLow")));
				ChoosedCountryList.setTempHigh(cursor.getString(cursor.getColumnIndex("choosedcountry_tempHigh")));
				ChoosedCountryList.setWeather(cursor.getString(cursor.getColumnIndex("choosedcountry_weather")));
				ChoosedCountryList.setImageId(cursor.getInt(cursor.getColumnIndex("choosedcountry_imageID")));
				list.add(ChoosedCountryList);
			} while (cursor.moveToNext());
		}
		return list;
	}
	
	//ɾ��ChoosedCountry���е�����
	public void delChoosedCountry(ChoosedCountryList choosedCountry) {
		try {
			db.delete("ChoosedCountry", "choosedcountry_code=?", new String[] {choosedCountry.getCode()});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
}
