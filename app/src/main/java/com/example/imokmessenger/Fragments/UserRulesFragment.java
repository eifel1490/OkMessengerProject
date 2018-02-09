package com.example.imokmessenger.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.example.imokmessenger.Activityes.MainActivityND;
import com.example.imokmessenger.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class UserRulesFragment extends Fragment implements MainActivityND.OnBackPressedListener  {

    public static final String TAG = "UserRulesFragment";

    @BindView(R.id.ok_button) Button text;
    
    private Unbinder unbinder;
    

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

        View v = inflater.inflate(R.layout.user_rules, container, false);
        unbinder = ButterKnife.bind(this, v);

        return  v;
    }

    @OnClick(R.id.ok_button)
    void onButtonOkClick() {
        goToHostActivity();
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
