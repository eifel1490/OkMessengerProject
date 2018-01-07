package com.example.imokmessenger;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Locale;



public class LocaleHelper {

    private static final String TAG = "LocaleHelper";

	private static final String SELECTED_LANGUAGE = "Locale.Helper.Selected.Language";

	public static Context onAttach(Context context) {
		//получаем язык,определенный в преференсе
		String lang = getPersistedData(context, Locale.getDefault().getLanguage());
		
		return setLocale(context, lang);
	}

	public static Context onAttach(Context context, String defaultLanguage) {
		String lang = getPersistedData(context, defaultLanguage);
		return setLocale(context, lang);
	}

	public static String getLanguage(Context context) {
		return getPersistedData(context, Locale.getDefault().getLanguage());
	}

	//принимает контекст и строку "язык"
	public static Context setLocale(Context context, String language) {
		//записывает язык по умолчанию в преференс
		persist(context, language);
		//если версия сдк больше или равна версии N	
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			return updateResources(context, language);
		}
		
		return updateResourcesLegacy(context, language);
	}

	private static String getPersistedData(Context context, String defaultLanguage) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);
	}

	//принимает контекст и строку "язык"
	private static void persist(Context context, String language) {
        Log.d(TAG,"persist() called");
        //создает SharedPreferences
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
                //записывает в SharedPreferences язык
		editor.putString(SELECTED_LANGUAGE, language);
		editor.apply();

	}

	//в этом методе 
	@TargetApi(Build.VERSION_CODES.N)
	private static Context updateResources(Context context, String language) {
		//устанавливем язык в локаль
		Locale locale = new Locale(language);
		//устанавливаем локаль по умолчанию
		Locale.setDefault(locale);

		Configuration configuration = context.getResources().getConfiguration();
		configuration.setLocale(locale);
		configuration.setLayoutDirection(locale);

		return context.createConfigurationContext(configuration);
	}

	@SuppressWarnings("deprecation")
	private static Context updateResourcesLegacy(Context context, String language) {
		Log.d(TAG,language);
		Locale locale = new Locale(language);
		Log.d(TAG,String.valueOf(locale==null));
		Locale.setDefault(locale);

		Resources resources = context.getResources();

		Configuration configuration = resources.getConfiguration();
		Log.d(TAG,String.valueOf(configuration==null));
		configuration.locale = locale;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			configuration.setLayoutDirection(locale);
		}

		resources.updateConfiguration(configuration, resources.getDisplayMetrics());

		return context;
	}
}
