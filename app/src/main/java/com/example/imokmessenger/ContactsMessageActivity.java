package com.example.imokmessenger;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 *
 */

public class ContactsMessageActivity extends Activity {

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
        //инициализируем БД-хелпер
        dbHelper = new ContactsBaseHelper(this);

    }

    //метод,вызывается при нажатии пользователем кнопки "Отмена"
    void onClickCancelButton(View v){
        //идет интент на главную активность
        Intent intent  = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    
    
    //метод, вызывается при нажатии пользователем кнопки "Сохранить"
    void onClickSaveButton(View v){
        //если сообщение не пустое (имеет текст)
        if (editUserText.getText().length() > 0){
           //то вызываем метод saveTextToDB(String s) и передаем в него текст введеного пользователем сообщения 
           saveTextToDB(editUserText.getText().toString());
        }
    }

    void saveTextToDB(String s){
        //открываем доступ к БД
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("message_text", s);
        db.insert(ContactsDbSchema.ContactsMessage.DB_MESSAGE_TABLE , null, cv);
    }





}
