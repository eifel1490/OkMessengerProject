package com.example.imokmessenger;

public class ContactsDbSchema {
  
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

  public static final class ContactsMessage {

    public static final String DB_MESSAGE_TABLE = "contacts";

      public static final class Cols {
        public static final String MESSAGE_TEXT = "message_text";
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

  //команда создания таблицы сообщения
  public static final String DB_MESSAGE_CREATE =
          "create table " + ContactsMessage.DB_MESSAGE_TABLE + "("
                  + ContactsMessage.Cols.MESSAGE_TEXT + ")";

}
