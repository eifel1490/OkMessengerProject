package com.example.imokmessenger;



import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/*класс отображающий список конактов для пользователя*/
public class ContactListActivity extends AppCompatActivity {

    //Recycleview
    RecyclerView rvContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_contacts_activity);
        //инициализируем Recycleview
        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        getAllContacts();
    }

    private void getAllContacts() {
        //создаем список контактов
        List<UserData> contactUserDataList = new ArrayList();
        //обьект контакта
        UserData userData;
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
                    //...и вычитываем значение имени контакта,все это сохраняем соответственно в String id и String name
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    //создаем обьект Контакта
                    userData = new UserData();
                    //заполняем его поле "имя"
                    userData.setContactName(name);
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
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //заполняем поле обьекта Контакт "телефонный номер"
                        userData.setContactNumber(phoneNumber);
                    }
                    //закрываем phoneCursor
                    phoneCursor.close();

                    /*НЕНУЖНЫЙ БЛОК "ПОЛУЧЕНИЕ ИМЕЙЛА"
                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    }*/
                    //поле "выбрано" по умолчанию ставим в "Не выбрано"
                    userData.setSolved(false);
                    //добавляем обьект в contactUserDataList
                    contactUserDataList.add(userData);
                }
            }
            //создаем обьект адаптера,ему передается ArrayList и обьект контекста
            UserDataAdapter contactAdapter = new UserDataAdapter(contactUserDataList, getApplicationContext());
            //расположение будет вертикальным списком
            rvContacts.setLayoutManager(new LinearLayoutManager(this));
            //присваиваем адаптер
            rvContacts.setAdapter(contactAdapter);
        }
    }
}


