package com.example.imokmessenger;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebViewFragment;
import android.widget.TextView;


public class MainActivityND extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,HomeFragment.onSomeEventListener {

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

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
        if(id == R.id.chengeBatteryLevel){
            showFragment(new BatteryLevelChangeFragment());
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
}
