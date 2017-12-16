package com.example.imokmessenger;

import android.content.Context;
import android.preference.PreferenceManager;



public class ContactPreferences {

    //константа для идентификации,было ли изменение в списке контактов
    private static final String PREF_QUERY = "prefQuery";
    //константа,было ли создано сообщение
    private static final String STORED_MESSAGE = "stored_message";

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

}
