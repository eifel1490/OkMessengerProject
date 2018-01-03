package com.example.imokmessenger;

import android.app.Application;
import android.content.Context;

import com.example.imokmessenger.LocaleHelper;

public class MainApplication extends Application {

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(LocaleHelper.onAttach(base, "ru"));
	}
}
