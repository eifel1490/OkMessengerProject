package com.example.imokmessenger;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import static android.R.attr.level;


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
        Log.d(TAG,"onStartCommand");

        //после перезагрузки телефона запускаем AlarmReceiver
        if (intent != null && intent.hasExtra(BootReceiver.ACTION_BOOT)){
            Log.d(TAG,"Action Boot");
            AlarmReceiver.startAlarms(YourService.this.getApplicationContext());
        }
        if (intent != null && intent.hasExtra(BATTERY_UPDATE)){
            Log.d(TAG,"Battery update");
            new BatteryCheckAsync().execute();
        }
        if (intent != null && intent.hasExtra(HANDLE_REBOOT)){
            Log.d(TAG,"first_start");
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
            Log.d(TAG,"doInBackground");
            //Battery State check - create log entries of current battery state
            IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = YourService.this.registerReceiver(null, ifilter);

            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            Log.d(TAG, "Battery is charging: " + isCharging);

            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int percent = (level * 100) / scale;
            boolean notSend = true;
            Log.d(TAG, "Battery charge percent: " + percent);

            while(percent==39&&notSend){
                SmsManager smsManager= SmsManager.getDefault();
                Log.d(TAG,"Sending message");
                smsManager.sendTextMessage("+380965924585",null,"Battery percent: "+level+"%",null,null);
                notSend=false;
            }


            return null;
        }





        protected void onPostExecute(){
            YourService.this.stopSelf();
        }
    }
}
