package com.example.imokmessenger;

import android.content.IntentFilter;
import android.content.SharedPreferences;
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
    //TODO TEST удалить
    //ContactsBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //определение кнопок
        //если выбраны контакты для рассылки сообщений
        if(getValueFromPreference()) {
            chooseContacts = (Button) findViewById(R.id.btnSelectContacts);
            //то делаем кнопку "Выбрать контакты" неактивной
            chooseContacts.setEnabled(false);
        }
        else
            //если контакты для рассылки сообщений не выбраны,то кнопка "Выбрать контакты" будет активной
            chooseContacts = (Button) findViewById(R.id.btnSelectContacts);

        //если пользователь не выбрал текст сообщения         
        if(getValueMessageFromPreference()) {
            createMessageText = (Button) findViewById(R.id.btnSmsText);
            //то кнопка "Выбрать сообщение" будет неактивной
            createMessageText.setEnabled(false);
        }
        else
            //если пользователь выбрал текст сообщения, то кнопка "Выбрать текст сообщения будет активной
            createMessageText = (Button) findViewById(R.id.btnSmsText);

        //если выбраны контакты или выбран текст то кнопка редактировать активна
        if(getValueFromPreference()||getValueMessageFromPreference()){
            editData = (Button) findViewById(R.id.btnEdit);
        }
       editData = (Button) findViewById(R.id.btnEdit);
        //по умолчанию кнопка неактивна
        editData.setEnabled(false);
        //назначаем кнопки слушателями
        chooseContacts.setOnClickListener(this);
        createMessageText.setOnClickListener(this);
        editData.setOnClickListener(this);
        //TODO TEST удалить
        //dbHelper = new ContactsBaseHelper(this);
    }

    //обработчик нажатия на кнопки
    @Override
    public void onClick(View v) {
        //TODO TEST удалить
        //SQLiteDatabase db = dbHelper.getWritableDatabase();
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
                //db.delete(ContactsDbSchema.ContactsTable.DB_TABLE,null,null);
                //ContactPreferences.setStoredMessage(this,"");
                //то остальные кнопки делаются активными
                chooseContacts.setEnabled(true);
                createMessageText.setEnabled(true);

        }
    }

    //метод,получающий значение,были ли чекнуты контакты в списке контактов
    boolean getValueFromPreference() {
        String result = ContactPreferences.getStoredQuery(this);
        Log.d(TAG,"result = "+result);
        if(result!=null){
            return true;
        }
        else return false;
    }

    //метод, получающий значение,было ли записано сообщение
    boolean getValueMessageFromPreference() {
        String result = ContactPreferences.getStoredMessage(this);
        Log.d(TAG,"result = "+result);
        if(result.length()>0){
            return true;
        }
        else return false;
    }

}
