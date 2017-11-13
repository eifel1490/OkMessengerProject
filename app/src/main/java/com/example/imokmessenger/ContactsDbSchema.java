

public class ContactsDbSchema {
  
  public static final class ContactsTable {
    //название таблицы
    public static final String NAME = "contacts";
    
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
}
