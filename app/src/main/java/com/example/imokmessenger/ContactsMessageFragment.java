package com.example.imokmessenger;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 *
 */

public class ContactsMessageFragment extends Fragment  {

    private static final String TAG = "ContactsMessageActivity";

    EditText editUserText;
    Button cancelMessage, saveMessage;
    DB db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DB(getContext());
        db.open();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.contacts_message_activity, container, false);
        editUserText = (EditText) v.findViewById(R.id.editText_message);
        saveMessage = (Button) v.findViewById(R.id.button_save);

        saveMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textMessage = editUserText.getText().toString();
                if(textMessage.length()>0){
                    ContactPreferences.setStoredMessage(getContext(),textMessage);
                    Intent intent = new Intent(getContext(),MainActivityND.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else Toast.makeText(getContext(),"Введите текст сообщения",Toast.LENGTH_SHORT).show();

            }
        });

        cancelMessage = (Button) v.findViewById(R.id.button_cancel);
        cancelMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //создаем интент на MainActivity
                Intent intent = new Intent(getContext(),MainActivityND.class);
                //очищаем бэкстек
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                //стартуем интент
                startActivity(intent);
            }
        });


        return v;
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_message_activity);

        editUserText = (EditText) findViewById(R.id.editText_message);
        cancelMessage = (Button) findViewById(R.id.button_cancel);
        saveMessage = (Button) findViewById(R.id.button_save);

        //назначаем кнопки слушателями
        cancelMessage.setOnClickListener(this);
        saveMessage.setOnClickListener(this);
        db = new DB(this);
        db.open();
    }*/

    //реакция на нажатие
    /*@Override
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
                    intent = new Intent(getBaseContext(),HomeFragment.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else Toast.makeText(getBaseContext(),"Введите текст сообщения",Toast.LENGTH_SHORT).show();
                break;
            default: break;
        }
    }*/
    
    @Override
    public void onPause() {
        super.onPause();
        if(db.isListChecked()&&ContactPreferences.getStoredMessage(getContext()).length()>0) {

            Intent intent = new Intent(getContext(), YourService.class);
            intent.putExtra(YourService.HANDLE_REBOOT, true);
            Log.d(TAG,"onPause ContactsMessageActivity" + String.valueOf(intent.putExtra(YourService.HANDLE_REBOOT, true) != null));
            db.close();
            getActivity().startService(intent);
        }
    }

}
