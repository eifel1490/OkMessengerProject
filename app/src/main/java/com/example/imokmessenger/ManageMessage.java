package com.example.imokmessenger;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class ManageMessage extends Activity {

    private static final String EXTRA_MESSAGE = "extra_message";
    public static final String TAG = "m";

    IntentFilter ifilter;
    Intent batteryStatus;
    String message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        message = (String) getIntent().getSerializableExtra(EXTRA_MESSAGE);
        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = getBaseContext().registerReceiver(null, ifilter);
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        int percent = (level * 100) / scale;
        Toast.makeText(this,String.valueOf(percent),Toast.LENGTH_LONG).show();
        //если батарея не заряжается
        if(!isChargeBattery(batteryStatus)){
            //и если уровень заряда равен 59
            if(percent==59){
                Toast.makeText(this,message,Toast.LENGTH_LONG).show();
            }
        }

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }

    //метод,который обрабатывает посланный себе же интент.Аналог конструктора
    public static Intent newIntent(Context packageContext, String message) {
        //достаем интент,отправленный нашему же классу
        Intent intent = new Intent(packageContext,ManageMessage.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        //возвращаем интент
        return intent;
    }

    public boolean isChargeBattery(Intent batteryStatus){
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;
        Toast.makeText(this,String.valueOf(isCharging),Toast.LENGTH_SHORT).show();
        return isCharging;
    }

    //метод формирующий ArrayList из чекнутых контактов
    public List<String> fillListCheckedContacts(Context context){
        Log.d(TAG,"fillListCheckedContacts");
        List<String> list = new ArrayList<>();
        ContactsBaseHelper dbHelper = new ContactsBaseHelper(context);
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

    public void sendMessageToContacts(List<String>list,String message,Context context) {
        Log.d(TAG, "sendMessageToContacts");
        //проходим по листу
        for (int i = 0; i < list.size(); i++) {
            //для каждого контакта вызываем метод sendSMSMessage
            sendSMSMessage(list.get(i), message,context);
        }
    }


    public void sendSMSMessage(String contact, String message,Context context) {
            Log.d(TAG,"sendSMSMessage");
            PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(context,ManageMessage.class), 0);
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(contact, null, message, pi, null);
        }







    }







    /*public static final String TAG = "logManageMessage";

    //константа для работы с интентом
    private static final String EXTRA_MESSAGE = "com.example.imokmessenger.message_id";

    //список контактов
    public List<String> userDataList;
    public String message;
    //SQLiteDatabase sqLiteDatabase;
    ContactsBaseHelper dbHelper;

    public ManageMessage() {
    }*/







    //метод,отправляющий СМС контактам
    /*protected void sendSMSMessage(String contact, String message) {
        Log.d(TAG,"sendSMSMessage");
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,ManageMessage.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(contact, null, message, pi, null);
    }*/


    /**/

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



