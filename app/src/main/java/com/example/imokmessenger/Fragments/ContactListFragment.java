package com.example.imokmessenger.Fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private static final String CONTACTLISTFRAGMENT_ID = "cont_id";

    RecyclerView rvContacts;
    Button btnConfirm;
    DB db;
    CircularProgressBar circularProgressBar;
    String value;

    public static ContactListFragment newInstance(String index) {
        Bundle args = new Bundle();
        args.putSerializable(CONTACTLISTFRAGMENT_ID, index);
        ContactListFragment fragment = new ContactListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            value = String.valueOf(getArguments().getSerializable(CONTACTLISTFRAGMENT_ID));
        }

    }


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
        //ОТКРЫВАЕМ БАЗУ
        db = new DB(getContext());
        db.open();
        
        
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

        if(value!=null){
            Log.d(TAG, "value = " + value);

            if(value.length()>0) {

                if (value.equals("1")) {
                    new MyTaskDownloadFromDatabase(getContext()).executeOnExecutor(prepareToStartAsyncTask());
                }
            }
        }
        else
            new MyTaskDownloadFromContentProvider(getContext()).executeOnExecutor(prepareToStartAsyncTask());

        //инициализируем LocalBroadcastManager для "отлова" сообщений из адаптера
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,new IntentFilter("custom-message"));

        return v;
    }




    //приемник локальных сообщений(транслируются только в нашем приложении)
    // принимает локальные ссобщения из UserDataAdapter
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // получает значение из интента,пришедшего в сообщении
            String isPressed = intent.getStringExtra("message");
            //если результат из интента ненулевой
            if(isPressed != null){
                //и если он соответствует метке "нажато"
                if(isPressed=="pressed") {
                    //то кнопка Подтвердить становится активной
                    btnConfirm.setEnabled(true);
                }

                //если кнопка не нажата,то проверяем нажаты ли остальные кнопки
                //и если ни одна не нажата,то кнопка Подтвердить неактивна
                if(isPressed=="unpressed") {
                    if(!db.isListChecked()){
                        //то кнопка Подтвердить становится неактивной
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
        db.close();
    }
    
    
    private class MyTaskDownloadFromDatabase extends AsyncTask<Void, Void, List<UserData>> {
        
        private Context context;
        DB db;
        
        public MyTaskDownloadFromDatabase(Context context){
            this.context = context;
        }
        
        //метод вызывается в начале работы AsyncTask
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //ОТКРЫВАЕМ БАЗУ
            db = new DB(context);
            db.open();
        }
        
        @Override
        protected List<UserData> doInBackground(Void... params) {
            List<UserData> userDataList = new ArrayList<>();
            try {
                    Cursor cursor1 = db.getAllData();
                    userDataList = getDataFromCursor(cursor1,userDataList);
                }
            finally {
                    cursor1.close();
                    db.close();
                }
            return userDataList;
         }
        
        @Override
        protected void onPostExecute(List<UserData>list) {
            super.onPostExecute(list);
            UserDataAdapter contactAdapter = new UserDataAdapter(list, getContext());
            rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
            rvContacts.setAdapter(contactAdapter);
        }
    }


    //класс AsyncTask, выполняет операции в фоне
    private class MyTaskDownloadFromContentProvider extends AsyncTask<Void, Void, List<UserData>> {

        private Context context;
        DB db;

        public MyTaskDownloadFromContentProvider(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            db = new DB(context);
            db.open();
            circularProgressBar.setVisibility(View.VISIBLE);
        }

        
        @Override
        protected List<UserData> doInBackground(Void... params) {
            List<UserData> userDataList = new ArrayList<>();
                Cursor cursor = db.getAllData();
                if((cursor != null) && (cursor.getCount() > 0)){
                    db.deleteAllData();
                    Log.d(TAG,"data deleted");
                }
                
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
                    userDataList = getDataFromCursor(c,userDataList);
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

    public List<UserData> getDataFromCursor(Cursor c,List<UserData>list){
        if (c.moveToFirst()) {
            
            int idColIndex = c.getColumnIndex("contact_id");
            int nameColIndex = c.getColumnIndex("contact_name");
            int phoneColIndex = c.getColumnIndex("contact_phone");
            int selectColIndex = c.getColumnIndex("selected");

            do {
                UserData userData = new UserData();
                userData.setContactID(c.getString(idColIndex));
                userData.setContactName(c.getString(nameColIndex));
                userData.setContactNumber(c.getString(phoneColIndex));
                if(c.getString(selectColIndex).equals("1")){
                    userData.setSolved(true);
                }
                userData.setContactSelect(c.getString(selectColIndex));
                list.add(userData);
            }
            while (c.moveToNext());
        }
        return list;
    }




}
