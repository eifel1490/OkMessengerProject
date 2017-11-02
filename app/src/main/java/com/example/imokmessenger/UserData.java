package com.example.imokmessenger;

import java.util.List;

public class UserData {

    private String ContactImage;
    private String ContactName;
    private String ContactNumber;
    private boolean mSolved;
    private List<UserData> list;

    public void setList(List<UserData> list) {
        this.list = list;
    }

    public List<UserData> getList() {
        return list;
    }

    public void addToList(UserData userData){
        list.add(userData);
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

}

