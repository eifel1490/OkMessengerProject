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
    //константа ответа
    private static final int REQUEST_CODE = 777;
    //интервал сигнала
    public static final long ALARM_INTERVAL = DateUtils.MINUTE_IN_MILLIS;


    // вызовите это из своего сервиса
    public static void startAlarms(final Context context) {
        Log.d(TAG,"Режим оповещения включен");
        //получаем AlarmManager
        final AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //назначаем срабатывание сигнала(запуск относительно реального прошедшего времени,
        //срабатывание через минуту,сработает PendingIntent
        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, ALARM_INTERVAL,
                getAlarmIntent(context));
    }

    /*
     * Creates the PendingIntent used for alarms of this receiver.
     * метод возвращает PendingIntent
     */
    private static PendingIntent getAlarmIntent(final Context context) {
        Log.d(TAG,"getAlarmIntent вызван.Он вызывается ежеминутно");
        //вкладываем интент,который будет вызывать AlarmReceiver
        /*Если система видит, что создаваемый с таким флагом PendingIntent похож на существующий,
         то она возьмет extra-данные Intent создаваемого PendingIntent и запишет их вместо extra-данных
          Intent существующего PendingIntent. Проще говоря, существующий PendingIntent будет использовать
           Intent из создаваемого.*/
        return PendingIntent.getBroadcast(context, REQUEST_CODE, new Intent(context, AlarmReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }

    //приемник слушатель
    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d(TAG,"ресивером отловлено сообщение.Сейчас будет отправлен интент на сервис "+
        "с параметром BATTERY_UPDATE");
        if (intent.getAction() == null) {
            // If you called your Receiver explicitly, this is what you should expect to happen
            //создаем интент на вызов сервиса
            Intent monitorIntent = new Intent(context, YourService.class);
            monitorIntent.putExtra(YourService.BATTERY_UPDATE, true);
            context.startService(monitorIntent);

            
        }
    }
}
