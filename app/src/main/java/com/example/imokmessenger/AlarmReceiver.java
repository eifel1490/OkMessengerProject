package com.example.imokmessenger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    private static final int REQUEST_CODE = 777;
    public static final long ALARM_INTERVAL = DateUtils.MINUTE_IN_MILLIS;


    
    public static void startAlarms(final Context context) {
        
        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, ALARM_INTERVAL,
                getAlarmIntent(context));
    }

    
    private static PendingIntent getAlarmIntent(final Context context) {
       return PendingIntent.getBroadcast(context, REQUEST_CODE, new Intent(context, AlarmReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    
    @Override
    public void onReceive(final Context context, final Intent intent) {
        
        if (intent.getAction() == null) {
            Intent monitorIntent = new Intent(context, YourService.class);
            monitorIntent.putExtra(YourService.BATTERY_UPDATE, true);
            context.startService(monitorIntent);
        }
    }
}
