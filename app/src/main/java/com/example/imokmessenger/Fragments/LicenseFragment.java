package com.example.imokmessenger.Fragments;


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
        
        LayoutInflater dialogInflater = getActivity().getLayoutInflater();
        View openSourceLicensesView = dialogInflater.inflate(R.layout.fragment_licenses, null);
       
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(openSourceLicensesView)
                .setTitle((getString(R.string.dialog_title_licenses)))
                .setNeutralButton(android.R.string.ok, null);

        return dialogBuilder.create();
    }

    public void goToHostActivity(){
        
        Intent intent = new Intent(getContext(),MainActivityND.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
