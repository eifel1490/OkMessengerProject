package com.example.imokmessenger.Model;

import java.util.List;

public class UserData {

    private String ContactID;
    private String ContactName;
    private String ContactNumber;
    private String ContactSelect;
    //TODO если что удалить
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
    
    public String getContactID() {
        return ContactID;
    }
    
    public void setContactID(String contactID) {
        ContactID = contactID;
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
    
    public void setContactSelect(String contactSel) {
        ContactSelect = contactSel;
    }

    public String getContactSelect() {
        return ContactSelect;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

}

