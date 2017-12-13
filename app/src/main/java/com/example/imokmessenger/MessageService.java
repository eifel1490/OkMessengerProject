

/*сервис.Будет работать в фоне*/

public class MessageService extends IntentService {

   @Override
    protected void onHandleIntent(Intent intent) {
        //,прослушивать BroadcastReceiver и при достижении
        //уровня аккумулятора меньше Х процентов выполнять действие
        //здесь выполняется код из ManageMessage
    }


}
