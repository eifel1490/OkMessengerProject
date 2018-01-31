package com.example.imokmessenger.Fragments;

//вставить импорты


public class LicenseFragment extends Fragment implements MainActivityND.OnBackPressedListener  {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //LayoutInflater – это класс, который умеет из содержимого layout-файла создать View-элемент.
        LayoutInflater dialogInflater = getActivity().getLayoutInflater();
        //создаем вью из макета fragment_licenses
        View openSourceLicensesView = dialogInflater.inflate(R.layout.fragment_licenses, null);
        //создаем диалог
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        
        dialogBuilder.setView(openSourceLicensesView)
                //устанавливаем заголовок
                .setTitle((getString(R.string.dialog_title_licenses)))
                //устанавливаем нейтральную кнопку ОК
                .setNeutralButton(android.R.string.ok, null);

        return dialogBuilder.create();
    }

}
