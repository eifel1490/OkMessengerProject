package com.example.imokmessenger;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;
import static android.content.Intent.ACTION_BATTERY_CHANGED;
import static android.content.Intent.ACTION_BATTERY_LOW;

/**
 *
 */

public class ManageMessage extends BroadcastReceiver{

    public static final String TAG = "logManageMessage";

    //константа для работы с интентом
    private static final String EXTRA_MESSAGE = "com.example.imokmessenger.message_id";

    //список контактов
    public List<String> userDataList;
    public String message;
    //SQLiteDatabase sqLiteDatabase;
    ContactsBaseHelper dbHelper;

    public ManageMessage() {
    }

    //метод формирующий ArrayList из чекнутых контактов
    public List<String> fillListCheckedContacts(){
        Log.d(TAG,"fillListCheckedContacts");
        List<String> list = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor c = sqLiteDatabase.query(ContactsDbSchema.ContactsTable.DB_TABLE,
                null,"selected = ?",new String[]{"1"},null,null,null);

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
        sqLiteDatabase.close();
        return list;
    }


        @Override
        //метод ресивер, отслеживает сообщения из BrodcastReceiver
        public void onReceive(Context context, Intent intent) {
            String message = ContactPreferences.getStoredMessage(context);

            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            if(level==25) {
                Toast.makeText(context,message,Toast.LENGTH_LONG).show();
            }
            //если полученный интент соответствует системному значению ACTION_BATTERY_LOW
            /*if(intent.getAction().equals(ACTION_BATTERY_CHANGED)) {
                //настраиваем фильтр на прием системных сообщений ACTION_BATTERY_CHANGED (состояние зарядки смартфона)
                IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                //создаем интент , которой регистрирует ресивер
                Intent batteryStatus = context.registerReceiver(null, ifilter);
                //текущий заряд батареи (рассчитывается от 0 до значения EXTRA_SCALE
                int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                //целое число, содержащее максимальный уровень заряда батареи
                int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
                //рассчитываем процент заряда батареи,который остался
                int percent = (level*100)/scale;
                //если процент заряда меньше 10 и больше 5
                //if (percent <= 10 && percent > 5) {
                if (percent == 21) {
                    // Do Something
                    //sendMessageToContacts(fillListCheckedContacts(),message);
                    userDataList = fillListCheckedContacts();
                    for(int i=0;i<userDataList.size();i++){
                        //для каждого контакта вызываем метод sendSMSMessage
                        PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context,ManageMessage.class), 0);
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(userDataList.get(i), null, message, pi, null);
                    }

                }*/
            }
        }


    //метод,отправляющий СМС контактам
    /*protected void sendSMSMessage(String contact, String message) {
        Log.d(TAG,"sendSMSMessage");
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,ManageMessage.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(contact, null, message, pi, null);
    }*/

    //метод,который обрабатывает посланный себе же интент.Аналог конструктора
    /*public static Intent newIntent(Context packageContext, String message) {
        //достаем интент,отправленный нашему же классу
        Intent intent = new Intent(packageContext,ManageMessage.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        //возвращаем интент
        return intent;
    }*/

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        message = (String) getIntent().getSerializableExtra(EXTRA_MESSAGE);
        //инициализация ArrayList
        this.userDataList = new ArrayList<>();
        dbHelper = new ContactsBaseHelper(this);
        registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        //sendMessageToContacts(fillListCheckedContacts());

        //LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,new IntentFilter(ACTION_BATTERY_CHANGED));

        Intent intent = new Intent(getBaseContext(),MainActivity.class);
        startActivity(intent);
    }*/

    /*private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent i) {
            //int level = i.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            //if(level==49){
            //    sendMessageToContacts(fillListCheckedContacts());

            //}
            //если полученный интент соответствует системному значению ACTION_BATTERY_LOW


        }
    };*/

    /*public void sendMessageToContacts(List<String>list,String message){
        Log.d(TAG,"sendMessageToContacts");
        //проходим по листу
        for(int i=0;i<list.size();i++){
            //для каждого контакта вызываем метод sendSMSMessage
            sendSMSMessage(list.get(i), message);
        }
    }*/



