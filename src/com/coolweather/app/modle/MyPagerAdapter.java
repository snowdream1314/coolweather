package com.coolweather.app.modle;

import java.util.List;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class MyPagerAdapter extends PagerAdapter {
	
	private List<View> views;
	private Activity activity;
	
	public MyPagerAdapter(List<View> views, Activity activity) {
		this.activity = activity;
		this.views = views;
	}
	
	//viewPager中的组件数量
	@Override
	public int getCount() {
		if (views != null) {
			return views.size();
		}
		return 0;
	}
	//滑动切换时销毁当前组件
	@Override
	public void destroyItem(ViewGroup arg0, int position, Object object) {
		((ViewPager) arg0).removeView(views.get(position));
	}
	//滑动时生成的组件
	@Override
	public Object instantiateItem(ViewGroup arg0, int position) {
		((ViewPager) arg0).addView(views.get(position),0);
		return views.get(position);
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}
	
//	@Override
//	public CharSequence getPageTitle(int position) {
//		return titleContainer.get(position);
//	}
}
