package com.example.imokmessenger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener  {

    Button chooseContacts, createMessageText, editData;
    List<UserData> testList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //определение кнопок
        chooseContacts = (Button) findViewById(R.id.btnSelectContacts);
        createMessageText = (Button) findViewById(R.id.btnSmsText);
        editData = (Button) findViewById(R.id.btnEdit);
        //назначаем кнопки слушателями
        chooseContacts.setOnClickListener(this);
        createMessageText.setOnClickListener(this);
        editData.setOnClickListener(this);
        testList = new UserData().getList();
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

}
