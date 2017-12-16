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
    //TODO TEST удалить
    ContactsBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //TODO TEST удалить
        dbHelper = new ContactsBaseHelper(this);
        Log.d(TAG,String.valueOf(isListChecked()));
        Log.d(TAG,String.valueOf(getValueMessageFromPreference()));
        //определение кнопок
        chooseContacts = (Button) findViewById(R.id.btnSelectContacts);
        createMessageText = (Button) findViewById(R.id.btnSmsText);
        editData = (Button) findViewById(R.id.btnEdit);

        if(isListChecked()) {
            chooseContacts.setEnabled(false);
        }

        if(getValueMessageFromPreference()) {
            createMessageText.setEnabled(false);
        }

        if(!getValueMessageFromPreference()&&!isListChecked()) {
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
        SQLiteDatabase db = dbHelper.getWritableDatabase();
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
                int clearCount = db.delete(ContactsDbSchema.ContactsTable.DB_TABLE,null,null);
                Log.d(TAG, "deleted rows count = " + clearCount);
                ContactPreferences.setStoredMessage(this,"");
                //то остальные кнопки делаются активными
                chooseContacts.setEnabled(true);
                createMessageText.setEnabled(true);

        }
    }

    //метод для проверки,есть ли нажатые кнопки.Возвращает false если ни одна кнопка не нажата
    //и true если нажата хотя бы одна кнопка
    public boolean isListChecked(){
        //результат по умолчанию false
        boolean result = false;
        //открываем доступ к БД
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        //создаем курсор,в который идет выкачка инфо методом query.Query вытягивает из таблицы только те значения в столце "Выбрано"
        //которые равны 1
        Cursor cursor = sqLiteDatabase.query(ContactsDbSchema.ContactsTable.DB_TABLE,
                new String[]{ContactsDbSchema.ContactsTable.Cols.SELECTED},"selected = ?",new String[]{"1"},null,null,null);
        //если курсор непустой,то значит есть минимум одно значение равное 1,тогде ставим результат true
        if(cursor.getCount()>0) result = true;
        //закрываем курсор
        cursor.close();
        //возвращаем результат
        return result;
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
