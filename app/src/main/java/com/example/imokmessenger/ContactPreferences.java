package com.example.imokmessenger;

import android.content.Context;
import android.preference.PreferenceManager;



public class ContactPreferences {

    private static final String PREF_QUERY = "prefQuery";

    public static String getStoredQuery(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_QUERY, null);
    }
    public static void setStoredQuery(Context context, String query) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_QUERY, query)
                .apply();
    }
}
