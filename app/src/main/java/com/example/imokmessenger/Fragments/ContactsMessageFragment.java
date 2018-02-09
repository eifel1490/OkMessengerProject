package com.example.imokmessenger.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
    String textMessage;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivityND) getActivity()).setOnBackPressedListener(this);
    }

    @Override
    public void doBack() {
        prepareToStartServise();
        goToHostActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.contacts_message_activity, container, false);
        db = new DB(getContext());
        db.open();
        editUserText = (EditText) v.findViewById(R.id.editText_message);
        saveMessage = (Button) v.findViewById(R.id.button_save);
        saveMessage.setEnabled(false);
        editUserText.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textMessage = s.toString();
                saveMessage.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        saveMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactPreferences.setStoredMessage(getContext(),"");
                if(textMessage.length()>0){
                    ContactPreferences.setStoredMessage(getContext(),textMessage);
                    prepareToStartServise();
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
        prepareToStartServise();
    }

    public void goToHostActivity(){
        Intent intent = new Intent(getContext(),MainActivityND.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void prepareToStartServise(){
        if(db.isListChecked()&&ContactPreferences.getStoredMessage(getContext())!=null&&
                ContactPreferences.getStoredMessage(getContext()).length()>0) {

            Intent intent = new Intent(getContext(), YourService.class);
            intent.putExtra(YourService.HANDLE_REBOOT, true);
            
            db.close();
            getActivity().startService(intent);
        }
    }

}
