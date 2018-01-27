package com.example.imokmessenger.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.imokmessenger.Activityes.MainActivityND;
import com.example.imokmessenger.DataBase.ContactPreferences;
import com.example.imokmessenger.DataBase.DB;
import com.example.imokmessenger.R;


public class HomeFragment extends Fragment  {

    public static final String TAG = "myLog";

    //кнопки
    Button chooseContacts, createMessageText;
    DB db;
    onSomeEventListener someEventListener;



    //интерфейс для взаимодействия с активити хостом MainActivityND
    public interface onSomeEventListener {
        void someEvent(String s);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            someEventListener = (onSomeEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DB(getContext());
        if(db.isListChecked()&&getValueMessageFromPreference()){
            //подгружаем другой лайаут
            someEventListener.someEvent("3");

        }
    }


    @Nullable

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_activity, container, false);

        chooseContacts = (Button) v.findViewById(R.id.btnSelectContacts);
        createMessageText = (Button) v.findViewById(R.id.btnSmsText);

        if(db.isListChecked()) {
            chooseContacts.setEnabled(false);
        }

        if(getValueMessageFromPreference()) {
            createMessageText.setEnabled(false);
        }

        chooseContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                someEventListener.someEvent("1");
            }
        });

        createMessageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                someEventListener.someEvent("2");
            }
        });

        return v;
    }

    //метод, получающий значение,было ли записано сообщение
    boolean getValueMessageFromPreference() {
        String result = ContactPreferences.getStoredMessage(getContext());
        if(result!=null&&result.length()>0){
            return true;
        }
        else return false;
    }

}
