package com.coolweather.app.modle;

import java.util.List;

import com.coolweather.app.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CityListAdapter extends ArrayAdapter<CityList> {
	private int resourceId;
	
	public CityListAdapter(Context context, int textViewResourceId, List<CityList> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CityList cityList = getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		ImageView image = (ImageView) view.findViewById(R.id.citylist_image);
		TextView name = (TextView) view.findViewById(R.id.citylist_name);
		TextView temp = (TextView) view.findViewById(R.id.citylist_temp);
		TextView weather = (TextView) view.findViewById(R.id.citylist_weather);
		image.setImageResource(cityList.getImageId());
		name.setText(cityList.getName());
		weather.setText(cityList.getWeather());
		temp.setText(cityList.getTemp());
		return view;
	}
}
