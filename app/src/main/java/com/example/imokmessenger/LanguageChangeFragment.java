package com.example.imokmessenger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LanguageChangeActivity extends AppCompatActivity   {
  
    //кнопки
    Button chooseRussian, chooseEnglish, chooseUkrainian;

    
    @Override 
    public void onBackPressed() { 
        //создаем интент на MainActivity
        Intent intent = new Intent(getContext(),MainActivityND.class);
        //очищаем бэкстек
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //стартуем интент
        startActivity(intent);
    } 
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_activity);
        
        chooseRussian = (Button) v.findViewById(R.id.btnSelectRussian);
        chooseRussian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateViews("ru");
                goToActivity();
                
            }
        });
        
        chooseEnglish = (Button) v.findViewById(R.id.btnSelectEnglish);
        chooseRussian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateViews("en");
                goToActivity();
                
            }
        });
        
        chooseUkrainian = (Button) v.findViewById(R.id.btnSelectUkrainian);
        chooseRussian.setOnClickListener(new View.OnClickListener() {
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
        
        chooseRussian.setText(resources.getString(R.string.set_Russian));
        chooseEnglish.setText(resources.getString(R.string.set_English));
        chooseUkrainian.setText(resources.getString(R.string.set_Ukrainian));
    }
    
    public void goToActivity(){
        //создаем интент на MainActivity
        Intent intent = new Intent(getContext(),MainActivityND.class);
        //очищаем бэкстек
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //стартуем интент
        startActivity(intent);
    }
 }
