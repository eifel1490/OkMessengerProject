

public class UserDataBaseHelper extends SQLiteOpenHelper {
  private static final int VERSION = 1;
  private static final String DATABASE_NAME = "userDataBase.db";
  
  public UserDataBaseHelper(Context context) {
    super(context, DATABASE_NAME, null, VERSION);
  }
  
  @Override
  public void onCreate(SQLiteDatabase db) {}

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

}
