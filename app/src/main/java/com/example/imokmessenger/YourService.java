package com.example.imokmessenger;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import com.example.imokmessenger.DataBase.ContactPreferences;
import com.example.imokmessenger.DataBase.DB;

import java.util.ArrayList;
import java.util.List;


public class YourService extends Service {
    private static final String TAG = "YourService";
    public static final String BATTERY_UPDATE = "battery_update";
    public static final String HANDLE_REBOOT = "first_start";


    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Сервис создан");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand в сервисе запущен");

        //после перезагрузки телефона запускаем AlarmReceiver
        if (intent != null && intent.hasExtra(BootReceiver.ACTION_BOOT)){
            Log.d(TAG,"Action Boot");
            AlarmReceiver.startAlarms(YourService.this.getApplicationContext());
        }
        if (intent != null && intent.hasExtra(BATTERY_UPDATE)){
            Log.d(TAG,"запущено после обновления уровня заряда.Теперь должен сработать" +
                    "BatteryCheckAsync");
            new BatteryCheckAsync().execute();
        }
        if (intent != null && intent.hasExtra(HANDLE_REBOOT)){
            Log.d(TAG,"запущено после HANDLE_REBOOT");
            AlarmReceiver.startAlarms(YourService.this.getApplicationContext());
        }

        // сервис будет восстановлен после убийства системой
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class BatteryCheckAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            Log.d(TAG,"doInBackground стартовал");
            String batteryChargeLevel = ContactPreferences.getStoredCharge(getBaseContext());

            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = YourService.this.registerReceiver(null, ifilter);

            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            Log.d(TAG, "Батарея заряжается? " + isCharging);

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int percent = (level * 100) / scale;

            if(!isCharging) {
                Log.d(TAG, "Уровень заряда батареи: " + percent);
                if (batteryChargeLevel != null &&
                        batteryChargeLevel.length() > 0) {
                    Log.d(TAG, "stored value = " + batteryChargeLevel);
                    boolean flag = false;

                    while (percent == Integer.parseInt(batteryChargeLevel)) {

                        if (!flag) {
                            Log.d(TAG, "Подготовка к отправке сообщения");
                            sendMessageToContacts(fillListCheckedContacts(getApplication()), getBaseContext());
                            Log.d(TAG, "Сообщение отправлено успешно после" +
                                    "достижения уровня батареи " + percent);
                            flag = true;
                        }
                    }

                } else if (batteryChargeLevel == null || batteryChargeLevel.length() == 0) {
                    Log.d(TAG, "пользователь не указывал значение заряда,сообщение будет" +
                            "отправлено в штатном режиме 5 процентов");
                    boolean flag = false;

                    while (percent == 5) {

                        if (!flag) {
                            sendMessageToContacts(fillListCheckedContacts(getApplication()), getBaseContext());
                            Log.d(TAG, "Сообщение отправлено успешно в штатном режиме 5 процентов заряда");
                            flag = true;
                        }
                    }
                }
            }

            return null;
        }

        //метод формирующий ArrayList из чекнутых контактов
        public List<String> fillListCheckedContacts(Context context){
            Log.d(TAG,"fillListCheckedContacts");
            List<String> list = new ArrayList<>();
            DB db = new DB(context);
            db.open();
            Cursor c = db.queryData();
            try {

                if (c.moveToFirst()) {
                    int phoneColIndex = c.getColumnIndex("contact_phone");

                    do {
                        list.add(c.getString(phoneColIndex));
                        // переход на следующую строку ,а если следующей нет (текущая - последняя), то false - выходим из цикла
                    }
                    while (c.moveToNext());
                }
            }
            finally {
                if(c!=null) {
                    c.close();
                }
            }

            db.close();
            for(String s:list){
                Log.d(TAG,"Выбран контакт : "+s);
            }
            return list;
        }

        public void sendMessageToContacts(List<String> list,Context context) {
            String message = ContactPreferences.getStoredMessage(context);
            Log.d(TAG, "sendMessageToContacts");
            //проходим по листу
            //for (int i = 0; i < list.size(); i++) {
                //для каждого контакта вызываем метод sendSMSMessage
            //    Log.d(TAG,"Сообщение отправлено :"+list.get(i));
            //    sendSMSMessage(list.get(i), message);
            //    break;
            //}
            for(String s:list){
                Log.d(TAG,"Сообщение отправлено :"+s);
                sendSMSMessage(s, message);
            }
        }


        public void sendSMSMessage(String contact,String message) {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(contact, null, message, null, null);
        }

    }
}
