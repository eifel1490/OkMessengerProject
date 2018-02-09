package com.example.imokmessenger.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.imokmessenger.DataBase.ContactsDbSchema;
import com.example.imokmessenger.Model.UserData;

import java.util.ArrayList;
import java.util.List;



public class DB {

    private static final String TAG = "databaselog";
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "contactsBase.db";
    
    private final Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;
    
    public DB(Context ctx) {
            mCtx = ctx;
    }
    
    public void open() {
            mDBHelper = new DBHelper(mCtx);
            mDB = mDBHelper.getWritableDatabase();
    }
    
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }
    
    public Cursor getAllData() {
        return mDB.query(ContactsTable.DB_TABLE, null, null, null, null, null, null);
    }
    
    //Method to check if there are any buttons pressed. Returns false if no button is pressed
    // and true if at least one button is pressed
    public boolean isListChecked(){
        
        boolean result = false;
        open();
        
        Cursor cursor = mDB.query(ContactsDbSchema.ContactsTable.DB_TABLE,
                new String[]{ContactsDbSchema.ContactsTable.Cols.SELECTED},"selected = ?",new String[]{"1"},null,null,null);
        
        if(cursor.getCount()>0) result = true;
        cursor.close();
        
        return result;
    }
    
    //returns a list of checked contacts for info on the main page
    public List<String> getCheckedContacts(){
        List<String>list= new ArrayList<>();
        Cursor c = mDB.query(ContactsDbSchema.ContactsTable.DB_TABLE,
                new String[]{ContactsDbSchema.ContactsTable.Cols.NAME},"selected = ?",new String[]{"1"},null,null,null);
        if(c!=null) {
            try {

                if (c.moveToFirst()) {
                    int nameColIndex = c.getColumnIndex("contact_name");
                    do {
                        Log.d(TAG, c.getString(nameColIndex));
                        list.add(c.getString(nameColIndex));
                    }
                    while (c.moveToNext());
                }
            } finally {
                if (c != null) {
                    c.close();
                }
            }

        }
        return list;
    }
    
    
    public void insert(ContentValues cv){
        mDB.insert(ContactsDbSchema.ContactsTable.DB_TABLE, null, cv);
    }

    
    public void deleteAllData(){
        mDB.delete(ContactsDbSchema.ContactsTable.DB_TABLE, null, null);
    }

    
    public void updateData(ContentValues cv,String contact_Id){
        mDB.update(ContactsDbSchema.ContactsTable.DB_TABLE , cv, ContactsDbSchema.ContactsTable.Cols.ID + " = ?",new String[] { contact_Id });
    }

    //returns all records where "selected" equals 1
    public Cursor queryData(){
        return mDB.query(ContactsDbSchema.ContactsTable.DB_TABLE,
                null,"selected = ?",new String[]{"1"},null,null,null);
    }
    
    public static final String DB_CONTACTS_CREATE =
            "create table " + ContactsTable.DB_TABLE + "(" + " _id integer primary key autoincrement, " +
                    ContactsTable.Cols.ID + ", " +
                    ContactsTable.Cols.NAME + ", " +
                    ContactsTable.Cols.PHONE + ", " +
                    ContactsTable.Cols.SELECTED +
                    ")";

    

    public static final class ContactsTable {
        
        public static final String DB_TABLE = "contacts";

        public static final class Cols {
            
            public static final String ID = "contact_id";
            public static final String NAME = "contact_name";
            public static final String PHONE = "contact_phone";
            public static final String SELECTED = "selected";
        }
    }

    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(ContactsDbSchema.DB_CONTACTS_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    }
}

