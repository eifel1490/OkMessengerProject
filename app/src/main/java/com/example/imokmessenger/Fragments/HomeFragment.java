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
    Button chooseContacts, createMessageText, editData;
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
    }


    @Nullable

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String valueFromHostActivity = "";
        View v = inflater.inflate(R.layout.main_activity, container, false);
        if(savedInstanceState!=null){
            valueFromHostActivity = getArguments().getString(MainActivityND.PARAMS);
        }

        chooseContacts = (Button) v.findViewById(R.id.btnSelectContacts);
        createMessageText = (Button) v.findViewById(R.id.btnSmsText);

        //если контакты уже отмечены и нет признаков желания редактировать
        if(db.isListChecked()&&!(valueFromHostActivity.equals("1"))) {
            chooseContacts.setEnabled(false);
        }
        //если контакты не отмечены и есть желание редактировать
        else chooseContacts.setEnabled(true);

        //если контакты уже отмечены и нет признаков желания редактировать
        if(getValueMessageFromPreference()&&!(valueFromHostActivity.equals("2"))) {
            createMessageText.setEnabled(false);
        }
        //если контакты не отмечены и есть желание редактировать
        else createMessageText.setEnabled(true);








        //editData = (Button) v.findViewById(R.id.btnEdit);


        //if(getValueMessageFromPreference()) {
        //    createMessageText.setEnabled(false);
        //}

        //if(!getValueMessageFromPreference()&&!db.isListChecked()) {
        //    editData.setEnabled(false);
        //}

        chooseContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //то создается интент с перенаправлением на список контактов
                //Intent intentSelectContact = new Intent(getContext(),ContactListActivity.class);
                //startActivity(intentSelectContact);
                someEventListener.someEvent("1");
            }
        });

        createMessageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                someEventListener.someEvent("2");
            }
        });

        //editData.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
        //        Log.d(TAG,"db is exist?"+String.valueOf(db!=null));
        //        db.open();
        //        db.deleteAllData();
        //        db.close();
        //        ContactPreferences.setStoredMessage(getContext(),"");
        //        //то остальные кнопки делаются активными
        //        chooseContacts.setEnabled(true);
        //        createMessageText.setEnabled(true);
        //        editData.setEnabled(false);
        //    }
        //});

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
