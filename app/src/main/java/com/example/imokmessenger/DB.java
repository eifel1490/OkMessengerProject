package com.example.imokmessenger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Класс базы данных
 */

public class DB {

    //версия БД
    private static final int VERSION = 1;
    //имя БД
    private static final String DATABASE_NAME = "contactsBase.db";

    public static final class ContactsTable {
        //название таблицы
        public static final String DB_TABLE = "contacts";

        public static final class Cols {
            //столбец ИД
            public static final String ID = "contact_id";
            //столбец имя контакта
            public static final String NAME = "contact_name";
            //столбец номер контакта
            public static final String PHONE = "contact_phone";
            //столбец выбран ли контакт
            public static final String SELECTED = "selected";
        }
    }

    //команда создания таблицы контактов
    public static final String DB_CONTACTS_CREATE =
            "create table " + ContactsTable.DB_TABLE + "(" + " _id integer primary key autoincrement, " +
                    ContactsTable.Cols.ID + ", " +
                    ContactsTable.Cols.NAME + ", " +
                    ContactsTable.Cols.PHONE + ", " +
                    ContactsTable.Cols.SELECTED +
                    ")";

    private final Context mCtx;


    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        mCtx = ctx;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    // получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {
        return mDB.query(ContactsTable.DB_TABLE, null, null, null, null, null, null);
    }

    //метод для проверки,есть ли нажатые кнопки.Возвращает false если ни одна кнопка не нажата
    //и true если нажата хотя бы одна кнопка
    public boolean isListChecked(){
        //результат по умолчанию false
        boolean result = false;
        //открываем доступ к БД
        open();
        //создаем курсор,в который идет выкачка инфо методом query.Query вытягивает из таблицы только те значения в столце "Выбрано"
        //которые равны 1
        Cursor cursor = mDB.query(ContactsDbSchema.ContactsTable.DB_TABLE,
                new String[]{ContactsDbSchema.ContactsTable.Cols.SELECTED},"selected = ?",new String[]{"1"},null,null,null);
        //если курсор непустой,то значит есть минимум одно значение равное 1,тогде ставим результат true
        if(cursor.getCount()>0) result = true;
        //закрываем курсор
        cursor.close();
        //возвращаем результат
        return result;
    }

    //вставка в БД
    public void insert(ContentValues cv){
        mDB.insert(ContactsDbSchema.ContactsTable.DB_TABLE, null, cv);
    }

    //очистка БД
    public void deleteAllData(){
        mDB.delete(ContactsDbSchema.ContactsTable.DB_TABLE, null, null);
    }

    //обновление БД
    public void updateData(ContentValues cv,String contact_Id){
        mDB.update(ContactsDbSchema.ContactsTable.DB_TABLE , cv, ContactsDbSchema.ContactsTable.Cols.ID + " = ?",new String[] { contact_Id });
    }

    public Cursor queryData(){
        return mDB.query(ContactsDbSchema.ContactsTable.DB_TABLE,
                null,"selected = ?",new String[]{"1"},null,null,null);
    }




    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }

        //метод вызывается если БД еще не создана
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(ContactsDbSchema.DB_CONTACTS_CREATE);
        }

        //метод вызывается если БД обновляется
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    }


}

