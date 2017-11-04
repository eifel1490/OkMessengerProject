package com.example.imokmessenger;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.ContactViewHolder>{

    public static final String TAG = "myLog";

    //список контактов
    private static List<UserData> userDataList;
    //обьект контекста
    private Context mContext;
    //конструктор адаптера,на вход принимает список контактов и обьект контекста
    public UserDataAdapter(List<UserData> userDataList, Context mContext){
        this.userDataList = userDataList;
        this.mContext = mContext;
    }

    public static List<UserData> getList(){
        return userDataList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //с помощью инфлятора "надуваем" вью из макета single_contact_view
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_contact_view, null);
        //создаем обьект ContactViewHolder contactViewHolder,в конструктор передаем вью
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {
        //получаем обьект Контакт из списка по позиции
        final UserData userData = userDataList.get(position);
        //с помощью обьекта holder присваиваем полям tvContactName и tvPhoneNumber значения из обьекта UserData
        holder.tvContactName.setText(userData.getContactName());
        holder.tvPhoneNumber.setText(userData.getContactNumber());

        //in some cases, it will prevent unwanted situations
        holder.tvCheckContact.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.tvCheckContact.setChecked(userData.isSolved());

        holder.tvCheckContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    userDataList.get(holder.getAdapterPosition()).setSolved(isChecked);
                    Intent intent = new Intent("custom-message");
                    intent.putExtra("message", "pressed");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
                else {
                    userDataList.get(holder.getAdapterPosition()).setSolved(false);
                    Intent intent = new Intent("custom-message");
                    intent.putExtra("message", "unpressed");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }

        });
    }



    //метод возвращает размер листа
    @Override
    public int getItemCount() {
        return userDataList.size();
    }

    //
    public static class ContactViewHolder extends RecyclerView.ViewHolder{

        CheckBox tvCheckContact;
        TextView tvContactName;
        TextView tvPhoneNumber;

        public ContactViewHolder(View itemView) {
            super(itemView);
            tvCheckContact = (CheckBox) itemView.findViewById(R.id.list_item_check_box);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
        }
    }
}


