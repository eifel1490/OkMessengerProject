package com.example.imokmessenger.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imokmessenger.DataBase.ContactPreferences;
import com.example.imokmessenger.DataBase.DB;
import com.example.imokmessenger.Model.UserData;
import com.example.imokmessenger.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.R.id.message;



public class InfoFragment extends Fragment {

    private static final String TAG = "Info";

    TextView infoSelectContacts, infoSelectMessage, infoSelectChargeLevel;
    String chargeBatteryLevel;
    DB db;
    List<String>infoList;
    Handler h;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.information_screen, container, false);
        
        infoSelectMessage = (TextView)v.findViewById(R.id.textViewSelectMessage);
        infoSelectChargeLevel = (TextView)v.findViewById(R.id.textViewSelectChargeLevel);
        infoSelectContacts = (TextView)v.findViewById(R.id.textViewSelectContacts);
        infoSelectContacts.setMovementMethod(new ScrollingMovementMethod());
        
        String userMessages = ContactPreferences.getStoredMessage(getContext());
        String userCharge = ContactPreferences.getStoredCharge(getContext());
        
        if(userMessages!=null){
            infoSelectMessage.setText("Выбранное сообщение: "+'\n'+userMessages);
        }
        else infoSelectMessage.setText("Сообщение не выбрано");
        
        if(userCharge!=null) {
            infoSelectChargeLevel.setText("Уровень заряда: " + '\n' + userCharge+"%");
        }
        else infoSelectChargeLevel.setText("Уровень заряда: " + '\n' + "по умолчанию 5 %");
        
        infoSelectContacts.setText("Выбранные контакты: "+'\n');


        db = new DB(getContext());
        db.open();

        h = new Handler() {
            public void handleMessage(android.os.Message msg) {
                String message = (String) msg.obj;
                Log.d(TAG,message);
                infoSelectContacts.append(message+'\n');
            };
        };

        Thread t = new Thread(new Runnable() {
            public void run() {
                infoList = db.getCheckedContacts();
                for(String s:infoList) {
                    Message msg = Message.obtain();
                    msg.obj = s;
                    msg.setTarget(h);
                    msg.sendToTarget();
                }
            }
        });
        t.start();
        
        return  v;
    }


}
