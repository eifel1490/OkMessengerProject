package com.example.imokmessenger.DataBase;

public class ContactsDbSchema {
  
  public static final class ContactsTable {
    
    public static final String DB_TABLE = "contacts";
    
      public static final class Cols {
        
        public static final String ID = "contact_id";
        public static final String NAME = "contact_name";
        public static final String PHONE = "contact_phone";
        public static final String SELECTED = "selected";
      }
    }

  
  public static final String DB_CONTACTS_CREATE =
      "create table " + ContactsTable.DB_TABLE + "(" + " _id integer primary key autoincrement, " +
      ContactsTable.Cols.ID + ", " +
      ContactsTable.Cols.NAME + ", " +
      ContactsTable.Cols.PHONE + ", " +
      ContactsTable.Cols.SELECTED +
  ")";
}
