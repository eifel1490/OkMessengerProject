package com.example.imokmessenger;



import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;


/*класс отображающий список конактов для пользователя*/
public class ContactListActivity extends AppCompatActivity {

    public static final String TAG = "myTag";

    //Recycleview
    RecyclerView rvContacts;
    Button btnConfirm;

    //переменная БД-хелпера
    ContactsBaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_contacts_activity);
        //инициализируем Recycleview
        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        btnConfirm = (Button) findViewById(R.id.button_confirm);
        btnConfirm.setEnabled(false);
        //инициируем БД-хелпер
        dbHelper = new ContactsBaseHelper(this);
        new MyTask(this).execute();
        
        //инициализируем LocalBroadcastManager для "отлова" сообщений из адаптера
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,new IntentFilter("custom-message"));
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
                    if(!isListChecked()){
                        //то кнопка Подтвердить становится неактивной
                        btnConfirm.setEnabled(false);
                    }    
                }
           }
        }
    };


    //метод для проверки,есть ли нажатые кнопки.Возвращает false если ни одна кнопка не нажата
    //и true если нажата хотя бы одна кнопка
    public boolean isListChecked(){
        boolean result = false;
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(ContactsDbSchema.ContactsTable.DB_TABLE,
                new String[]{ContactsDbSchema.ContactsTable.Cols.SELECTED},"selected = ?",new String[]{"1"},null,null,null);
        if(cursor.getCount()>0) result = true;
        cursor.close();
        //осуществляется чтение из БД ,где в методе query вытаскиваются только значения из колонки "выбрано"
        //осуществляется просмотр все полученных записей,если хотя одна равна 1, то возвращаем true
        //иначе возвращаем false
        return result;
    }


    //при нажатии на кнопку Подтвердить
    public void btnConfirmClick(View v) {

        if(isListChecked()) {
            ContactPreferences.setStoredQuery(this,"listWithChecked");
        }
        else ContactPreferences.setStoredQuery(this,"listWithoutChecked");

        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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
            dialog = new SpotsDialog(context);
            dialog.show();
        }

        @Override
        protected List<UserData> doInBackground(Void... params) {
            //получаем доступ к базе
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            List<UserData> userDataList = new ArrayList<>();

            // создаем объект для данных
            ContentValues cv = new ContentValues();

            //создаем обьект contentResolver
            ContentResolver contentResolver = getContentResolver();
            //TODO попробовать другой алгоритм вытяжки данных из ContentProvider,этот долго работает
            //или поставить этот алгоритм в фоновый режим
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
                                cv.put("contact_phone", phone);
                                //заполняем поле обьекта Контакт "телефонный номер"
                                //userData.setContactNumber(phoneNumber);
                            }
                            //в колонку "выбрано" по умолчанию пишем 0,то есть не выбрано
                            cv.put("selected", 0);
                            //записываем строку в БД
                            db.insert(ContactsDbSchema.ContactsTable.DB_TABLE, null, cv);
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

            Cursor c = db.query(ContactsDbSchema.ContactsTable.DB_TABLE, null, null, null, null, null, null);
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
                }
            }

            //возвращаем список с обьектами UserData
            Log.d(TAG,"Количество контактов в ContactListActivity " + String.valueOf(userDataList.size()));
            return userDataList;

        }

        @Override
        protected void onPostExecute(List<UserData>list) {
            super.onPostExecute(list);
            UserDataAdapter contactAdapter = new UserDataAdapter(list, getApplicationContext());
            //расположение будет вертикальным списком
            rvContacts.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            //присваиваем адаптер
            rvContacts.setAdapter(contactAdapter);
            dialog.dismiss();
        }
    }


}


