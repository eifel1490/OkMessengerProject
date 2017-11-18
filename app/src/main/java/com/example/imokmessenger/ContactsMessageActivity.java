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

    }
}
