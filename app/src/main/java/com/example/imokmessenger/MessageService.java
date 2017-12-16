package com.example.imokmessenger;


/*сервис.Будет работать в фоне*/

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.telephony.SmsManager;

public class MessageService extends IntentService {
   
   public MessageService() {
    super("messageService");
  }

   @Override
    protected void onHandleIntent(Intent intent) {
        //,прослушивать BroadcastReceiver и при достижении
        //уровня аккумулятора меньше Х процентов выполнять действие
        //здесь выполняется код из ManageMessage
       getBatteryPercentage();
       
    }

    private void getBatteryPercentage() {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int level = -1;
                if (currentLevel >= 0 && scale > 0) {
                    level = (currentLevel * 100) / scale;
                }
                if(level==61){
                    SmsManager smsManager= SmsManager.getDefault();
                    smsManager.sendTextMessage("+380679041199",null,"Battery percent: "+level+"%",null,null);
                }
                //batteryPercent.setText("Battery Level Remaining: " + level + "%");
                //if(level<=5 && notSent){
                //    notSent=false;
                //   SmsManager smsManager=SmsManager.getDefault();
                //  Log.d("Sending message to", "9999999999");
                //   smsManager.sendTextMessage("9999999999",null,"Battery percent: "+level+"%",null,null);
                // }
            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }


}
