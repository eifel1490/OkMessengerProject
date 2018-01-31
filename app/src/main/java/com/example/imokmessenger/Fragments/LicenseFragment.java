package com.example.imokmessenger.Fragments;

//вставить импорты


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.imokmessenger.Activityes.MainActivityND;
import com.example.imokmessenger.R;

import static java.security.AccessController.getContext;

public class LicenseFragment extends DialogFragment implements MainActivityND.OnBackPressedListener  {
    private static final String TAG = "License";

    @Override
    public void doBack() {
        goToHostActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG,"onCreateDialog called");
        //LayoutInflater – это класс, который умеет из содержимого layout-файла создать View-элемент.
        LayoutInflater dialogInflater = getActivity().getLayoutInflater();
        //создаем вью из макета fragment_licenses
        View openSourceLicensesView = dialogInflater.inflate(R.layout.fragment_licenses, null);
        //создаем диалог
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        
        dialogBuilder.setView(openSourceLicensesView)
                //устанавливаем заголовок
                .setTitle((getString(R.string.dialog_title_licenses)))
                //устанавливаем нейтральную кнопку ОК
                .setNeutralButton(android.R.string.ok, null);

        return dialogBuilder.create();
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
