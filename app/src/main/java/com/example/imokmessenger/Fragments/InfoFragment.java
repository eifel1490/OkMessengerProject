package com.example.imokmessenger.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class InfoFragment extends Fragment {

    TextView infoSelectContacts, infoSelectMessage, infoSelectChargeLevel;
    String chargeBatteryLevel;
    DB db;
    List<String>infoList;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.information_screen, container, false);

        infoSelectContacts = (TextView)v.findViewById(R.id.textViewSelectContacts);
        infoSelectMessage = (TextView)v.findViewById(R.id.textViewSelectMessage);
        infoSelectChargeLevel = (TextView)v.findViewById(R.id.textViewSelectChargeLevel);

        db = new DB(getContext());
        db.open();

        Thread t = new Thread(new Runnable() {
            public void run() {
                infoList = db.getCheckedContacts();
            }
        });
        t.start();

        infoSelectContacts.setText("");
        if(infoList!=null) {
            for (int j = 0; j < infoList.size(); j++) {
                infoSelectContacts.append("Выбран:\n" + infoList.get(j) + "\n");
            }
        }









        //получение списка четкутых контактов,отображение в текствью





        return  v;
    }


}
