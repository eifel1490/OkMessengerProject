package com.example.imokmessenger;

import android.content.Intent;
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


public class BatteryLevelChangeFragment extends Fragment implements MainActivityND.OnBackPressedListener  {

    public static final String TAG = "BatteryLevelChangeFragment";

    EditText editChargeText;
    Button saveChargeMessage , cancelChargeMessage, resetChargeButton;
    String chargeBatteryLevel;
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

        View v = inflater.inflate(R.layout.battery_level_change, container, false);

        db = new DB(getContext());
        db.open();
        chargeBatteryLevel = ContactPreferences.getStoredCharge(getContext());
        Toast.makeText(getContext(),chargeBatteryLevel,Toast.LENGTH_SHORT).show();
        editChargeText = (EditText) v.findViewById(R.id.editLevel_message);
        if(chargeBatteryLevel!=null){
            editChargeText.setText(String.valueOf(chargeBatteryLevel));
        }

        saveChargeMessage = (Button) v.findViewById(R.id.button_save_charge);
        cancelChargeMessage = (Button) v.findViewById(R.id.button_cancel);
        resetChargeButton = (Button) v.findViewById(R.id.button_reset);

        saveChargeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chargeLevel = editChargeText.getText().toString();

                if(chargeLevel.length()>0&&Integer.parseInt(chargeLevel)<=100&&Integer.parseInt(chargeLevel)>0){
                    ContactPreferences.setStoredCharge(getContext(),chargeLevel);
                    Intent intent = new Intent(getContext(),MainActivityND.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                else Toast.makeText(getContext(),"Введите уровень батареи",Toast.LENGTH_SHORT).show();
            }
        });

        resetChargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),chargeBatteryLevel,Toast.LENGTH_SHORT).show();
                if(chargeBatteryLevel!=null){
                    ContactPreferences.setStoredCharge(getContext(),"");
                    editChargeText.setText(String.valueOf(""));
                }
            }
        });

        cancelChargeMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHostActivity();
            }
        });

        return  v;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(db.isListChecked()&&ContactPreferences.getStoredMessage(getContext())!=null&&
                ContactPreferences.getStoredMessage(getContext()).length()>0) {

            Intent intent = new Intent(getContext(), YourService.class);
            intent.putExtra(YourService.HANDLE_REBOOT, true);
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
