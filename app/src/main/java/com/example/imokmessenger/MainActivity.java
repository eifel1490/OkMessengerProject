package com.example.imokmessenger;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener  {

    final String SAVED_VALUE = "saved_value";
    public static final String TAG = "myLog";

    Button chooseContacts, createMessageText, editData;
    List<UserData> testList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //определение кнопок
        if(getValueFromPreference()==true) {
            chooseContacts = (Button) findViewById(R.id.btnSelectContacts);
            chooseContacts.setEnabled(false);
        }
        else
            chooseContacts = (Button) findViewById(R.id.btnSelectContacts);

        createMessageText = (Button) findViewById(R.id.btnSmsText);
        editData = (Button) findViewById(R.id.btnEdit);
        //назначаем кнопки слушателями
        chooseContacts.setOnClickListener(this);
        createMessageText.setOnClickListener(this);
        editData.setOnClickListener(this);
        testList = new UserData().getList();
        //TODO проверка преференса,в зависимости от этого ставим кнопку Выбрать контакты активной или нет
    }

    //обработчик нажатия на кнопки
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSelectContacts:
                Intent intent = new Intent(this,ContactListActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSmsText:


                break;
            case R.id.btnEdit:

                break;
        }
    }

    boolean getValueFromPreference() {
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        String result = sPref.getString(SAVED_VALUE,"");
        Log.d(TAG,"result = ");
        if(result.equals("listNotEmpty")){
            return true;
        }
        else return false;
    }

}
