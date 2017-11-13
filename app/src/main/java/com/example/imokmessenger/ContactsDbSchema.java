

public class ContactsDbSchema {
  
  public static final class ContactsTable {
    //название таблицы
    public static final String DB_TABLE = "contacts";
    
    public static final class Cols {
      //столбец ИД
      public static final String UUID = "uuid";
      //столбец имя контакта
      public static final String NAME = "contact_name";
      //столбец номер контакта
      public static final String PHONE = "contact_phone";
      //столбец выбран ли контакт
      public static final String SELECTED = "selected";
    }
  
  }
  
  //команда создания БД
  private static final String DB_CONTACTS_CREATE =
      "create table " + ContactsTable.DB_NAME + "(" + " _id integer primary key autoincrement, " +
      ContactsTable.Cols.UUID + ", " +
      ContactsTable.Cols.NAME + ", " +
      ContactsTable.Cols.PHONE + ", " +
      ContactsTable.Cols.SELECTED +
  ")";
  
  
    
}
