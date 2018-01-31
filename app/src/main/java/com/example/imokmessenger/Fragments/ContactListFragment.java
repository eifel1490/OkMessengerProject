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

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;


/*класс отображающий список конактов для пользователя*/
public class ContactListFragment extends Fragment implements MainActivityND.OnBackPressedListener {

    public static final String TAG = "ContactListActivity";


    RecyclerView rvContacts;
    Button btnConfirm;
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
        
        View v = inflater.inflate(R.layout.all_contacts_activity, container, false);

        rvContacts = (RecyclerView) v.findViewById(R.id.rvContacts);

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
    }


    //класс AsyncTask, выполняет операции в фоне
    private class MyTask extends AsyncTask<Void, Void, List<UserData>> {

        private Context context;
        private AlertDialog dialog;


        public MyTask(Context context) {
            this.context = context;

        }

        //метод вызывается в начале работы AsyncTask
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new SpotsDialog(context);
            dialog.show();
            Log.d(TAG,"onPreExecute() вызван");
        }

        //в этом методе происходит основная работа в фоне
        @Override
        protected List<UserData> doInBackground(Void... params) {
            Log.d(TAG,"doInBackground(Void... params) вызван");
            Cursor cursor = db.getAllData();
            if((cursor != null) && (cursor.getCount() > 0)){
                db.deleteAllData();
            }

            List<UserData> userDataList = new ArrayList<>();

            // создаем объект для данных
            ContentValues cv = new ContentValues();

            //создаем обьект contentResolver
            ContentResolver contentResolver = getActivity().getContentResolver();
            //создаем курсор с запросом "вытянуть все из contentProvider-базы ContactsContract.Contacts.CONTENT_URI"
            Cursor cursorContentResolver = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            //если курсор вернулся непустой(то есть есть хотя бы одна строка)

            try {

                if (cursorContentResolver.getCount() > 0) {
                    //передвигаем парсер на первую позицию
                    while (cursorContentResolver.moveToNext()) {
                        //получаем интовую переменную из поля contentProvider-базы,которая будет индикатором есть ли в контакте телефонный номер
                        int hasPhoneNumber = Integer.parseInt(cursorContentResolver.getString(cursorContentResolver.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                        //если в контакте есть телефонный номер,то...
                        if (hasPhoneNumber > 0) {
                            //...вычитываем значение ИД контакта
                            String id = cursorContentResolver.getString(cursorContentResolver.getColumnIndex(ContactsContract.Contacts._ID));
                            //записываем его в contentValues
                            cv.put("contact_id", id);
                            //затем вычитываем значение имени контакта
                            String name = cursorContentResolver.getString(cursorContentResolver.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            //и тоже записываем его в contentValues
                            cv.put("contact_name", name);
                            //создаем курсор с запросом "вытянуть все из contentProvider-базы ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            //где поле ContactsContract.CommonDataKinds.Phone.CONTACT_ID совпадает с введенным нами ИД
                            Cursor phoneCursor = contentResolver.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id},
                                    null);
                            //передвигаем парсер на первую позицию
                            if (phoneCursor.moveToNext()){
                                //вычитываем значение телефонного номера контакта
                                String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                //записываем телефонный номер в contentValues
                                cv.put("contact_phone",phone.replaceAll("\\s+",""));
                                //заполняем поле обьекта Контакт "телефонный номер"
                                //userData.setContactNumber(phoneNumber);
                            }
                            //в колонку "выбрано" по умолчанию пишем 0,то есть не выбрано
                            cv.put("selected", 0);
                            //записываем строку в БД
                            db.insert(cv);
                            //закрываем phoneCursor
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

            //читаем данные из него,пока они там есть (цикл)
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

        //метод вызывается после основной работы в фоне.Имеет связь с UI
        @Override
        protected void onPostExecute(List<UserData>list) {
            super.onPostExecute(list);
            Log.d(TAG,"onPostExecute(List<UserData>list)  вызван");
            UserDataAdapter contactAdapter = new UserDataAdapter(list, getContext());
            //расположение будет вертикальным списком
            rvContacts.setLayoutManager(new LinearLayoutManager(getContext()));
            //присваиваем адаптер
            rvContacts.setAdapter(contactAdapter);
            //закрываем прогрессдиалог
            dialog.dismiss();
        }
    }
    
    public void prepareToWork(){
        db = new DB(getContext());
        db.open();
        new MyTask(getContext()).executeOnExecutor(prepareToStartAsyncTask());
        //new MyTask(getContext()).execute();
        //new MyTask(getContext()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public void goToHostActivity(){
        //создаем интент на MainActivity
        Intent intent = new Intent(getContext(),MainActivityND.class);
        //очищаем бэкстек
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //стартуем интент
        startActivity(intent);
    }

    public void prepareToStartServise(){
        Log.d(TAG,"prepareToStartServise() вызван");
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
