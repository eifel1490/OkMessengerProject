package com.example.imokmessenger.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.imokmessenger.Activityes.MainActivityND;
import com.example.imokmessenger.DataBase.ContactPreferences;
import com.example.imokmessenger.DataBase.DB;
import com.example.imokmessenger.R;
import com.example.imokmessenger.Model.UserData;
import com.example.imokmessenger.Adapter.UserDataAdapter;

import com.example.imokmessenger.YourService;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



/*класс отображающий список конактов для пользователя*/
public class ContactListFragment extends Fragment implements MainActivityND.OnBackPressedListener {

    public static final String TAG = "ContactListActivity";

    RecyclerView rvContacts;
    Button btnConfirm;
    DB db;
    CircularProgressBar circularProgressBar;

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
        
        View v = inflater.inflate(R.layout.all_contacts_activity, container, false);

        rvContacts = (RecyclerView) v.findViewById(R.id.rvContacts);

        circularProgressBar = (CircularProgressBar) v.findViewById(R.id.yourCircularProgressbar);
        CircularProgressBar circularProgressBar = (CircularProgressBar) v.findViewById(R.id.yourCircularProgressbar);
        circularProgressBar.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        circularProgressBar.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.title_separator));
        int animationDuration = 3000;
        circularProgressBar.setProgressWithAnimation(100, animationDuration);


        btnConfirm = (Button) v.findViewById(R.id.button_confirm);
        btnConfirm.setEnabled(false);
        
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prepareToStartServise();
                goToHostActivity();
            }
        });


        prepareToWork();
        
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,new IntentFilter("custom-message"));


        return v;
    }

    //receiver of local messages (broadcast only in our application)
    // receive local message from the UserDataAdapter
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            
            String isPressed = intent.getStringExtra("message");
            
            if(isPressed != null){
                if(isPressed=="pressed") {
                    btnConfirm.setEnabled(true);
                }

            if(isPressed=="unpressed") {
                if(!db.isListChecked()){
                     btnConfirm.setEnabled(false);
                    }
                }
            }
        }
    };


    @Override
    public void onPause() {
        super.onPause();
        prepareToStartServise();
    }


    private class MyTask extends AsyncTask<Void, Void, List<UserData>> {

        private Context context;
        private AlertDialog dialog;


        public MyTask(Context context) {
            this.context = context;

        }

        
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            circularProgressBar.setVisibility(View.VISIBLE);
        }

        
        @Override
        protected List<UserData> doInBackground(Void... params) {
            Log.d(TAG,"doInBackground(Void... params) вызван");
            Cursor cursor = db.getAllData();
            if((cursor != null) && (cursor.getCount() > 0)){
                db.deleteAllData();
            }

            List<UserData> userDataList = new ArrayList<>();
            ContentValues cv = new ContentValues();

            ContentResolver contentResolver = getActivity().getContentResolver();
            Cursor cursorContentResolver = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            
            try {
                if (cursorContentResolver.getCount() > 0) {
                    while (cursorContentResolver.moveToNext()) {
                        int hasPhoneNumber = Integer.parseInt(cursorContentResolver.getString(cursorContentResolver.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                            if (hasPhoneNumber > 0) {
                            
                            String id = cursorContentResolver.getString(cursorContentResolver.getColumnIndex(ContactsContract.Contacts._ID));
                            
                            cv.put("contact_id", id);
                           
                            String name = cursorContentResolver.getString(cursorContentResolver.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            
                            cv.put("contact_name", name);
                            
                            Cursor phoneCursor = contentResolver.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id},
                                    null);
                            
                            if (phoneCursor.moveToNext()){
                                    String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    cv.put("contact_phone",phone.replaceAll("\\s+",""));
                            }
                            
                            cv.put("selected", 0);
                            db.insert(cv);
                            phoneCursor.close();
                        }
                    }
                }
            }
            
            finally {
                if(cursorContentResolver!=null){
                    cursorContentResolver.close();
                }
            }

            Cursor c = db.getAllData();
                try {
                    if (c.moveToFirst()) {
                        // определяем номера столбцов по имени в выборке
                        int idColIndex = c.getColumnIndex("contact_id");
                        int nameColIndex = c.getColumnIndex("contact_name");
                        int phoneColIndex = c.getColumnIndex("contact_phone");
                        int selectColIndex = c.getColumnIndex("selected");

                        do {
                            //создаем обьект типа UserData
                            UserData userData = new UserData();
                            //Заполняем его поля значениями из БД
                            userData.setContactID(c.getString(idColIndex));
                            userData.setContactName(c.getString(nameColIndex));
                            userData.setContactNumber(c.getString(phoneColIndex));
                            userData.setContactSelect(c.getString(selectColIndex));
                            //кладем в список
                            userDataList.add(userData);

                            // переход на следующую строку ,а если следующей нет (текущая - последняя), то false - выходим из цикла
                        }
                        while (c.moveToNext());
                    }
                }
                finally {
                    if(c!=null) {
                        c.close();
                        db.close();
                    }
                }   
                return userDataList;
        }

        
        @Override
        protected void onPostExecute(List<UserData>list) {
            super.onPostExecute(list);
            
            UserDataAdapter contactAdapter = new UserDataAdapter(list, getContext());
            rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
            rvContacts.setAdapter(contactAdapter);
            circularProgressBar.setVisibility(View.GONE);
        }
    }
    
    public void prepareToWork(){
        db = new DB(getContext());
        db.open();
        new MyTask(getContext()).executeOnExecutor(prepareToStartAsyncTask());
    }

    public void goToHostActivity(){
        Intent intent = new Intent(getContext(),MainActivityND.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void prepareToStartServise(){
        if(db.isListChecked()&&ContactPreferences.getStoredMessage(getContext())!=null&&
                ContactPreferences.getStoredMessage(getContext()).length()>0) {
                    Intent intent = new Intent(getContext(), YourService.class);
                    intent.putExtra(YourService.HANDLE_REBOOT, true);
                    db.close();
                    getActivity().startService(intent);
                }
     }

    //solution if doInBackground doesn't called after onPreExecute
    public Executor prepareToStartAsyncTask(){
        int corePoolSize = 60;
        int maximumPoolSize = 80;
        int keepAliveTime = 10;

        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
        Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
        return threadPoolExecutor;
    }


}
