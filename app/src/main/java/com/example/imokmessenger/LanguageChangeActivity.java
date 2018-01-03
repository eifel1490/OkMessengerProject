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

public class LanguageChangeActivity extends AppCompatActivity {

    private static final String TAG = "LanguageChangeActivity";
  
    //кнопки
    Button chooseRussian, chooseEnglish, chooseUkrainian;

    
    /*@Override
    public void onBackPressed() { 
        //создаем интент на MainActivity
        Intent intent = new Intent(this,MainActivityND.class);
        //очищаем бэкстек
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //стартуем интент
        startActivity(intent);
    } */
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_activity);
        
        chooseRussian = (Button) findViewById(R.id.btnSelectRussian);
        chooseRussian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateViews("ru");
                goToActivity();
            }
        });
        
        chooseEnglish = (Button) findViewById(R.id.btnSelectEnglish);
        chooseEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateViews("en");
                goToActivity();
            }
        });
        
        chooseUkrainian = (Button) findViewById(R.id.btnSelectUkrainian);
        chooseUkrainian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateViews("uk");
                goToActivity();
            }
        });
    
    }
    
    @Override
	  protected void attachBaseContext(Context base) {
		    super.attachBaseContext(LocaleHelper.onAttach(base));
	  }
    
    private void updateViews(String languageCode) {
		    Context context = LocaleHelper.setLocale(this, languageCode);
		    Resources resources = context.getResources();
        
        /*chooseRussian.setText(resources.getString(R.string.set_Russian));
        chooseEnglish.setText(resources.getString(R.string.set_English));
        chooseUkrainian.setText(resources.getString(R.string.set_Ukrainian));*/
    }
    
    public void goToActivity(){
        //создаем интент на MainActivity
        Intent intent = new Intent(this,MainActivityND.class);
        //очищаем бэкстек
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //стартуем интент
        startActivity(intent);
        Log.d(TAG,"goToActivity();");
    }
 }
