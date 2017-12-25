package com.example.imokmessenger;

import android.content.Context;
import android.preference.PreferenceManager;

import static android.R.id.message;


public class ContactPreferences {

    //константа,было ли создано сообщение
    private static final String STORED_MESSAGE = "stored_message";
    private static final String STORED_CHARGE = "stored_message";

    //метод,возвращающий индикатор,было ли создано сообщение
    public static String getStoredMessage(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(STORED_MESSAGE,null);
    }

    //записывает идентификатор создания сообщения
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

    //записывает идентификатор создания сообщения
    public static void setStoredCharge(Context context,String message) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(STORED_CHARGE,message)
                .apply();
    }




}
