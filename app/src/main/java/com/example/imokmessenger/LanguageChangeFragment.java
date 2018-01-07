package com.example.imokmessenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static java.security.AccessController.getContext;

public class LanguageChangeFragment extends Fragment  {

    private static final String TAG = "LanguageChangeActivity";
  
    //кнопки
    Button chooseRussian, chooseEnglish;
    Context context;
    onSomeLanguageListener languageListener;

    //интерфейс для взаимодействия с активити хостом MainActivityND
    public interface onSomeLanguageListener {
        void someLanguageEvent(String s);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            languageListener = (onSomeLanguageListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeLanguageListener");
        }
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.language_activity, container, false);
        chooseRussian = (Button) v.findViewById(R.id.btnSelectRussian);
        chooseRussian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageListener.someLanguageEvent("ru");
                goToHostActivity();


            }
        });

        chooseEnglish = (Button) v.findViewById(R.id.btnSelectEnglish);
        chooseEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageListener.someLanguageEvent("en");
                goToHostActivity();

            }
        });

        return v;

    }

    public void goToHostActivity(){

        Intent intent = new Intent(getContext(),MainActivityND.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
 }
