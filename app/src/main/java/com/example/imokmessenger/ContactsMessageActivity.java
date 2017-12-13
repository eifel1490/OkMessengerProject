package com.example.imokmessenger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 *
 */

public class ContactsMessageActivity extends Activity implements View.OnClickListener {



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
    }

    //реакция на нажатие
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.button_cancel :
                intent = new Intent(getBaseContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.button_save :
                String textMessage = editUserText.getText().toString();
                if(textMessage.length()>0){
                    ContactPreferences.setStoredMessage(getBaseContext(),textMessage);
                    intent = ManageMessage.newIntent(this,textMessage);
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
        //если контакты выбраны, и сообщение сохранено,вызываем сервис
        if(ContactPreferences.getStoredQuery(this)!=null||ContactPreferences.getStoredMessage(this)!=null){
            startService(new Intent(this, MessageService.class));
        }
    }
}