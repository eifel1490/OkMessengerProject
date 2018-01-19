package com.example.imokmessenger.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.imokmessenger.Activityes.MainActivityND;
import com.example.imokmessenger.DataBase.ContactPreferences;
import com.example.imokmessenger.DataBase.DB;
import com.example.imokmessenger.R;
import com.example.imokmessenger.YourService;

public class ContactsMessageFragment extends Fragment implements MainActivityND.OnBackPressedListener  {

    private static final String TAG = "ContactsMessageActivity";

    EditText editUserText;
    Button cancelMessage, saveMessage;
    DB db;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivityND) getActivity()).setOnBackPressedListener(this);
    }

    @Override
    public void doBack() {
        goToHostActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.contacts_message_activity, container, false);
        db = new DB(getContext());
        db.open();
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
                goToHostActivity();
            }
        });


        return v;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(db.isListChecked()&&ContactPreferences.getStoredMessage(getContext())!=null&&
                ContactPreferences.getStoredMessage(getContext()).length()>0) {

            Intent intent = new Intent(getContext(), YourService.class);
            intent.putExtra(YourService.HANDLE_REBOOT, true);
            Log.d(TAG,"onPause ContactsMessageActivity" + String.valueOf(intent.putExtra(YourService.HANDLE_REBOOT, true) != null));
            db.close();
            getActivity().startService(intent);
        }
    }

    public void goToHostActivity(){
        //создаем интент на MainActivity
        Intent intent = new Intent(getContext(),MainActivityND.class);
        //очищаем бэкстек
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //стартуем интент
        startActivity(intent);
    }

}
