package com.example.imokmessenger;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.BatteryManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import static android.R.attr.level;


public class MainActivity extends AppCompatActivity implements View.OnClickListener  {

    public static final String TAG = "myLog";

    //кнопки
    Button chooseContacts, createMessageText, editData;
    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        db = new DB(this);
        db.open();
        Log.d(TAG,String.valueOf(db.isListChecked()));
        Log.d(TAG,String.valueOf(getValueMessageFromPreference()));
        //определение кнопок
        chooseContacts = (Button) findViewById(R.id.btnSelectContacts);
        createMessageText = (Button) findViewById(R.id.btnSmsText);
        editData = (Button) findViewById(R.id.btnEdit);

        if(db.isListChecked()) {
            chooseContacts.setEnabled(false);
        }

        if(getValueMessageFromPreference()) {
            createMessageText.setEnabled(false);
        }

        if(!getValueMessageFromPreference()&&!db.isListChecked()) {
            editData.setEnabled(false);
        }

        //назначаем кнопки слушателями
        chooseContacts.setOnClickListener(this);
        createMessageText.setOnClickListener(this);
        editData.setOnClickListener(this);

    }

    //обработчик нажатия на кнопки
    @Override
    public void onClick(View v) {
        //TODO TEST удалить

        switch (v.getId()) {
            //если нажата кнопка "Выбрать контакты"
            case R.id.btnSelectContacts:
                //то создается интент с перенаправлением на список контактов
                Intent intentSelectContact = new Intent(this,ContactListActivity.class);
                startActivity(intentSelectContact);
                break;
            //если нажата кнопка "Выбрать текст СМС"
            case R.id.btnSmsText:
                //то создается интент с перенаправлением на страницу выбора сообщения
                Intent intentEditMessage = new Intent(this,ContactsMessageActivity.class);
                startActivity(intentEditMessage);
                break;
            //если нажата кнопка "Редактировать"
            case R.id.btnEdit:
                //TODO TEST удалить
                db.deleteAllData();
                db.close();
                ContactPreferences.setStoredMessage(this,"");
                //то остальные кнопки делаются активными
                chooseContacts.setEnabled(true);
                createMessageText.setEnabled(true);

        }
    }

    //метод, получающий значение,было ли записано сообщение
    boolean getValueMessageFromPreference() {
        String result = ContactPreferences.getStoredMessage(this);

        if(result!=null&&result.length()>0){
            return true;
        }
        else return false;
    }
}
