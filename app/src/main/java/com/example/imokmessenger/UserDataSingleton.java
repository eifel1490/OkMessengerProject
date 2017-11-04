package com.example.imokmessenger;

import java.util.ArrayList;
import java.util.List;


//синглтон для хранения данных контактов
public class UserDataSingleton {

    private static UserDataSingleton userDataSingleton;
    private List<UserData> dataList;

    private UserDataSingleton() {
        dataList = new ArrayList<>();
    }

    public static UserDataSingleton getInstance() {
        if (userDataSingleton == null) {
            userDataSingleton = new UserDataSingleton();
        }
        return userDataSingleton;
    }

    public void addData(UserData userData) {
        dataList.add(userData);
    }



    public List<UserData> getdataList() {
        return dataList;
    }

}

