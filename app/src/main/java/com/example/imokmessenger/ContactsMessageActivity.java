package com.example.imokmessenger;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 *
 */

public class ContactsMessageActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "ContactsMessageActivity";

    EditText editUserText;
    Button cancelMessage, saveMessage;

    //переменная БД-хелпера
    ContactsBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_message_activity);

        editUserText = (EditText) findViewById(R.id.editText_message);
        cancelMessage = (Button) findViewById(R.id.button_cancel);
        saveMessage = (Button) findViewById(R.id.button_save);

        //назначаем кнопки слушателями
        cancelMessage.setOnClickListener(this);
        saveMessage.setOnClickListener(this);
        dbHelper = new ContactsBaseHelper(this);
    }

    //реакция на нажатие
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.button_cancel :
                intent = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
                break;
            case R.id.button_save :
                String textMessage = editUserText.getText().toString();
                if(textMessage.length()>0){
                    ContactPreferences.setStoredMessage(getBaseContext(),textMessage);
                    intent = new Intent(getBaseContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else Toast.makeText(getBaseContext(),"Введите текст сообщения",Toast.LENGTH_SHORT).show();
                break;
            default: break;
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if(isListChecked()&&ContactPreferences.getStoredMessage(this).length()>0) {

            Intent intent = new Intent(getApplicationContext(), YourService.class);
            intent.putExtra(YourService.HANDLE_REBOOT, true);
            Log.d(TAG,"onPause ContactsMessageActivity" + String.valueOf(intent.putExtra(YourService.HANDLE_REBOOT, true) != null));
            startService(intent);
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


}
