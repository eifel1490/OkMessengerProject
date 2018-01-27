package com.example.imokmessenger.Activityes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebViewFragment;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imokmessenger.Fragments.BatteryLevelChangeFragment;
import com.example.imokmessenger.Fragments.ContactListFragment;
import com.example.imokmessenger.Fragments.ContactsMessageFragment;
import com.example.imokmessenger.Fragments.HomeFragment;
import com.example.imokmessenger.Fragments.InfoFragment;
import com.example.imokmessenger.Fragments.UserRulesFragment;
import com.example.imokmessenger.LocaleHelper;
import com.example.imokmessenger.R;


public class MainActivityND extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,HomeFragment.onSomeEventListener {

    private static final String TAG = "MainActivityND";
    //константа для отправки во фрагмент
    public static final String PARAMS = "params";
    //обьект слушателя для нажатия кнопки Назад во фрагменте
    protected OnBackPressedListener onBackPressedListener;

    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nd);
        //Тулбар
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Тулбар будет использовать все свойства старого ActionBar
        setSupportActionBar(toolbar);
        //по умолчанию будет выводится фрагмент главной страницы
        if (savedInstanceState == null) {
            Fragment newFragment = new HomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.content_frame, newFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    //метод интерфейса,обьявленного во фрагментах.Отлавливаем значения
    @Override
    public void someEvent(String s) {
        if(s == "1"){
            showFragment(new ContactListFragment());
        }
        if(s == "2"){
            showFragment(new ContactsMessageFragment());
        }
        if(s == "3"){
            showFragment(new InfoFragment());
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (onBackPressedListener != null) {
            onBackPressedListener.doBack();
        }
        else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            showFragment(new HomeFragment());
        }
        
        if(id == R.id.changeBatteryLevel){
            showFragment(new BatteryLevelChangeFragment());
        }
        
        if(id == R.id.rules){
            showFragment(new UserRulesFragment());
        }

        if(id == R.id.edit_contacts){
            showFragment(new ContactListFragment());
        }

        if(id == R.id.edit_message){
            showFragment(new ContactsMessageFragment());
        }


        
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showFragment(Fragment fragment){
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }

    @Override
    protected void onDestroy() {
        onBackPressedListener = null;
        super.onDestroy();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    private void updateViews(String languageCode) {
        Log.d(TAG,"updateViews is called");
        LocaleHelper.setLocale(getApplication(), languageCode);
        updateScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_russian:
                updateViews("ru");
                return true;

            case R.id.menu_item_english:
                updateViews("en");
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //для плавного обновления активити
    public void updateScreen(){
        Intent intent= new Intent(MainActivityND.this,MainActivityND.class);
        finish();
        //overridePendingTransition(0,0);
        startActivity(getIntent());
        //overridePendingTransition(0,0);

    }

    //для отпраки значения во фрагмент
    public void passingValueToFrgment(String s){
        Bundle bundle = new Bundle();
        bundle.putString(PARAMS, s);

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
    }
}
