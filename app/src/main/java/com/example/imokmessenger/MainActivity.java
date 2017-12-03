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

    Button chooseContacts, createMessageText, editData;
    //TODO TEST удалить
    ContactsBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        //определение кнопок
        if(getValueFromPreference()) {
            chooseContacts = (Button) findViewById(R.id.btnSelectContacts);
            chooseContacts.setEnabled(false);
        }
        else
            chooseContacts = (Button) findViewById(R.id.btnSelectContacts);

        if(getValueMessageFromPreference()) {
            createMessageText = (Button) findViewById(R.id.btnSmsText);
            createMessageText.setEnabled(false);
        }
        else
            createMessageText = (Button) findViewById(R.id.btnSmsText);

        editData = (Button) findViewById(R.id.btnEdit);
        //назначаем кнопки слушателями
        chooseContacts.setOnClickListener(this);
        createMessageText.setOnClickListener(this);
        editData.setOnClickListener(this);
        //TODO TEST удалить
        dbHelper = new ContactsBaseHelper(this);
    }

    //обработчик нажатия на кнопки
    @Override
    public void onClick(View v) {
        //TODO TEST удалить
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (v.getId()) {
            case R.id.btnSelectContacts:
                Intent intentSelectContact = new Intent(this,ContactListActivity.class);
                startActivity(intentSelectContact);
                break;
            case R.id.btnSmsText:
                Intent intentEditMessage = new Intent(this,ContactsMessageActivity.class);
                startActivity(intentEditMessage);

                break;
            case R.id.btnEdit:
                //TODO TEST удалить
                db.delete(ContactsDbSchema.ContactsTable.DB_TABLE,null,null);
                ContactPreferences.setStoredMessage(this,"");
                chooseContacts.setEnabled(true);
                createMessageText.setEnabled(true);

        }
    }

    boolean getValueFromPreference() {
        String result = ContactPreferences.getStoredQuery(this);
        Log.d(TAG,"result = "+result);
        if(result!=null){
            return true;
        }
        else return false;
    }

    boolean getValueMessageFromPreference() {
        String result = ContactPreferences.getStoredMessage(this);
        Log.d(TAG,"result = "+result);
        if(result.length()>0){
            return true;
        }
        else return false;
    }

}
