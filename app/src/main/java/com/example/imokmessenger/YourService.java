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
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
        if (intent != null && intent.hasExtra(BootReceiver.ACTION_BOOT)){
            AlarmReceiver.startAlarms(YourService.this.getApplicationContext());
        }
        if (intent != null && intent.hasExtra(BATTERY_UPDATE)){
            new BatteryCheckAsync().execute();
        }
        if (intent != null && intent.hasExtra(HANDLE_REBOOT)){
            AlarmReceiver.startAlarms(YourService.this.getApplicationContext());
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private class BatteryCheckAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            
            String batteryChargeLevel = ContactPreferences.getStoredCharge(getBaseContext());

            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = YourService.this.registerReceiver(null, ifilter);

            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int percent = (level * 100) / scale;

            if(!isCharging) {
                
                if (batteryChargeLevel != null &&
                        batteryChargeLevel.length() > 0) {
                    
                    boolean flag = false;

                    while (percent == Integer.parseInt(batteryChargeLevel)) {

                        if (!flag) {
                            sendMessageToContacts(fillListCheckedContacts(getApplication()), getBaseContext());
                            flag = true;
                        }
                    }

                } else if (batteryChargeLevel == null || batteryChargeLevel.length() == 0) {
                   
                    boolean flag = false;

                    while (percent == 5) {

                        if (!flag) {
                            sendMessageToContacts(fillListCheckedContacts(getApplication()), getBaseContext());
                            flag = true;
                        }
                    }
                }
            }

            return null;
        }

        
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
                
            }
            return list;
        }

        public void sendMessageToContacts(List<String> list,Context context) {
            String message = ContactPreferences.getStoredMessage(context);
            
            
            for(String s:list){
                
                sendSMSMessage(s, message);
            }
        }


        public void sendSMSMessage(String contact,String message) {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(contact, null, message, null, null);
        }

    }
}
