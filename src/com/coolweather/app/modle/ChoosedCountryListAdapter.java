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

public class ChoosedCountryListAdapter extends ArrayAdapter<ChoosedCountryList> {
	private int resourceId;
	
	public ChoosedCountryListAdapter(Context context, int textViewResourceId, List<ChoosedCountryList> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChoosedCountryList choosedCountryList = getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
		ImageView image = (ImageView) view.findViewById(R.id.choosedCountryList_image);
		TextView del = (TextView) view.findViewById(R.id.del);
		TextView name = (TextView) view.findViewById(R.id.choosedCountryList_name);
		TextView tempLow = (TextView) view.findViewById(R.id.choosedCountryList_tempLow);
		TextView tempHigh = (TextView) view.findViewById(R.id.choosedCountryList_tempHigh);
		TextView weather = (TextView) view.findViewById(R.id.choosedCountryList_weather);
		image.setImageResource(choosedCountryList.getImageId());
		del.setVisibility(View.INVISIBLE);
		name.setText(choosedCountryList.getName());
		weather.setText(choosedCountryList.getWeather());
		tempLow.setText(choosedCountryList.getTempLow());
		tempHigh.setText(choosedCountryList.getTempHigh());
		return view;
	}
}

