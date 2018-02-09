package com.example.imokmessenger.Adapter;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.amulyakhare.textdrawable.TextDrawable;

import com.example.imokmessenger.DataBase.ContactsDbSchema;
import com.example.imokmessenger.DataBase.DB;
import com.example.imokmessenger.Model.UserData;
import com.example.imokmessenger.R;

import java.util.List;
import java.util.Random;

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.ContactViewHolder>{

    public static final String TAG = "myTag";
    DB db;
    private static List<UserData> userDataList;
    private Context mContext;
    
    public UserDataAdapter(List<UserData> userDataList, Context mContext){
        this.userDataList = userDataList;
        this.mContext = mContext;
        db = new DB(mContext);
    }

    public static List<UserData> getList(){
        return userDataList;
    }

    
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        
        View view = LayoutInflater.from(mContext).inflate(R.layout.m_single_contact_view, parent, false);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, int position) {
        
        final UserData userData = userDataList.get(position);
        
        holder.tvContactName.setText(userData.getContactName());
        holder.tvPhoneNumber.setText(userData.getContactNumber());

        holder.tvCheckContact.setOnCheckedChangeListener(null);

        holder.tvCheckContact.setChecked(userData.isSolved());

        holder.tvCheckContact.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked) {
                    userDataList.get(holder.getAdapterPosition()).setSolved(isChecked);
                    addDataToDB(userDataList.get(holder.getAdapterPosition()).getContactID(),"1");
                    
                    Intent intent = new Intent("custom-message");
                    intent.putExtra("message", "pressed");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
                else {
                    userDataList.get(holder.getAdapterPosition()).setSolved(false);
                    addDataToDB(userDataList.get(holder.getAdapterPosition()).getContactID(),"0");
                    
                    Intent intent = new Intent("custom-message");
                    intent.putExtra("message", "unpressed");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }

        });

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        TextDrawable myDrawable = TextDrawable.builder().beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .toUpperCase()
                .endConfig()
                .buildRound(userData.getContactName().substring(0,1),color);

        holder.mColorImageView.setImageDrawable(myDrawable);
    }

    
    @Override
    public int getItemCount() {
        return userDataList.size();
    }
    
    //update an entry in the database by ID
    public void addDataToDB(String contact_Id, String index){
        Log.d(TAG,"ID =" + contact_Id);
        db.open();
        
        ContentValues cv = new ContentValues();
        cv.put(ContactsDbSchema.ContactsTable.Cols.SELECTED, index);
        db.updateData(cv,contact_Id);
        db.close();
    }


    
    


    public static class ContactViewHolder extends RecyclerView.ViewHolder{

        CheckBox tvCheckContact;
        TextView tvContactName;
        TextView tvPhoneNumber;
        ImageView mColorImageView;

        public ContactViewHolder(View itemView) {
            super(itemView);
            tvCheckContact = (CheckBox) itemView.findViewById(R.id.list_item_check_box);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
            mColorImageView = (ImageView)itemView.findViewById(R.id.toDoListItemColorImageView);
        }
    }
}


