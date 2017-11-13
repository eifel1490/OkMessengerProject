

public class ContactsBaseHelper extends SQLiteOpenHelper {
  
  //версия БД
  private static final int VERSION = 1;
  //имя БД
  private static final String DATABASE_NAME = "contactsBase.db";
  
  public CrimeBaseHelper(Context context) {
    super(context, DATABASE_NAME, null, VERSION);
  }
  
  //метод вызывается если БД еще не создана
  @Override
  public void onCreate(SQLiteDatabase db) {}
  
  //метод вызывается если БД
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
  
}
