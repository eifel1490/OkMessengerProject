import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;




public class UserRulesFragment extends Fragment implements MainActivityND.OnBackPressedListener  {

    public static final String TAG = "UserRulesFragment";

    @BindView(R.id.button_ok) Button text;
    
    private Unbinder unbinder;
    

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivityND) getActivity()).setOnBackPressedListener(this);
    }

    @Override
    public void doBack() {
        goToHostActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.user_rules, container, false);
        unbinder = ButterKnife.bind(this, v);

        return  v;
    }

    @OnClick(R.id.button_ok)
    void onButtonOkClick() {
        goToHostActivity();
    }

    public void goToHostActivity(){
        //создаем интент на MainActivity
        Intent intent = new Intent(getContext(),MainActivityND.class);
        //очищаем бэкстек
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        //стартуем интент
        startActivity(intent);
    }

    //необходимо отключить butterknife
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
