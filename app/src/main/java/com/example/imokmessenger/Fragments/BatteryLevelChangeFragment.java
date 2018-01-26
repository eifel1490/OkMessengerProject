package com.example.imokmessenger.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class BatteryLevelChangeFragment extends Fragment implements MainActivityND.OnBackPressedListener  {

    public static final String TAG = "BatteryLevelChangeFragment";

    @BindView(R.id.editLevel_message) EditText editChargeText;
    @BindView(R.id.button_save_charge) Button saveChargeMessage;
    @BindView(R.id.button_cancel) Button cancelChargeMessage;
    @BindView(R.id.button_reset) Button resetChargeButton;
    private Unbinder unbinder;
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
        unbinder = ButterKnife.bind(this, v);

        if(chargeBatteryLevel!=null){
            editChargeText.setText(String.valueOf(chargeBatteryLevel));
        }

        return  v;

       
    }

    @OnClick(R.id.button_save_charge)
    void onButtonSaveChargeClick() {
        String chargeLevel = editChargeText.getText().toString();

        if(chargeLevel.length()>0&&Integer.parseInt(chargeLevel)<=100&&Integer.parseInt(chargeLevel)>0){
            ContactPreferences.setStoredCharge(getContext(),chargeLevel);
            Intent intent = new Intent(getContext(),MainActivityND.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else Toast.makeText(getContext(),"Введите уровень батареи",Toast.LENGTH_SHORT).show();
    }

    @OnClick (R.id.button_cancel)
    void onButtonCancelClick() {
        goToHostActivity();
    }

    @OnClick (R.id.button_reset)
    void onButtonResetClick() {
        Toast.makeText(getContext(),chargeBatteryLevel,Toast.LENGTH_SHORT).show();
        if(chargeBatteryLevel!=null){
            ContactPreferences.setStoredCharge(getContext(),"");
            editChargeText.setText(String.valueOf(""));
        }
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
        
        Intent intent = new Intent(getContext(),MainActivityND.class);
       
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        
        startActivity(intent);
    }

    //необходимо отключить butterknife
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
