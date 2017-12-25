package com.example.imokmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class BatteryLevelChangeFragment extends Fragment {

    EditText editChargeText;
    Button saveChargeMessage , cancelChargeMessage;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.battery_level_change, container, false);
        editChargeText = (EditText) v.findViewById(R.id.editLevel_message);
        saveChargeMessage = (Button) v.findViewById(R.id.button_save_charge);
        cancelChargeMessage = (Button) v.findViewById(R.id.button_cancel);

        saveChargeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chargeLevel = editChargeText.getText().toString();

                if(Integer.parseInt(chargeLevel)<=100&&Integer.parseInt(chargeLevel)>0){
                    ContactPreferences.setStoredCharge(getContext(),chargeLevel);
                    Intent intent = new Intent(getContext(),MainActivityND.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else Toast.makeText(getContext(),"Введите уровень батареи",Toast.LENGTH_SHORT).show();
            }
        });

        cancelChargeMessage.setOnClickListener(new View.OnClickListener() {
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

        return  v;
    }

}
