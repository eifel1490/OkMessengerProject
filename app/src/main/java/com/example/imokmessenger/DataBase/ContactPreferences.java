package com.example.imokmessenger.DataBase;

import android.content.Context;
import android.preference.PreferenceManager;

import static android.R.id.message;


public class ContactPreferences {

    private static final String STORED_MESSAGE = "stored_message";
    private static final String STORED_CHARGE = "stored_charge";
    private static final String STORED_LANGUAGE = "stored_language";


    
    public static String getStoredMessage(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(STORED_MESSAGE,null);
    }

    
    public static void setStoredMessage(Context context,String message) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(STORED_MESSAGE,message)
                .apply();
    }

    
    public static String getStoredCharge(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(STORED_CHARGE,null);
    }

    
    public static void setStoredCharge(Context context,String message) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(STORED_CHARGE,message)
                .apply();
    }

    
    public static String getStoredLanguage(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(STORED_LANGUAGE,null);
    }

    
    public static void setStoredLanguage(Context context,String message) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(STORED_LANGUAGE,message)
                .apply();
    }

}
