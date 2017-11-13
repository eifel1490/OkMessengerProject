package com.example.imokmessenger;



import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

/*класс отображающий список конактов для пользователя*/
public class ContactListActivity extends AppCompatActivity {

    public static final String TAG = "myTag";


    //Recycleview
    RecyclerView rvContacts;
    List<UserData> contactUserDataList;
    Button btnConfirm;
    SharedPreferences sPref;
    //UserDataSingleton userDataSingleton;
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
        //userDataSingleton = UserDataSingleton.getInstance();
        getAllContacts();
        //инициализируем LocalBroadcastManager для "отлова" сообщений из адаптера
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,new IntentFilter("custom-message"));
    }

    private void getAllContacts() {
        
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        
        //обьект контакта
        //UserData userData;
        //создаем обьект contentResolver
        ContentResolver contentResolver = getContentResolver();
        //TODO попробовать другой алгоритм вытяжки данных из ContentProvider,этот долго работает
        //или поставить этот алгоритм в фоновый режим
        //создаем курсор с запросом "вытянуть все из contentProvider-базы ContactsContract.Contacts.CONTENT_URI"
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        //если курсор вернулся непустой(то есть есть хотя бы одна строка)
        if (cursor.getCount() > 0) {
            //передвигаем парсер на первую позицию
            while (cursor.moveToNext()) {
                //получаем интовую переменную из поля contentProvider-базы,которая будет индикатором есть ли в контакте телефонный номер
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                //если в контакте есть телефонный номер,то...
                if (hasPhoneNumber > 0) {
                    //...вычитываем значение ИД контакта
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    //записываем его в contentValues
                    cv.put("contact_id", id);
                    //затем вычитываем значение имени контакта
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    //и тоже записываем его в contentValues
                    cv.put("contact_name", name);
                    
                    //создаем обьект Контакта
                    //userData = new UserData();
                    //заполняем его поле "имя"
                    //userData.setContactName(name);
                    //создаем курсор с запросом "вытянуть все из contentProvider-базы ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    //где поле ContactsContract.CommonDataKinds.Phone.CONTACT_ID совпадает с введенным нами ИД
                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    //передвигаем парсер на первую позицию
                    if (phoneCursor.moveToNext()) {
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

                    //поле "выбрано" по умолчанию ставим в "Не выбрано"
                    //userData.setSolved(false);
                    
                    //добавляем обьект в contactUserDataList
                    //userDataSingleton.getdataList().add(userData);
                }
            }
            //создаем обьект адаптера,ему передается ArrayList и обьект контекста
            UserDataAdapter contactAdapter = new UserDataAdapter(userDataSingleton.getdataList(), getApplicationContext());
            //расположение будет вертикальным списком
            rvContacts.setLayoutManager(new LinearLayoutManager(this));
            //присваиваем адаптер
            rvContacts.setAdapter(contactAdapter);
        }
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
                if(isListChecked()==false){

                    btnConfirm.setEnabled(false);
                }
            }
        }
    };

    //метод для проверки,есть ли нажатые кнопки.Возвращает false если ни одна кнопка не нажата
    //и true если нажата хотя бы одна кнопка
    public boolean isListChecked(){
        UserDataSingleton userDataSingleton = UserDataSingleton.getInstance();
        List<UserData>l = userDataSingleton.getdataList();
        boolean result = false;
        for(int i=0;i<l.size();i++){
            if(l.get(i).isSolved()==true){
                result = true;
                break;
            }
        }
        Log.d(TAG,"result "+ result);
        return result;
    }

    public void btnConfirmClick(View v) {

        if(isListChecked()==true) {
            ContactPreferences.setStoredQuery(this,"listWithChecked");
        }
        else ContactPreferences.setStoredQuery(this,"listWithoutChecked");

        //TEST
        UserDataSingleton userDataSingleton = UserDataSingleton.getInstance();
        List<UserData>l = userDataSingleton.getdataList();
        for(int i=0;i<l.size();i++){
                Log.d(TAG,l.get(i).getContactName()+" "+l.get(i).isSolved());
            }


        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}


