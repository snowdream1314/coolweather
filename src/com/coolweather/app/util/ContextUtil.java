package com.coolweather.app.util;

import android.app.Application;

//用于获取全局上下文
public class ContextUtil extends Application {
	private static ContextUtil instance;
	
	public static ContextUtil getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}
}
